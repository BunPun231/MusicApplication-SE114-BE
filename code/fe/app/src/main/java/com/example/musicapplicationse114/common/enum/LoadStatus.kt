package com.example.musicapplicationse114.common.enum

sealed class LoadStatus(val description: String = "") {
    class Init() : LoadStatus()
    class Loading(): LoadStatus()
    class Success(): LoadStatus()
    class Error(val error : String): LoadStatus(error)
}