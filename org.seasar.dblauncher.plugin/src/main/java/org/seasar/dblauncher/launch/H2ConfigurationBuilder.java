/*
 * Copyright 2004-2006 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, 
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.dblauncher.launch;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jdt.launching.IRuntimeClasspathEntry;
import org.h2.tools.Server;
import org.seasar.dblauncher.Constants;
import org.seasar.dblauncher.DbLauncherPlugin;
import org.seasar.dblauncher.preferences.H2Preferences;
import org.seasar.eclipse.common.util.ProjectUtil;

/**
 * @author taichi
 * 
 */
public class H2ConfigurationBuilder {

    private IProject project;

    private String name;

    private String mainClass = Server.class.getName();

    private IRuntimeClasspathEntry[] classpath;

    private IRuntimeClasspathEntry[] srcpath;

    private String args;

    public H2ConfigurationBuilder(String name, IProject project,
            IRuntimeClasspathEntry entry) {
        super();
        this.name = name;
        this.project = project;
        this.classpath = new IRuntimeClasspathEntry[] { entry };
    }

    public ILaunchConfiguration build() throws CoreException {
        ILaunchManager manager = DebugPlugin.getDefault().getLaunchManager();
        ILaunchConfigurationType type = manager
                .getLaunchConfigurationType(Constants.ID_H2_LAUNCH_CONFIG);
        ILaunchConfiguration config = null;
        ILaunchConfiguration[] configs = manager.getLaunchConfigurations(type);
        for (int i = 0; i < configs.length; i++) {
            if (configs[i].getName().equals(getName())) {
                String current = configs[i]
                        .getAttribute(
                                IJavaLaunchConfigurationConstants.ATTR_PROGRAM_ARGUMENTS,
                                "");
                if (current.equals(getArgs())) {
                    config = configs[i];
                } else {
                    configs[i].delete();
                }
                break;
            }
        }
        if (config == null) {
            ILaunchConfigurationWorkingCopy copy = type.newInstance(null,
                    getName());
            copy.setAttribute(
                    IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME,
                    project.getName());

            H2Preferences pref = DbLauncherPlugin.getPreferences(project);
            IPath baseDirPath = new Path(pref.getBaseDir());
            IWorkspaceRoot root = ProjectUtil.getWorkspaceRoot();
            IContainer c = root.getFolder(baseDirPath);
            String workDir = project.getLocation().toString();
            if (c.exists()) {
                workDir = baseDirPath.toString();
            }
            copy.setAttribute(
                    IJavaLaunchConfigurationConstants.ATTR_WORKING_DIRECTORY,
                    workDir);

            if (hasMain(getProject(), getMainClass()) == false) {
                copy
                        .setAttribute(
                                IJavaLaunchConfigurationConstants.ATTR_DEFAULT_CLASSPATH,
                                false);
                copy.setAttribute(
                        IJavaLaunchConfigurationConstants.ATTR_CLASSPATH,
                        toMemento(getClasspath()));
                if (this.srcpath != null && 0 < this.srcpath.length) {
                    copy
                            .setAttribute(
                                    IJavaLaunchConfigurationConstants.ATTR_DEFAULT_SOURCE_PATH,
                                    false);
                    copy.setAttribute(
                            IJavaLaunchConfigurationConstants.ATTR_SOURCE_PATH,
                            toMemento(getSrcpath()));
                }
            }

            copy.setAttribute(
                    IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME,
                    getMainClass());
            copy.setAttribute(
                    IJavaLaunchConfigurationConstants.ATTR_PROGRAM_ARGUMENTS,
                    getArgs());
            config = copy.doSave();
        }
        return config;
    }

    private List toMemento(IRuntimeClasspathEntry[] classpath)
            throws CoreException {
        List classpathList = new ArrayList(classpath.length);
        for (int i = 0; i < classpath.length; i++) {
            classpathList.add(classpath[i].getMemento());
        }
        return classpathList;
    }

    public static boolean hasMain(IProject project, String mainClass) {
        boolean result = false;
        try {
            IJavaProject jp = JavaCore.create(project);
            IType type = jp.findType(mainClass);
            result = type != null && type.exists();
        } catch (JavaModelException e) {
            DbLauncherPlugin.log(e);
        }
        return result;
    }

    /**
     * @return Returns the args.
     */
    public String getArgs() {
        return this.args;
    }

    /**
     * @param args
     *            The args to set.
     */
    public void setArgs(String args) {
        this.args = args;
    }

    /**
     * @return Returns the classpath.
     */
    public IRuntimeClasspathEntry[] getClasspath() {
        return this.classpath;
    }

    /**
     * @return Returns the name.
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return Returns the project.
     */
    public IProject getProject() {
        return this.project;
    }

    /**
     * @return Returns the srcpath.
     */
    public IRuntimeClasspathEntry[] getSrcpath() {
        return this.srcpath;
    }

    /**
     * @param srcpath
     *            The srcpath to set.
     */
    public void setSrcpath(IRuntimeClasspathEntry[] srcpath) {
        this.srcpath = srcpath;
    }

    /**
     * @return Returns the mainClass.
     */
    public String getMainClass() {
        return this.mainClass;
    }

    /**
     * @param mainClass
     *            The mainClass to set.
     */
    public void setMainClass(String mainClass) {
        this.mainClass = mainClass;
    }
}
