package com.example.musicapplicationse114.model

import com.example.musicapplicationse114.common.enum.Role

data class UserSignUpRequest(val username : String, val password : String, val email : String,
                             val phone : String, val avatar : String, val role : String//chu y role
)