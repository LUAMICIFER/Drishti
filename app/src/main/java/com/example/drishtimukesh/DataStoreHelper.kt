package com.example.drishtimukesh

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

// Extension to create DataStore
val Context.dataStore by preferencesDataStore(name = "settings")

private val ONBOARDING_KEY = booleanPreferencesKey("onboarding_completed")

suspend fun saveOnboardingCompleted(context: Context) {
    context.dataStore.edit { prefs ->
        prefs[ONBOARDING_KEY] = true
    }
}

suspend fun readOnboardingCompleted(context: Context): Boolean {
    val prefs = context.dataStore.data.first()
    return prefs[ONBOARDING_KEY] ?: false
}
