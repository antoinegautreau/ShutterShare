package ca.unb.mobiledev.shuttershare.util

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity

class SharedPreferenceUtils() {

    fun getSharedPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(NAME, AppCompatActivity.MODE_PRIVATE)
    }

    companion object {
        private const val NAME = "ShutterShareData"
    }
}