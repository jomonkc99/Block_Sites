package com.joe.coinedone.blocksites.service

import android.accessibilityservice.AccessibilityService
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.util.Patterns
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.joe.coinedone.blocksites.utils.AppPreferences
import com.joe.coinedone.blocksites.utils.Constants
import com.joe.coinedone.blocksites.utils.Utils
import java.time.LocalTime
import java.util.*


class BlockingService : AccessibilityService() {

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {

        val parentNodeInfo = event!!.source ?: return
        val packageName = event!!.packageName.toString()

        if ("com.android.chrome".equals(packageName)) {
            val capturedUrl: String? = captureUrl(parentNodeInfo)
            if (capturedUrl != null) {
                val blacklistMode = AppPreferences.getBlackListMode(applicationContext)
                if (isScheduledTimeNow()) {

                    if (isListedUrl(capturedUrl)) {
                        if (blacklistMode)
                            launchAccessDenied(capturedUrl)
                    }
                    else {
                        if (!blacklistMode)
                            launchAccessDenied(capturedUrl)
                    }
                }

            }
        }
    }

    private fun isListedUrl(capturedUrl: String): Boolean  {
        var isContains = false
        Constants.websites.forEach {
            if (capturedUrl.contains(it)) {
                isContains = true
                return@forEach
            }
        }
        return isContains
    }

    private fun launchAccessDenied(currentURL: String) {

        if (currentURL.contains("ipi.media/wp-content/uploads") || currentURL.contains("Search or type")) return

        if (!Patterns.WEB_URL.matcher(currentURL).matches()) return

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("googlechrome://navigate?url=${Constants.accessDeniedUrl}"))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.setPackage("com.android.chrome")
        try {
            applicationContext.startActivity(intent)
        } catch (ex: ActivityNotFoundException) {
        }
    }

    override fun onInterrupt() {}

    private fun captureUrl(info: AccessibilityNodeInfo): String? {
        val nodes = info.findAccessibilityNodeInfosByViewId("com.android.chrome:id/url_bar")
        if (nodes == null || nodes.size <= 0) {
            return null
        }
        val addressBarNodeInfo = nodes[0]
        var url: String? = null
        if (addressBarNodeInfo.text != null) {
            url = addressBarNodeInfo.text.toString()
        }
        addressBarNodeInfo.recycle()
        return url
    }

    private fun isScheduledTimeNow() : Boolean {

        val currentTime = LocalTime.now()
        val scheduleStart = Utils.getLocalTime(AppPreferences.getValueFromSharedPreference(applicationContext, Constants.TIME_FROM))
        val scheduleEnd = Utils.getLocalTime(AppPreferences.getValueFromSharedPreference(applicationContext, Constants.TIME_TO))

        return (currentTime.isAfter(scheduleStart) && currentTime.isBefore(scheduleEnd))
    }
}