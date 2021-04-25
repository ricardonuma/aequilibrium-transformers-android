package com.aequilibrium.transformers.auth

import android.content.SharedPreferences
import android.util.Log
import com.aequilibrium.transformers.utils.PREF_TOKEN
import org.json.JSONException
import javax.inject.Inject

class SessionManager @Inject constructor(
    private val appPreferences: SharedPreferences
) {

    fun getToken(): String {
        try {
            return appPreferences.getString(PREF_TOKEN, "").toString()
        } catch (ex: JSONException) {
            Log.e(TAG, ex.toString())
        }
        return ""
    }

    fun storeToken(token: String) {
        Log.d(TAG, "Persisting token")
        appPreferences.edit().putString(PREF_TOKEN, token).apply()
    }

    companion object {
        private val TAG = SessionManager::class.java.simpleName
    }

}
