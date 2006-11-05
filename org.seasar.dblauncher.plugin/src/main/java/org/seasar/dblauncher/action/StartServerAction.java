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

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.jdt.launching.IRuntimeClasspathEntry;
import org.eclipse.jdt.launching.JavaRuntime;
import org.seasar.dblauncher.Constants;
import org.seasar.dblauncher.DbLauncherPlugin;
import org.seasar.dblauncher.decorator.DBRunningDecorator;
import org.seasar.dblauncher.launch.H2ConfigurationBuilder;
import org.seasar.dblauncher.preferences.H2Preferences;
import org.seasar.dblauncher.variable.H2SrcVariable;
import org.seasar.dblauncher.variable.H2Variable;
import org.seasar.eclipse.common.util.ProjectUtil;

/**
 * @author taichi
 * 
 */
public class StartServerAction extends AbstractProjectAction {

    /**
     * 
     */
    public StartServerAction() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.seasar.h2.action.AbstractProjectAction#run(org.eclipse.core.resources.IProject)
     */
    protected void run(IProject project) throws CoreException {
        H2Preferences pref = DbLauncherPlugin.getPreferences(project);
        if (pref != null) {
            String name = "H2 " + project.getName();
            H2ConfigurationBuilder builder = new H2ConfigurationBuilder(name,
                    project, JavaRuntime
                            .newVariableRuntimeClasspathEntry(H2Variable.LIB));
            builder.setArgs(buildBootArgs(pref));
            builder.setSrcpath(new IRuntimeClasspathEntry[] { JavaRuntime
                    .newVariableRuntimeClasspathEntry(H2SrcVariable.SRC) });
            ILaunchConfiguration config = builder.build();
            ILaunch launch = config.launch(
                    pref.isDebug() ? ILaunchManager.DEBUG_MODE
                            : ILaunchManager.RUN_MODE, null);
            launch.setAttribute(Constants.KEY_H2_LAUNCH, "TRUE");

            DbLauncherPlugin.setLaunch(project, launch);
            DBRunningDecorator.updateDecorators(project);
        }
    }

    public String buildBootArgs(H2Preferences pref) {
        StringBuffer stb = new StringBuffer();
        stb.append(" -tcp -tcpPort ");
        stb.append(pref.getDbPortNo());
        stb.append(" -web -webPort ");
        stb.append(pref.getWebPortNo());

        IWorkspaceRoot root = ProjectUtil.getWorkspaceRoot();
        IContainer c = root.getFolder(new Path(pref.getBaseDir()));
        IPath p = c.getLocation();
        stb.append(" -baseDir \"");
        stb.append(p.toString());
        stb.append("\"");
        return stb.toString();
    }

}
