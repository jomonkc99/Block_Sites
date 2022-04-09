package com.joe.coinedone.blocksites.activities

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.text.Html
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import com.joe.coinedone.blocksites.R
import com.joe.coinedone.blocksites.bottomsheet.ScheduleBottomSheet
import com.joe.coinedone.blocksites.interfaces.ScheduleChangeListener
import com.joe.coinedone.blocksites.utils.AppPreferences
import com.joe.coinedone.blocksites.utils.Constants

class ScheduleModeActivity : AppCompatActivity(), ScheduleChangeListener {

    lateinit var txtWhiteList: TextView
    lateinit var txtBlackList: TextView
    lateinit var txtDescription: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule_mode)

        initViews()
    }

    fun initViews() {

        findViewById<TextView>(R.id.txtTimeFrom).text = AppPreferences.getValueFromSharedPreference(this, Constants.TIME_FROM)
        findViewById<TextView>(R.id.txtTimeTo).text = AppPreferences.getValueFromSharedPreference(this, Constants.TIME_TO)

        val switchCompat = findViewById<SwitchCompat>(R.id.switchMode)
        txtWhiteList = findViewById<TextView>(R.id.txtWhiteList)
        txtBlackList = findViewById<TextView>(R.id.txtBlackList)
        txtDescription = findViewById<TextView>(R.id.txtDescription)

        switchCompat.setOnCheckedChangeListener { buttonView, isChecked ->

            setBlackList(isChecked)
            AppPreferences.setBlackListMode(this, isChecked)
            if (!isAccessibilitySettingsON()) {
                Toast.makeText(this, "Please turn ON accessibility settings for blocking websites.", Toast.LENGTH_SHORT).show()
            }
        }
        switchCompat.isChecked = AppPreferences.getBlackListMode(this)
        setBlackList(AppPreferences.getBlackListMode(this))

        findViewById<TextView>(R.id.txtDelete).setOnClickListener {
            AppPreferences.setValueInSharedPreference(this, Constants.TIME_FROM, "")
            AppPreferences.setValueInSharedPreference(this, Constants.TIME_TO, "")
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        findViewById<TextView>(R.id.txtEdit).setOnClickListener {
            val bottomSheet = ScheduleBottomSheet()
            bottomSheet.setListener(this, true)
            bottomSheet.apply {
                show(supportFragmentManager, ScheduleBottomSheet.TAG)
            }
        }
    }

    fun setBlackList(isBlackList: Boolean) {
        if (isBlackList) {
            txtWhiteList.setTextColor(getColor(R.color.grey))
            txtBlackList.setTextColor(getColor(R.color.blueDark))
            txtDescription.text = Html.fromHtml(getString(R.string.desc_blacklist))
        }
        else {
            txtWhiteList.setTextColor(getColor(R.color.blueDark))
            txtBlackList.setTextColor(getColor(R.color.grey))
            txtDescription.text = Html.fromHtml(getString(R.string.desc_whitelist))
        }
    }

    override fun onScheduleChanged() {
        findViewById<TextView>(R.id.txtTimeFrom).text = AppPreferences.getValueFromSharedPreference(this, Constants.TIME_FROM)
        findViewById<TextView>(R.id.txtTimeTo).text = AppPreferences.getValueFromSharedPreference(this, Constants.TIME_TO)

        if (!isAccessibilitySettingsON()) {
            Toast.makeText(this, "Please turn ON accessibility settings for blocking websites.", Toast.LENGTH_LONG).show()
        }
    }

    private fun isAccessibilitySettingsON(): Boolean {
        try {
            return (Settings.Secure.getInt(this.contentResolver, Settings.Secure.ACCESSIBILITY_ENABLED) != 0)
        }catch (e : Settings.SettingNotFoundException){}
        return false
    }
}