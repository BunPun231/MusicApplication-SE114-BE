package com.example.musicapplicationse114.ui.screen.start

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.musicapplicationse114.Screen
import com.example.musicapplicationse114.repositories.Api
import com.example.musicapplicationse114.repositories.MainLog
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class StartViewModel @Inject constructor(
    private val mainLog: MainLog?,
    private val api: Api?
): ViewModel() {
    fun navToSignUpScreen(navController: NavController)
    {
        navController.navigate(Screen.SignUp.route)
    }

    fun navToLoginScreen(navController: NavController)
    {
        navController.navigate(Screen.Login.route)
    }
}