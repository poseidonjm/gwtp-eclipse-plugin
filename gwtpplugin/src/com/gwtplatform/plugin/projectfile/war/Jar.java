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

package com.gwtplatform.plugin.projectfile.war;

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Platform;

import com.gwtplatform.plugin.Activator;
import com.gwtplatform.plugin.projectfile.ProjectFile;

/**
 *
 * @author Michael Renaud
 *
 */
public class Jar extends ProjectFile {

  public Jar(IProject project, IPath path, String name) throws CoreException {
    super(project, path, name + ".jar");
  }

  public IFile createFile() throws IOException, CoreException {
    InputStream inputStream = Platform.getBundle(Activator.PLUGIN_ID)
        .getEntry("/file/" + file.getName()).openStream();

    file.create(inputStream, false, new NullProgressMonitor());

    return file;
  }
}
