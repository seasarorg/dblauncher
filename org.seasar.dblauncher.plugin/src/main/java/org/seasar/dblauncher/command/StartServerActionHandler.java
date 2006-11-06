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
package org.seasar.dblauncher.command;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.seasar.dblauncher.action.StartServerAction;

/**
 * @author taichi
 * 
 */
public class StartServerActionHandler extends AbstractHandler implements
        IHandler {

    private StartServerAction action = new StartServerAction();

    /**
     * 
     */
    public StartServerActionHandler() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.ExecutionEvent)
     */
    public Object execute(ExecutionEvent event) throws ExecutionException {
        try {
            Object o = event.getApplicationContext();
            if (o instanceof IAdaptable) {
                IAdaptable a = (IAdaptable) o;
                IProject project = (IProject) a.getAdapter(IProject.class);
                if (project != null) {
                    action.run(project);
                }
            }
        } catch (CoreException e) {
            throw new ExecutionException("", e);
        }
        return null;
    }

}