/**
 * Copyright 2011 IMAGEM Solutions TI sant�
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gwtplatform.plugin.wizard;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

import com.gwtplatform.plugin.Activator;
import com.gwtplatform.plugin.SourceWriterFactory;
import com.gwtplatform.plugin.projectfile.Field;
import com.gwtplatform.plugin.projectfile.src.client.event.Event;

/**
 *
 * @author Michael Renaud
 *
 */
public class NewEventWizard extends Wizard implements INewWizard {

  private final SourceWriterFactory sourceWriterFactory;

  private NewEventWizardPage page;
  private IStructuredSelection selection;
  private boolean isDone;

  public NewEventWizard() {
    super();
    setNeedsProgressMonitor(true);
    setWindowTitle("New Event");
    sourceWriterFactory = new SourceWriterFactory();
  }

  @Override
  public void addPages() {
    page = new NewEventWizardPage(selection);
    addPage(page);
  }

  @Override
  public void init(IWorkbench workbench, IStructuredSelection selection) {
    this.selection = selection;
  }

  @Override
  public boolean performFinish() {
    try {
      super.getContainer().run(false, false, new IRunnableWithProgress() {
        @Override
        public void run(IProgressMonitor monitor) throws InvocationTargetException,
            InterruptedException {
          isDone = finish(monitor);
        }
      });
    } catch (Exception e) {
      return false;
    }
    return isDone;
  }

  protected boolean finish(IProgressMonitor desiredMonitor) {
    IProgressMonitor monitor = desiredMonitor;
    if (monitor == null) {
      monitor = new NullProgressMonitor();
    }

    Event event = null;
    try {
      monitor.beginTask("Event creation", 2);

      IPackageFragmentRoot root = page.getPackageFragmentRoot();

      monitor.subTask("Event");
      event = new Event(root, page.getPackageText(), page.getTypeName(), sourceWriterFactory);
      IType handler = event.createHandlerInterface();
      event.createTypeField(handler);

      Field[] eventFields = page.getFields();
      IField[] fields = new IField[eventFields.length];
      for (int i = 0; i < eventFields.length; i++) {
        if (eventFields[i].isPrimitiveType()) {
          fields[i] = event
              .createField(eventFields[i].getPrimitiveType(), eventFields[i].getName());
        } else {
          fields[i] = event.createField(eventFields[i].getType(), eventFields[i].getName());
        }
      }
      IMethod constructor = event.createConstructor(fields);
      for (IField field : fields) {
        event.createGetterMethod(field);
      }

      event.createDispatchMethod(handler);
      event.createAssociatedTypeGetterMethod(handler);
      event.createTypeGetterMethod(handler);
      monitor.worked(1);

      if (page.hasHandlers()) {
        monitor.subTask("HasHandlers");
        // TODO Do we need to do something with the HasHandlers interface?
        event.createHasHandlersInterface(handler, constructor);
        event.createFireMethod(fields);
      } else {
        event.createFireMethod(fields);
      }

      monitor.worked(1);

      // Committing
      if (event != null) {
        event.commit();
      }
    } catch (JavaModelException e) {
      e.printStackTrace();

      try {
        if (event != null) {
          event.discard(true);
        }
      } catch (JavaModelException e1) {
      }
      
      IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, "An unexpected error has happened. Close the wizard and retry.", e);
      
      ErrorDialog.openError(getShell(), null, null, status);

      return false;
    }

    monitor.done();
    return true;
  }

}
