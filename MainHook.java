package com.alexdesrosier.lockplayer;

import android.view.ViewGroup;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class MainHook implements IXposedHookLoadPackage {

    /** Hide the stock Android media card on the lock screen so only ours shows. */
    static final boolean HIDE_STOCK_PLAYER = true;

    private static final String ROOT_VIEW =
            "com.android.systemui.keyguard.ui.view.KeyguardRootView";
    private static final String KG_MEDIA =
            "com.android.systemui.media.controls.ui.controller.KeyguardMediaController";

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {
        if (!"com.android.systemui".equals(lpparam.packageName)) return;
        final ClassLoader cl = lpparam.classLoader;

        try {
            Class<?> rootCls = XposedHelpers.findClass(ROOT_VIEW, cl);
            XposedBridge.hookAllConstructors(rootCls, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) {
                    LockPlayer.attach((ViewGroup) param.thisObject);
                }
            });
        } catch (Throwable t) {
            XposedBridge.log("LockPlayer: KeyguardRootView hook failed: " + t);
        }

        if (HIDE_STOCK_PLAYER) {
            try {
                Class<?> kmc = XposedHelpers.findClass(KG_MEDIA, cl);
                XposedBridge.hookAllMethods(kmc, "refreshMediaPosition", new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) {
                        try {
                            XposedHelpers.setBooleanField(
                                    param.thisObject, "allowMediaPlayerOnLockScreen", false);
                        } catch (Throwable ignored) { }
                    }
                });
            } catch (Throwable t) {
                XposedBridge.log("LockPlayer: stock player hook failed: " + t);
            }
        }
    }
}
