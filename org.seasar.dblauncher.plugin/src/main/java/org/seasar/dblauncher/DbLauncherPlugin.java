package org.seasar.dblauncher;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.osgi.framework.BundleContext;
import org.seasar.dblauncher.preferences.H2Preferences;
import org.seasar.dblauncher.preferences.impl.H2PreferencesImpl;
import org.seasar.eclipse.common.util.LogUtil;

/**
 * The main plugin class to be used in the desktop.
 */
public class DbLauncherPlugin extends Plugin {

    // The shared instance.
    private static DbLauncherPlugin plugin;

    /**
     * The constructor.
     */
    public DbLauncherPlugin() {
        plugin = this;
    }

    /**
     * This method is called upon plug-in activation
     */
    public void start(BundleContext context) throws Exception {
        super.start(context);
        DebugPlugin debug = DebugPlugin.getDefault();
        debug.addDebugEventListener(new TerminateListener());
    }

    /**
     * This method is called when the plug-in is stopped
     */
    public void stop(BundleContext context) throws Exception {
        super.stop(context);
        plugin = null;
    }

    /**
     * Returns the shared instance.
     */
    public static DbLauncherPlugin getDefault() {
        return plugin;
    }

    public static H2Preferences getPreferences(IProject project) {
        return new H2PreferencesImpl(project);
    }

    public static void setLaunch(IProject project, ILaunch launch) {
        try {
            if (project != null) {
                project.setSessionProperty(Constants.KEY_SERVER_STATE, launch);
            }
        } catch (CoreException e) {
            log(e);
        }
    }

    public static ILaunch getLaunch(IProject project) {
        ILaunch result = null;
        try {
            if (project != null) {
                result = (ILaunch) project
                        .getSessionProperty(Constants.KEY_SERVER_STATE);
            }
        } catch (CoreException e) {
            log(e);
        }
        return result;
    }

    public static void log(String msg) {
        LogUtil.log(getDefault(), msg);
    }

    public static void log(Throwable throwable) {
        LogUtil.log(getDefault(), throwable);
    }

}
