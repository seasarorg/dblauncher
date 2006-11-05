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
package org.seasar.dblauncher;

import org.eclipse.core.resources.IProject;
import org.eclipse.debug.core.DebugEvent;
import org.eclipse.debug.core.IDebugEventSetListener;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.model.IProcess;
import org.seasar.dblauncher.decorator.DBRunningDecorator;
import org.seasar.eclipse.common.util.LaunchUtil;
import org.seasar.framework.util.StringUtil;

/**
 * @author taichi
 * 
 */
public class TerminateListener implements IDebugEventSetListener {

    /**
     * 
     */
    public TerminateListener() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.debug.core.IDebugEventSetListener#handleDebugEvents(org.eclipse.debug.core.DebugEvent[])
     */
    public void handleDebugEvents(DebugEvent[] events) {
        for (int i = 0; i < events.length; i++) {
            DebugEvent event = events[i];
            if (event.getKind() == DebugEvent.TERMINATE) {
                Object o = event.getSource();
                if (o instanceof IProcess) {
                    ILaunch l = ((IProcess) o).getLaunch();
                    if(StringUtil.isEmpty(l.getAttribute(Constants.KEY_H2_LAUNCH)) == false) {
                        IProject p = LaunchUtil.getProject(l);
                        DbLauncherPlugin.setLaunch(p, null);
                        DBRunningDecorator.updateDecorators(p);
                    }
                }
            }
        }
    }

}
