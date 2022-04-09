package com.joe.coinedone.blocksites.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.joe.coinedone.blocksites.R
import com.joe.coinedone.blocksites.bottomsheet.ScheduleBottomSheet
import com.joe.coinedone.blocksites.utils.AppPreferences
import com.joe.coinedone.blocksites.utils.Constants

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<ImageView>(R.id.imgAddTime).setOnClickListener { openScheduleBottomSheet() }
    }

    override fun onResume() {
        super.onResume()

        if (!isAccessServiceEnabled(this))
            setAccessibilityPermission()
        else
            checkScheduled()
    }

    private fun checkScheduled() {
        if (AppPreferences.getValueFromSharedPreference(this, Constants.TIME_TO).isNotEmpty()
            && AppPreferences.getValueFromSharedPreference(this, Constants.TIME_FROM).isNotEmpty()) {
            startActivity(Intent(this, ScheduleModeActivity::class.java))
            finish()
        }
    }

    private fun openScheduleBottomSheet() {
        if (!isAccessServiceEnabled(this)) {
            Toast.makeText(this, "Please turn ON accessibility settings before scheduling.", Toast.LENGTH_LONG).show()
        }
        ScheduleBottomSheet().apply {
            show(supportFragmentManager, ScheduleBottomSheet.TAG)
        }
    }

    private fun setAccessibilityPermission() {
        Toast.makeText(this, "Find Block Sites app in the list and enable permission for using the app", Toast.LENGTH_LONG).show()
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    fun isAccessServiceEnabled(context: Context): Boolean {
        val prefString =
            Settings.Secure.getString(context.contentResolver, Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES)
        if (prefString==null) {
            return (Settings.Secure.getInt(this.contentResolver, Settings.Secure.ACCESSIBILITY_ENABLED) != 0)
        }
        else {
            return prefString.contains("${context.packageName}/${context.packageName}.service.BlockingService")
        }

    }
}