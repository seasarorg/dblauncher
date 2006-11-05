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

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunch;
import org.seasar.dblauncher.DbLauncherPlugin;

/**
 * @author taichi
 * 
 */
public class StopServerAction extends AbstractProjectAction {

    /**
     * 
     */
    public StopServerAction() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.seasar.h2.action.AbstractProjectAction#run(org.eclipse.core.resources.IProject)
     */
    protected void run(IProject project) throws CoreException {
        ILaunch launch = DbLauncherPlugin.getLaunch(project);
        if (launch != null) {
            launch.terminate();
        }
    }
}