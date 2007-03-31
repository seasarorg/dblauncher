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

import org.eclipse.core.runtime.QualifiedName;

/**
 * @author taichi
 * 
 */
public class Constants {
    public static final String ID_PLUGIN = "org.seasar.dblauncher";

    public static final String ID_NATURE = ID_PLUGIN + ".nature";

    public static final String ID_DECORATOR = ID_PLUGIN + ".decorator";

    public static final String ID_H2_LAUNCH_CONFIG = ID_PLUGIN
            + ".launchConfigurationType";

    /* ---------------------------------------------------------------------- */
    public static final QualifiedName KEY_SERVER_STATE = new QualifiedName(
            ID_PLUGIN, "serverstate");

    public static final String KEY_H2_LAUNCH = "h2launch";

    /* ---------------------------------------------------------------------- */
    public static final String PREF_BASE_DIR = "baseDir";

    public static final String PREF_DB_PORTNO = "dbPortNo";

    public static final String PREF_WEB_PORTNO = "webPortNo";

    public static final String PREF_IS_DEBUG = "isDebug";

    public static final String PREF_USER = "user";

    public static final String PREF_PASSWORD = "password";
}
