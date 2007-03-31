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

import java.net.URL;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWebBrowser;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;
import org.seasar.dblauncher.DbLauncherPlugin;
import org.seasar.dblauncher.preferences.H2Preferences;
import org.seasar.eclipse.common.action.AbstractProjectAction;

/**
 * @author taichi
 * 
 */
public class ViewDatabaseManagerAction extends AbstractProjectAction {

    /**
     * 
     */
    public ViewDatabaseManagerAction() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.seasar.h2.action.AbstractProjectAction#run(org.eclipse.core.resources.IProject)
     */
    public void run(IProject project) throws CoreException {
        try {
            H2Preferences pref = DbLauncherPlugin.getPreferences(project);
            if (pref != null) {
                IWorkbenchBrowserSupport support = PlatformUI.getWorkbench()
                        .getBrowserSupport();
                IWebBrowser browser = support.getExternalBrowser();
                browser.openURL(new URL(buildManagerUrl(pref)));
            }
        } catch (Exception e) {
            DbLauncherPlugin.log(e);
        }
    }

    private String buildManagerUrl(H2Preferences pref) {
        StringBuffer stb = new StringBuffer();
        stb.append("http://localhost:");
        stb.append(pref.getWebPortNo());
        return stb.toString();
    }
}
