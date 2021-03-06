/*******************************************************************************
 * Copyright (c) 2008, 2012 Red Hat Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Incorporated - initial API and implementation
 *     Marc-Andre Laperle - Fix failing test on Windows
 *******************************************************************************/
package org.eclipse.cdt.autotools.tests;

import java.io.File;

import junit.framework.TestCase;

import org.eclipse.cdt.autotools.core.AutotoolsNewProjectNature;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.URIUtil;

// This test verifies using Autotools with a linked folder.
public class AutotoolsVirtualFolderTest extends TestCase {

	private IProject testProject;

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		if (!ProjectTools.setup())
			fail("could not perform basic project workspace setup");
		testProject = ProjectTools.createProject("testProjectVirtualFolder");
		if (testProject == null) {
			fail("Unable to create test project");
		}
		testProject.open(new NullProgressMonitor());
	}

	/**
	 * Test sample project with a virtual folder that points to configure scripts.
	 * Tests Bug 434275 - Autotools configuration in subfolder not found 
	 * @throws Exception
	 */
	public void testAutotoolsVirtualFolder() throws Exception {
		Path p = new Path("zip/project2.zip");
		IWorkspaceRoot root = ProjectTools.getWorkspaceRoot();
		IPath rootPath = root.getLocation();
		IPath configPath = rootPath.append("config");
		File configDir = configPath.toFile();
		configDir.deleteOnExit();
		assertTrue(configDir.mkdir());
		ProjectTools.createLinkedFolder(testProject, "src", URIUtil.append(root.getLocationURI(), "config"));
		ProjectTools.addSourceContainerWithImport(testProject, "src", p, null);
		assertTrue(testProject.hasNature(AutotoolsNewProjectNature.AUTOTOOLS_NATURE_ID));
		assertTrue(exists("src/ChangeLog"));
		ProjectTools.setConfigDir(testProject, "src");
		ProjectTools.markExecutable(testProject, "src/autogen.sh");
		assertFalse(exists("src/configure"));
		assertFalse(exists("src/Makefile.in"));
		assertFalse(exists("src/sample/Makefile.in"));
		assertFalse(exists("src/aclocal.m4"));
		assertTrue(ProjectTools.build());
		assertTrue(exists("src/configure"));
		assertTrue(exists("src/Makefile.in"));
		assertTrue(exists("src/sample/Makefile.in"));
		assertTrue(exists("src/aclocal.m4"));
		assertTrue(exists("config.status"));
		assertTrue(exists("Makefile"));
		String extension = Platform.getOS().equals(Platform.OS_WIN32) ? ".exe" : "";
		assertTrue(exists("sample/a.out" + extension));
		assertTrue(exists("sample/Makefile"));
	}

	private boolean exists(String path) {
		return testProject.exists(new Path(path));
	}

	protected void tearDown() throws Exception {
		testProject.refreshLocal(IResource.DEPTH_INFINITE, null);
		try {
			testProject.delete(true, true, null);
		} catch (Exception e) {
			//FIXME: Why does a ResourceException occur when deleting the project??
		}
		super.tearDown();
	}

}
