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
package org.seasar.dblauncher.decorator;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.debug.core.model.ITerminate;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.viewers.ILightweightLabelDecorator;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.LabelProviderChangedEvent;
import org.eclipse.ui.IDecoratorManager;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.seasar.dblauncher.Constants;
import org.seasar.dblauncher.DbLauncherPlugin;
import org.seasar.dblauncher.nls.Images;
import org.seasar.dblauncher.preferences.H2Preferences;

/**
 * @author taichi
 * 
 */
public class DBRunningDecorator extends LabelProvider implements
		ILightweightLabelDecorator {

	public static void updateDecorators(IProject project) {
		if (project == null) {
			return;
		}
		IWorkbench workbench = PlatformUI.getWorkbench();
		IDecoratorManager manager = workbench.getDecoratorManager();
		final DBRunningDecorator decorator = (DBRunningDecorator) manager
				.getBaseLabelProvider(Constants.ID_DECORATOR);
		final LabelProviderChangedEvent event = new LabelProviderChangedEvent(
				decorator, project);
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
			public void run() {
				decorator.fireLabelProviderChanged(event);
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ILightweightLabelDecorator#decorate(java.lang.Object,
	 *      org.eclipse.jface.viewers.IDecoration)
	 */
	public void decorate(Object element, IDecoration decoration) {
		try {
			if (element instanceof IAdaptable) {
				IAdaptable adaptable = (IAdaptable) element;
				IProject project = (IProject) adaptable
						.getAdapter(IProject.class);
				Object o = project
						.getSessionProperty(Constants.KEY_SERVER_STATE);
				H2Preferences pref = DbLauncherPlugin.getPreferences(project);
				if (o instanceof ITerminate) {
					ITerminate t = (ITerminate) o;
					if (t.isTerminated()) {
						project.setSessionProperty(Constants.KEY_SERVER_STATE,
								null);
					} else {
						decoration
								.addSuffix(" [H2:" + pref.getDbPortNo() + "]");
						decoration.addOverlay(Images.RUNNING,
								IDecoration.BOTTOM_RIGHT);
					}
				}
			}
		} catch (CoreException e) {
		}

	}
}
