package com.example.conexionapi.utils

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import java.io.IOException
import java.security.GeneralSecurityException

object SecurePreferences {
    private const val PREFS_NAME = "secure_prefs"
    private const val KEY_TOKEN = "auth_token"

    private fun getSharedPreferences(context: Context): EncryptedSharedPreferences {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM) // Usa solo esto
            .build()

        return EncryptedSharedPreferences.create(
            context,
            PREFS_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        ) as EncryptedSharedPreferences
    }

    fun saveToken(context: Context, token: String) {
        try {
            getSharedPreferences(context).edit()
                .putString(KEY_TOKEN, token)
                .apply()
        } catch (e: GeneralSecurityException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun getToken(context: Context): String? {
        return try {
            getSharedPreferences(context).getString(KEY_TOKEN, null)
        } catch (e: GeneralSecurityException) {
            e.printStackTrace()
            null
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    fun clearToken(context: Context) {
        try {
            getSharedPreferences(context).edit()
                .remove(KEY_TOKEN)
                .apply()
        } catch (e: GeneralSecurityException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}