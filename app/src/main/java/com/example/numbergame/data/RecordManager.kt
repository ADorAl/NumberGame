package com.example.numbergame.data

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("game_records")

object RecordManager {

    // gameType + difficulty로 key 생성
    private fun keyForDifficulty(gameType: String, difficulty: Int) =
        intPreferencesKey("${gameType}_$difficulty")

    // 기록 조회
    fun getRecord(context: Context, gameType: String, difficulty: Int): Flow<Int?> {
        val key = keyForDifficulty(gameType, difficulty)
        return context.dataStore.data.map { prefs ->
            prefs[key]
        }
    }

    // 기록 저장
    suspend fun saveRecord(
        context: Context,
        gameType: String,  // "number" or "card"
        difficulty: Int,
        newTime: Int
    ) {
        val key = keyForDifficulty(gameType, difficulty)
        context.dataStore.edit { prefs ->
            val currentBest = prefs[key]
            if (currentBest == null || newTime < currentBest) {
                prefs[key] = newTime
            }
        }
    }

    // 전체 기록 초기화
    suspend fun resetAll(context: Context) {
        context.dataStore.edit { it.clear() }
    }
}