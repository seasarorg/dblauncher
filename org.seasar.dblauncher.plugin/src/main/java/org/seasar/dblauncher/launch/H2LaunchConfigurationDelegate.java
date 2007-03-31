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

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate;
import org.eclipse.jdt.launching.JavaLaunchDelegate;
import org.seasar.dblauncher.DbLauncherPlugin;
import org.seasar.dblauncher.decorator.DBRunningDecorator;
import org.seasar.eclipse.common.util.LaunchUtil;

/**
 * @author taichi
 * 
 */
public class H2LaunchConfigurationDelegate extends JavaLaunchDelegate implements
        ILaunchConfigurationDelegate {
    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jdt.launching.JavaLaunchDelegate#launch(org.eclipse.debug.core.ILaunchConfiguration,
     *      java.lang.String, org.eclipse.debug.core.ILaunch,
     *      org.eclipse.core.runtime.IProgressMonitor)
     */
    public void launch(ILaunchConfiguration configuration, String mode,
            ILaunch launch, IProgressMonitor monitor) throws CoreException {
        super.launch(configuration, mode, launch, monitor);

        IProject project = LaunchUtil.getProject(launch);
        if (project != null && launch.isTerminated() == false) {
            DbLauncherPlugin.setLaunch(project, launch);
            DBRunningDecorator.updateDecorators(project);
        }
    }
}
