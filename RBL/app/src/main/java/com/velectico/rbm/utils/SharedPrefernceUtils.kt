package com.velectico.rbm.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.velectico.rbm.network.apiconstants.USER_ID
import com.velectico.rbm.network.apiconstants.USER_ROLE
import java.util.*

object SharedPreferenceUtils {
    fun addSessionDataToSharedPreference(userId:String,context: Context){
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_FILE,
            Context.MODE_PRIVATE)
        val editor:SharedPreferences.Editor =  sharedPreferences.edit()
        editor.putString(USER_ID,userId)
        editor.putString(USER_ROLE,userId)
        editor.apply()
        editor.commit()
    }

    fun getLoggedInUserId(context: Context) : String{
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_FILE,
            Context.MODE_PRIVATE)
        val userId = sharedPreferences.getString(USER_ID,"")
        return userId as String;
    }

    fun saveData(context: Context,key: String, token: String) {
        val mSharedPreferences: SharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_FILE,
            Context.MODE_PRIVATE)
        mSharedPreferences.edit().putString(key, token).apply()
    }


    fun getData(context: Context,key: String): String {
        val mSharedPreferences: SharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_FILE,
            Context.MODE_PRIVATE)
        return  mSharedPreferences.getString(key, "0")!!
    }



}