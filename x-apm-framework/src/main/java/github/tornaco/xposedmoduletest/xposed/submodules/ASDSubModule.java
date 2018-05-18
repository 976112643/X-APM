package github.tornaco.xposedmoduletest.xposed.submodules;

import android.content.Intent;
import android.util.Log;

import java.util.Set;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import github.tornaco.xposedmoduletest.xposed.util.XposedLog;

/**
 * ActivityStack move to back.
 */

class ASDSubModule extends AndroidSubModule {

    @Override
    public void handleLoadingPackage(String pkg, XC_LoadPackage.LoadPackageParam lpparam) {
        hookActivityStack(lpparam);
    }

    private void hookActivityStack(XC_LoadPackage.LoadPackageParam lpparam) {
        XposedLog.verbose("ASDSubModule hookActivityStack...");
        try {
            Class stackClass = XposedHelpers.findClass("com.android.server.am.ActivityStack",
                    lpparam.classLoader);

            @SuppressWarnings("unchecked")
            Set unHooks = XposedBridge.hookAllMethods(stackClass,
                    "destroyActivityLocked", new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            super.afterHookedMethod(param);
                            Object ar = param.args[0];
                            Intent intent = (Intent) XposedHelpers.getObjectField(ar, "intent");
                            if (intent == null) return;
                            getBridge().onActivityDestroy(intent, "destroyActivityLocked");
                        }
                    });
            XposedLog.verbose("ASDSubModule hookActivityStack OK:" + unHooks);
            setStatus(unhooksToStatus(unHooks));
        } catch (Exception e) {
            XposedLog.verbose("ASDSubModule Fail hook hookActivityStack" + Log.getStackTraceString(e));
            setStatus(SubModuleStatus.ERROR);
            setErrorMessage(Log.getStackTraceString(e));
        }
    }
}
