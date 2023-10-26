package com.hal1cinogen.transparentnavigationbarcompat

import android.annotation.SuppressLint
import android.graphics.Color
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

class XposedEntry : IXposedHookLoadPackage {

    override fun handleLoadPackage(lpp: XC_LoadPackage.LoadPackageParam) {
        XposedHelpers.findAndHookMethod("com.android.internal.policy.PhoneWindow",
            null,
            "generateLayout",
            "com.android.internal.policy.DecorView",
            object : XC_MethodHook() {
                @SuppressLint("PrivateApi")
                override fun beforeHookedMethod(param: MethodHookParam) {
                    try {
                        val navBarDefaultColor = Color.parseColor("#00808080")
/*                        val window = param.thisObject as Window
                        val a = window.windowStyle
                        val color = try {
                            val index = Class.forName("android.R\$styleable").fields.firstOrNull {
                                it.name == "Window_navigationBarColor"
                            }?.get(null) as? Int ?: 0
                            a.getColor(index, navBarDefaultColor)
                        } catch (t: Throwable) {
                            XposedBridge.log(t)
                            navBarDefaultColor
                        }
                        XposedBridge.log("nav color - ${String.format("#%08X", 0xFFFFFFFF and color.toLong())}")*/
                        val field =
                            param.thisObject.javaClass.getDeclaredField("mNavigationBarColor")
                        field.isAccessible = true
                        field.set(param.thisObject, navBarDefaultColor)
                        val flagField =
                            param.thisObject.javaClass.getDeclaredField("mForcedNavigationBarColor")
                        flagField.isAccessible = true
                        flagField.set(param.thisObject, true)
                    } catch (e: Exception) {
                        XposedBridge.log(e)
                    }
                }
            })
    }
}