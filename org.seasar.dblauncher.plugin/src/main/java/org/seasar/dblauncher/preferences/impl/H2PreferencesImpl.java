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
package org.seasar.dblauncher.preferences.impl;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.seasar.dblauncher.Constants;
import org.seasar.dblauncher.DbLauncherPlugin;
import org.seasar.dblauncher.preferences.H2Preferences;
import org.seasar.framework.util.StringUtil;

/**
 * @author taichi
 * 
 */
public class H2PreferencesImpl implements H2Preferences {

    private ScopedPreferenceStore store;

    /**
     * 
     */
    public H2PreferencesImpl(IProject project) {
        super();
        store = new ScopedPreferenceStore(new ProjectScope(project),
                Constants.ID_PLUGIN);
        setupPreferences(project, store);
    }

    public static void setupPreferences(IProject project, IPreferenceStore store) {
        String baseDir = store.getString(Constants.PREF_BASE_DIR);
        if (StringUtil.isEmpty(baseDir)) {
            store.setValue(Constants.PREF_BASE_DIR, getDefaultBaseDir(project));
        }
    }

    public static String getDefaultBaseDir(IProject project) {
        String result = "";
        try {
            if (project != null) {
                IJavaProject jp = JavaCore.create(project);
                IPath p = jp.getOutputLocation();
                IContainer c = project.getParent().getFolder(p);
                IPath data = c.getFullPath().append("data").append("demo");
                result = data.toString();
            }
        } catch (JavaModelException e) {
            DbLauncherPlugin.log(e);
        }

        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.seasar.h2.preferences.H2Preferences#getBaseDir()
     */
    public String getBaseDir() {
        return store.getString(Constants.PREF_BASE_DIR);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.seasar.h2.preferences.H2Preferences#setBaseDir(java.lang.String)
     */
    public void setBaseDir(String path) {
        this.store.setValue(Constants.PREF_BASE_DIR, path);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.seasar.h2.preferences.H2Preferences#getDbPortNo()
     */
    public String getDbPortNo() {
        return this.store.getString(Constants.PREF_DB_PORTNO);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.seasar.h2.preferences.H2Preferences#setDbPortNo(java.lang.String)
     */
    public void setDbPortNo(String no) {
        this.store.setValue(Constants.PREF_DB_PORTNO, no);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.seasar.h2.preferences.H2Preferences#getWebPortNo()
     */
    public String getWebPortNo() {
        return this.store.getString(Constants.PREF_WEB_PORTNO);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.seasar.h2.preferences.H2Preferences#setWebPortNo(java.lang.String)
     */
    public void setWebPortNo(String no) {
        this.store.setValue(Constants.PREF_WEB_PORTNO, no);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.seasar.h2.preferences.H2Preferences#isDebug()
     */
    public boolean isDebug() {
        return this.store.getBoolean(Constants.PREF_IS_DEBUG);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.seasar.h2.preferences.H2Preferences#setDebug(boolean)
     */
    public void setDebug(boolean is) {
        this.store.setValue(Constants.PREF_IS_DEBUG, is);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.seasar.h2.preferences.H2Preferences#getPassword()
     */
    public String getPassword() {
        return this.store.getString(Constants.PREF_PASSWORD);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.seasar.h2.preferences.H2Preferences#getUser()
     */
    public String getUser() {
        return this.store.getString(Constants.PREF_USER);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.seasar.h2.preferences.H2Preferences#setPassword(java.lang.String)
     */
    public void setPassword(String pass) {
        this.store.setValue(Constants.PREF_PASSWORD, pass);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.seasar.h2.preferences.H2Preferences#setUser(java.lang.String)
     */
    public void setUser(String user) {
        this.store.setValue(Constants.PREF_USER, user);
    }

}
