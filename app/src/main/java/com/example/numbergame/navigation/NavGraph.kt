package com.example.numbergame.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.numbergame.screens.NumberSuccessScreen
import com.example.numbergame.screens.card.CardFailScreen
import com.example.numbergame.screens.card.CardGameScreen
import com.example.numbergame.screens.card.CardSuccessScreen
import com.example.numbergame.screens.main.MainScreen
import com.example.numbergame.screens.number.*
import com.example.numbergame.screens.record.RecordScreen

@Composable
fun NavGraph() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "main"
    ) {

        // 🔹 메인
        composable("main") {
            MainScreen(navController)
        }

        // 🔹 난이도 선택
        composable(
            route = "difficulty/{gameType}",
            arguments = listOf(
                navArgument("gameType") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->

            val gameType =
                backStackEntry.arguments?.getString("gameType")!!

            DifficultyScreen(
                navController = navController,
                gameType = gameType
            )
        }

        // 🔹 게임 화면 (공통)
        composable(
            route = "game/{gameType}/{difficulty}",
            arguments = listOf(
                navArgument("gameType") { type = NavType.StringType },
                navArgument("difficulty") { type = NavType.IntType }
            )
        ) { backStackEntry ->

            val gameType =
                backStackEntry.arguments?.getString("gameType")!!

            val difficulty =
                backStackEntry.arguments?.getInt("difficulty")!!

            if (gameType == "number") {
                NumberGameScreen(navController, difficulty)
            } else if (gameType == "card") {
                CardGameScreen(navController, difficulty)
            }
        }

        // 🔥 숫자 게임 성공 화면 (추가된 부분)
        composable(
            route = "number_success/{difficulty}/{time}",
            arguments = listOf(
                navArgument("difficulty") {
                    type = NavType.IntType
                },
                navArgument("time") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->

            val difficulty =
                backStackEntry.arguments?.getInt("difficulty") ?: 1

            val timeString =
                backStackEntry.arguments?.getString("time")

            val elapsedTime =
                timeString?.toDoubleOrNull()

            NumberSuccessScreen(
                navController = navController,
                difficulty = difficulty,
                elapsedTime = elapsedTime
            )
        }

        // 🔹 카드 성공
        composable("card_success/{difficulty}/{usedTime}") { backStack ->

            val difficulty =
                backStack.arguments?.getString("difficulty")?.toInt() ?: 1

            val usedTime =
                backStack.arguments?.getString("usedTime")?.toInt() ?: 0

            CardSuccessScreen(
                navController = navController,
                difficulty = difficulty,
                usedTime = usedTime
            )
        }

        // 🔹 카드 실패
        composable("card_fail/{difficulty}") { backStack ->
            val difficulty =
                backStack.arguments?.getString("difficulty")?.toInt() ?: 1
            CardFailScreen(navController, difficulty)
        }

        // 🔹 기록 화면
        composable("record") {
            RecordScreen(navController)
        }
    }
}