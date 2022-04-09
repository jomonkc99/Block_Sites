package com.joe.coinedone.blocksites.utils

import android.content.Context
import android.content.SharedPreferences

object AppPreferences {

    val preferenceName: String = "app_pref"

    fun getValueFromSharedPreference(context: Context, key: String): String {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE)
        return sharedPreferences.getString(key, "").toString()
    }

    fun setValueInSharedPreference(context: Context, key: String, value: String): Boolean {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
        return editor.commit()
    }

    fun getBlackListMode(context: Context): Boolean {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("mode", false)
    }

    fun setBlackListMode(context: Context, value: Boolean): Boolean {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putBoolean("mode", value)
        editor.apply()
        return editor.commit()
    }

    fun getBooleanValueFromSharedPreference(context: Context, key: String): Boolean {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(key, false)
    }

    fun setBooleanValueInSharedPreference(context: Context, key: String, value: Boolean): Boolean {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putBoolean(key, value)
        editor.apply()
        return editor.commit()
    }
}