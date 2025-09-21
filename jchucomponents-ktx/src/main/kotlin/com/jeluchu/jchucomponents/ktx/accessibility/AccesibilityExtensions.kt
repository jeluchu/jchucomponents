package com.jeluchu.jchucomponents.ktx.accessibility

import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Context
import android.provider.Settings
import android.view.accessibility.AccessibilityManager
import androidx.core.content.getSystemService
import com.jeluchu.jchucomponents.ktx.constants.PackageConstants

inline val AccessibilityManager?.isTalkBackEnabled: Boolean
    get() = this != null && isEnabled && isTouchExplorationEnabled

inline val Context.isTalkBackEnabled: Boolean
    get() = getSystemService<AccessibilityManager>().isTalkBackEnabled

fun Context.isSwitchAccessEnabled(): Boolean {
    (getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager).apply {
        val accessibilityList = getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK)
            .firstOrNull { it.resolveInfo.serviceInfo.name.equals(PackageConstants.SWITCH_ACCESS_SERVICE) }

        if (accessibilityList != null)  return true
    }
    return false
}

val Context.isAccessibilityEnabled: Boolean
    get() = isTalkBackEnabled || isSwitchAccessEnabled()

fun Context.isReduceMotionEnabled(): Boolean {
    val animationDuration = try {
        Settings.Global.getFloat(
            contentResolver,
            Settings.Global.ANIMATOR_DURATION_SCALE
        )
    } catch (_: Settings.SettingNotFoundException) {
        1f
    }

    return animationDuration == 0f
}