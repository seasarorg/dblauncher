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
package org.seasar.dblauncher.action;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.jdt.launching.IRuntimeClasspathEntry;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.PlatformUI;
import org.h2.tools.RunScript;
import org.seasar.dblauncher.DbLauncherPlugin;
import org.seasar.dblauncher.launch.H2ConfigurationBuilder;
import org.seasar.dblauncher.preferences.H2Preferences;
import org.seasar.dblauncher.variable.H2SrcVariable;
import org.seasar.dblauncher.variable.H2Variable;
import org.seasar.framework.util.StringUtil;

/**
 * @author taichi
 * 
 */
public class QueryExecuteAction implements IActionDelegate {

    private IFile sql = null;

    /**
     * 
     */
    public QueryExecuteAction() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
     */
    public void run(IAction action) {
        if (this.sql == null
                || "sql".equalsIgnoreCase(this.sql.getFileExtension()) == false) {
            return;
        }
        IProject project = this.sql.getProject();
        H2Preferences pref = DbLauncherPlugin.getPreferences(project);
        if (pref == null) {
            return;
        }
        String name = "H2 " + project.getName() + "[" + this.sql.getName()
                + "]";

        H2ConfigurationBuilder builder = new H2ConfigurationBuilder(name,
                project, JavaRuntime
                        .newVariableRuntimeClasspathEntry(H2Variable.LIB));
        builder.setMainClass(RunScript.class.getName());
        builder.setArgs(buildBootArgs(pref));
        builder.setSrcpath(new IRuntimeClasspathEntry[] { JavaRuntime
                .newVariableRuntimeClasspathEntry(H2SrcVariable.SRC) });

        try {
            ILaunchConfiguration config = builder.build();
            config.launch(pref.isDebug() ? ILaunchManager.DEBUG_MODE
                    : ILaunchManager.RUN_MODE, null);
            MessageDialog.openInformation(PlatformUI.getWorkbench()
                    .getDisplay().getActiveShell(), "H2 Sql Executor",
                    "Finished Query ...");
        } catch (CoreException e) {
            DbLauncherPlugin.log(e);
        }

    }

    private String buildBootArgs(H2Preferences pref) {
        StringBuffer stb = new StringBuffer();
        stb.append(" -url jdbc:h2:tcp://localhost:");
        stb.append(pref.getDbPortNo());
        stb.append("/");
        stb.append(" -user ");
        stb.append(pref.getUser());
        if (StringUtil.isEmpty(pref.getPassword()) == false) {
            stb.append(" -password ");
            stb.append(pref.getPassword());
        }
        stb.append(" -script ");
        stb.append("\"");
        stb.append(this.sql.getLocation().toString());
        stb.append("\"");

        return stb.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction,
     *      org.eclipse.jface.viewers.ISelection)
     */
    public void selectionChanged(IAction action, ISelection selection) {
        if (selection instanceof IStructuredSelection) {
            IStructuredSelection ss = (IStructuredSelection) selection;
            Object o = ss.getFirstElement();
            if (o instanceof IAdaptable) {
                IAdaptable a = (IAdaptable) o;
                this.sql = (IFile) a.getAdapter(IFile.class);
            }
        }
    }

}
