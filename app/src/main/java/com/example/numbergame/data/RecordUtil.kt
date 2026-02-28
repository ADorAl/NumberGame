package com.example.numbergame.data

import android.content.Context

// 🔹 기록 저장 (새 기록 추가 후 Top 10 유지)
fun saveRecord(context: Context, difficulty: Int, elapsed: Double) {
    val prefs = context.getSharedPreferences("records", Context.MODE_PRIVATE)
    val key = "difficulty_$difficulty"

    // 기존 기록 불러오기
    val records = prefs.getStringSet(key, emptySet())!!
        .mapNotNull { it.toDoubleOrNull() }
        .toMutableList()

    // 새 기록 추가
    records.add(elapsed)

    // 오름차순 정렬 후 Top 10만 유지
    records.sort()
    val top10 = records.take(10)

    // 다시 저장
    prefs.edit().putStringSet(key, top10.map { it.toString() }.toSet()).apply()
}

// 🔹 기록 불러오기
fun getRecords(context: Context, difficulty: Int): List<Double> {
    val prefs = context.getSharedPreferences("records", Context.MODE_PRIVATE)
    val key = "difficulty_$difficulty"

    return prefs.getStringSet(key, emptySet())!!
        .mapNotNull { it.toDoubleOrNull() }
        .sorted()
}
