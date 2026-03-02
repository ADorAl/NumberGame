package com.example.numbergame.data

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("game_records")

object RecordManager {

    // gameType + optional operation + difficulty로 key 생성
    private fun keyForDifficulty(gameType: String, operation: String? = null, difficulty: Int) =
        if (operation != null)
            intPreferencesKey("${gameType}_${operation}_$difficulty")
        else
            intPreferencesKey("${gameType}_$difficulty")

    // 기록 조회
    fun getRecord(context: Context, gameType: String, difficulty: Int, operation: String? = null): Flow<Int?> {
        val key = keyForDifficulty(gameType, operation, difficulty)
        return context.dataStore.data.map { prefs ->
            prefs[key]
        }
    }

    // 기록 저장
    suspend fun saveRecord(
        context: Context,
        gameType: String,  // "number", "card", "four_basic"
        difficulty: Int,
        newTime: Int,
        operation: String? = null // 사칙연산일 경우 연산 기호
    ) {
        val key = keyForDifficulty(gameType, operation, difficulty)
        context.dataStore.edit { prefs ->
            val currentBest = prefs[key]
            if (currentBest == null || newTime > currentBest) {
                prefs[key] = newTime
            }
        }
    }

    // 전체 기록 초기화
    suspend fun resetAll(context: Context) {
        context.dataStore.edit { it.clear() }
    }
}