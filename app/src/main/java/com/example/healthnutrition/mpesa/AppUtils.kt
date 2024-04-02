package com.example.healthnutrition.mpesa

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.androidstudy.daraja.BuildConfig
import java.util.Calendar
import java.util.UUID

object AppUtils {

    fun generateUUID(): String =
        UUID.randomUUID().toString()

    val passKey: String
        get() = ""

    fun saveAccessToken(context: Context, accessToken: String) {
        val cal = Calendar.getInstance()
        cal.add(Calendar.HOUR, 1)
        val oneHourAfter = cal.timeInMillis

        val mSettings = context.getSharedPreferences(BuildConfig.LIBRARY_PACKAGE_NAME, MODE_PRIVATE)
        val editor = mSettings.edit()

        editor.putString("accessToken", accessToken)
        editor.putLong("expiryDate", oneHourAfter)
        editor.apply()
    }

    fun getAccessToken(context: Context): String? {
        return if (expired(context)) {
            null
        }else {
            val mSettings = context.getSharedPreferences(BuildConfig.LIBRARY_PACKAGE_NAME, MODE_PRIVATE)
            mSettings.getString("accessToken", "")
        }
    }

    private fun expired(context: Context): Boolean {
        val mSettings = context.getSharedPreferences(BuildConfig.LIBRARY_PACKAGE_NAME, MODE_PRIVATE)
        val expiryTime = mSettings.getLong("expiryDate", 0)
        val currentTime = Calendar.getInstance().timeInMillis
        return currentTime > expiryTime
    }
}