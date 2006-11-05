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

/**
 * @author taichi
 * 
 */
public interface H2Preferences {

    String getBaseDir();

    void setBaseDir(String path);

    String getDbPortNo();

    void setDbPortNo(String no);

    String getWebPortNo();

    void setWebPortNo(String no);

    void setDebug(boolean is);

    boolean isDebug();

    String getUser();

    void setUser(String user);

    String getPassword();

    void setPassword(String pass);

}
