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
package org.seasar.dblauncher.preferences;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPropertyPage;
import org.eclipse.ui.dialogs.PropertyPage;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.h2.server.TcpServer;
import org.seasar.dblauncher.Constants;
import org.seasar.dblauncher.DbLauncherPlugin;
import org.seasar.dblauncher.nls.Messages;
import org.seasar.dblauncher.preferences.impl.H2PreferencesImpl;
import org.seasar.eclipse.common.util.ProjectUtil;
import org.seasar.eclipse.common.wiget.ResourceTreeSelectionDialog;
import org.seasar.framework.util.StringUtil;

/**
 * @author taichi
 * 
 */
public class H2PreferencesPage extends PropertyPage implements
        IWorkbenchPropertyPage {

    private Pattern numeric = Pattern.compile("\\d*");

    private Button useH2;

    private Button isDebug;

    private Text baseDir;

    private Text dbPortNo;

    private Text webPortNo;

    private Text user;

    private Text password;

    /**
     * 
     */
    public H2PreferencesPage() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.widgets.Composite)
     */
    protected Control createContents(Composite parent) {
        Composite composite = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        composite.setLayout(layout);
        GridData data = new GridData(GridData.FILL);
        data.grabExcessHorizontalSpace = true;
        composite.setLayoutData(data);

        this.useH2 = new Button(createDefaultComposite(composite), SWT.CHECK);
        this.useH2.setText(Messages.LABEL_USE_H2_PLUGIN);

        this.isDebug = new Button(createDefaultComposite(composite), SWT.CHECK);
        this.isDebug.setText(Messages.LABEL_IS_DEBUG);
        NumberVerifier nv = new NumberVerifier();
        this.dbPortNo = createPart(composite, Messages.LABEL_DB_PORTNO);
        this.dbPortNo.addModifyListener(nv);
        this.webPortNo = createPart(composite, Messages.LABEL_WEB_PORTNO);
        this.webPortNo.addModifyListener(nv);
        this.user = createPart(composite, Messages.LABEL_USER);
        this.password = createPart(composite, Messages.LABEL_PASSWORD,
                SWT.BORDER | SWT.PASSWORD);
        this.baseDir = createPart(composite, Messages.LABEL_BASE_DIR);
        new Label(composite, SWT.NONE);
        Button outpath = new Button(composite, SWT.PUSH);
        outpath.setText("Browse ...");
        outpath.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                ResourceTreeSelectionDialog dialog = new ResourceTreeSelectionDialog(
                        getShell(), getProject().getParent(), IResource.FOLDER
                                | IResource.PROJECT);
                dialog.setInitialSelection(getProject());
                dialog.setAllowMultiple(false);
                if (dialog.open() == Dialog.OK) {
                    Object[] results = dialog.getResult();
                    if (results != null && 0 < results.length) {
                        IResource r = (IResource) results[0];
                        baseDir.setText(r.getFullPath().toString());
                    }
                }
            }
        });

        setUpStoredValue();
        setUpPortNos();
        return composite;
    }

    private class NumberVerifier implements ModifyListener {
        public void modifyText(ModifyEvent e) {
            if (e.widget instanceof Text) {
                Text t = (Text) e.widget;
                boolean is = false;
                if (is = numeric.matcher(t.getText()).matches()) {
                    setErrorMessage(null);
                } else {
                    setErrorMessage(NLS.bind(Messages.ERR_ONLY_NUMERIC,
                            "Port No"));
                }
                setValid(is);
            }
        }
    }

    /**
     * 
     */
    private void setUpStoredValue() {
        IPreferenceStore store = getPreferenceStore();
        this.useH2.setSelection(ProjectUtil.hasNature(getProject(),
                Constants.ID_NATURE));
        this.isDebug.setSelection(store.getBoolean(Constants.PREF_IS_DEBUG));
        this.baseDir.setText(store.getString(Constants.PREF_BASE_DIR));
        this.dbPortNo.setText(store.getString(Constants.PREF_DB_PORTNO));
        this.webPortNo.setText(store.getString(Constants.PREF_WEB_PORTNO));
        this.user.setText(store.getString(Constants.PREF_USER));
        this.password.setText(store.getString(Constants.PREF_PASSWORD));
    }

    private void setUpPortNos() {
        IProject project = getProject();
        if (ProjectUtil.hasNature(project, Constants.ID_NATURE) == false) {
            IWorkspaceRoot root = ProjectUtil.getWorkspaceRoot();
            IProject[] all = root.getProjects();
            Set nos = new HashSet();
            for (int i = 0; i < all.length; i++) {
                if (ProjectUtil.hasNature(all[i], Constants.ID_NATURE)) {
                    IPreferenceStore other = new ScopedPreferenceStore(
                            new ProjectScope(all[i]), Constants.ID_PLUGIN);
                    nos.add(other.getString(Constants.PREF_DB_PORTNO));
                    nos.add(other.getString(Constants.PREF_WEB_PORTNO));
                }
            }
            int dbPort = TcpServer.DEFAULT_PORT - 1;
            while (nos.contains(String.valueOf(++dbPort))) {
            }
            nos.add(String.valueOf(dbPort));
            int webPort = org.h2.engine.Constants.DEFAULT_HTTP_PORT - 1;
            while (nos.contains(String.valueOf(++webPort))) {
            }
            this.dbPortNo.setText(String.valueOf(dbPort));
            this.webPortNo.setText(String.valueOf(webPort));
        }
    }

    /**
     * @param composite
     * @param label
     */
    private Text createPart(Composite composite, String label) {
        return createPart(composite, label, SWT.SINGLE | SWT.BORDER);
    }

    private Text createPart(Composite composite, String label, int style) {
        Label l = new Label(composite, SWT.NONE);
        l.setText(label);
        Text txt = new Text(composite, style);
        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        txt.setLayoutData(data);
        return txt;

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.PreferencePage#doGetPreferenceStore()
     */
    protected IPreferenceStore doGetPreferenceStore() {
        IProject project = getProject();
        ScopedPreferenceStore store = null;
        if (project != null) {
            store = new ScopedPreferenceStore(new ProjectScope(project),
                    Constants.ID_PLUGIN);
            H2PreferencesImpl.setupPreferences(project, store);
            setPreferenceStore(store);
        }
        return store;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.PreferencePage#performDefaults()
     */
    protected void performDefaults() {
        IPreferenceStore store = getPreferenceStore();
        this.useH2.setSelection(false);
        this.isDebug.setSelection(store
                .getDefaultBoolean(Constants.PREF_IS_DEBUG));
        this.baseDir.setText(H2PreferencesImpl.getDefaultBaseDir(getProject()));
        this.dbPortNo.setText(store.getDefaultString(Constants.PREF_DB_PORTNO));
        this.webPortNo.setText(store
                .getDefaultString(Constants.PREF_WEB_PORTNO));
        super.performDefaults();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.preference.PreferencePage#performOk()
     */
    public boolean performOk() {
        boolean result = false;
        IProject project = getProject();
        try {
            if (project != null) {
                IPreferenceStore store = getPreferenceStore();
                if (this.useH2.getSelection()) {
                    ProjectUtil.addNature(project, Constants.ID_NATURE);
                } else {
                    ProjectUtil.removeNature(project, Constants.ID_NATURE);
                }

                store.setValue(Constants.PREF_IS_DEBUG, this.isDebug
                        .getSelection());
                String dir = this.baseDir.getText();
                if (StringUtil.isEmpty(dir) == false) {
                    store.setValue(Constants.PREF_BASE_DIR, dir);
                }
                String no = this.dbPortNo.getText();
                if (StringUtil.isEmpty(no) == false
                        && numeric.matcher(no).matches()) {
                    store.setValue(Constants.PREF_DB_PORTNO, no);
                }
                no = this.webPortNo.getText();
                if (StringUtil.isEmpty(no) == false
                        && numeric.matcher(no).matches()) {
                    store.setValue(Constants.PREF_WEB_PORTNO, no);
                }
                result = true;
            }
        } catch (CoreException e) {
            DbLauncherPlugin.log(e);
        }

        return result;
    }

    private IProject getProject() {
        IProject result = null;
        IAdaptable adaptable = getElement();
        if (adaptable != null) {
            result = (IProject) adaptable.getAdapter(IProject.class);
        }
        return result;
    }

    private Composite createDefaultComposite(Composite parent) {
        Composite composite = new Composite(parent, SWT.NULL);
        GridLayout layout = new GridLayout();
        layout.numColumns = 1;
        composite.setLayout(layout);

        GridData data = new GridData();
        data.verticalAlignment = GridData.FILL;
        data.horizontalAlignment = GridData.FILL;
        data.horizontalSpan = 2;
        composite.setLayoutData(data);

        return composite;
    }
}
