package porky.training.zypko.global

import android.content.Context
import android.content.SharedPreferences

class PreferenceEditor(private val context: Context) {
    val FILE_NAME = "MySharedPref"
    lateinit var preferences: SharedPreferences
    private var editor: SharedPreferences.Editor? = null

    fun init(): PreferenceEditor {
        //initialize the preference
        preferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)
        //initialize preferenceEditor
        editor = preferences.edit()
        return this
    }

    //executes whenever want to put a String value
    fun put(key: String?, value: String?): PreferenceEditor {
        editor!!.putString(key, value)
        editor!!.apply()
        return this
    }

    //executes whenever want to put a integer value
    fun put(key: String?, value: Int): PreferenceEditor {
        editor!!.putInt(key, value)
        editor!!.apply()
        return this
    }

    //executes whenever want to get a String value
    operator fun get(key: String?, defaultValue: String?): String? {
        return preferences.getString(key, defaultValue)
    }

    //executes whenever want to get a integer value
    operator fun get(key: String?, defaultValue: Int): Int {
        return preferences.getInt(key, defaultValue)
    }


}