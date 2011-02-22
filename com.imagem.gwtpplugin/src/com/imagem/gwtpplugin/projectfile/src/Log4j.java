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

package com.imagem.gwtpplugin.projectfile.src;

import java.io.ByteArrayInputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IPackageFragmentRoot;

import com.imagem.gwtpplugin.projectfile.ProjectSrcFile;

/**
 * 
 * @author Michael Renaud
 *
 */
public class Log4j extends ProjectSrcFile {
	
	public Log4j(IPackageFragmentRoot root) throws CoreException {
		super(root, "", "log4j.properties");
	}
	
	public IFile createFile() throws CoreException {
		String contents = "# A default log4j configuration for log4j users.\n";
		contents += "#\n";
		contents += "# To use this configuration, deploy it into your application's WEB-INF/classes\n";
		contents += "# directory.  You are also encouraged to edit it as you like.\n\n";

		contents += "# Configure the console as our one appender\n";
		contents += "log4j.appender.A1=org.apache.log4j.ConsoleAppender\n";
		contents += "log4j.appender.A1.layout=org.apache.log4j.PatternLayout\n";
		contents += "log4j.appender.A1.layout.ConversionPattern=%d{HH:mm:ss,SSS} %-5p [%c] - %m%n\n\n";

		contents += "# tighten logging on the DataNucleus Categories\n";
		contents += "log4j.category.DataNucleus.JDO=WARN, A1\n";
		contents += "log4j.category.DataNucleus.Persistence=WARN, A1\n";
		contents += "log4j.category.DataNucleus.Cache=WARN, A1\n";
		contents += "log4j.category.DataNucleus.MetaData=WARN, A1\n";
		contents += "log4j.category.DataNucleus.General=WARN, A1\n";
		contents += "log4j.category.DataNucleus.Utility=WARN, A1\n";
		contents += "log4j.category.DataNucleus.Transaction=WARN, A1\n";
		contents += "log4j.category.DataNucleus.Datastore=WARN, A1\n";
		contents += "log4j.category.DataNucleus.ClassLoading=WARN, A1\n";
		contents += "log4j.category.DataNucleus.Plugin=WARN, A1\n";
		contents += "log4j.category.DataNucleus.ValueGeneration=WARN, A1\n";
		contents += "log4j.category.DataNucleus.Enhancer=WARN, A1\n";
		contents += "log4j.category.DataNucleus.SchemaTool=WARN, A1\n";
		
		file.create(new ByteArrayInputStream(contents.getBytes()), false, null);
		
		return file;
	}
}
