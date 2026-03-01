package com.example.numbergame.data

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("card_records")

object RecordManager {

    private fun keyForDifficulty(difficulty: Int) =
        intPreferencesKey("best_time_$difficulty")

    fun getRecord(context: Context, difficulty: Int): Flow<Int?> {
        return context.dataStore.data.map { prefs ->
            prefs[keyForDifficulty(difficulty)]
        }
    }

    suspend fun saveRecord(
        context: Context,
        difficulty: Int,
        newTime: Int
    ) {
        context.dataStore.edit { prefs ->
            val key = keyForDifficulty(difficulty)
            val currentBest = prefs[key]

            if (currentBest == null || newTime < currentBest) {
                prefs[key] = newTime
            }
        }
    }

    suspend fun resetAll(context: Context) {
        context.dataStore.edit { it.clear() }
    }
}