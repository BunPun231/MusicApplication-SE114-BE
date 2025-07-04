package com.example.musicapplicationse114.model

data class AuthenticationResponse(
val access_token : String?,
val refresh_token : String?,
val message: String
)