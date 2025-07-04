package com.example.musicapplicationse114.ui.screen.start

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.musicapplicationse114.MainViewModel
import com.example.musicapplicationse114.R

@Composable
fun StartScreen(navController: NavController, viewModel: StartViewModel, mainViewModel: MainViewModel) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Black
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // Logo with icons as a single image
            Image(
                painter = painterResource(id = R.drawable.musico_with_icons), // Use your combined image here
                contentDescription = "Musico logo with icons",
                modifier = Modifier
                    .offset(x = 3.dp, y = 45.dp)// Adjust size based on your image
                    .size(width = 214.dp, height = 74.dp)
            )

            Text(
                text = "Just keep on vibin’",
                fontSize = 16.sp,
                modifier = Modifier
                    .offset(x = 26.dp, y = 55.dp)
                    .size(width = /*170.dp*/ 200.dp, height = 90.dp),
                color = Color(0xFFB0B0B0)
            )

            Spacer(modifier = Modifier.weight(0.8f))

            // Sign Up Button
            Button(
                onClick = { viewModel.navToSignUpScreen(navController) },
                modifier = Modifier
                    .offset(x = 1.dp, y = -40.dp)
                    .size(width = 325.dp, height = 62.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White
                ),
                shape = RoundedCornerShape(24.dp)
            ) {
                Text("Sign up", fontSize = 16.sp , fontWeight = FontWeight.Bold, color = Color.Black)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Login Button
            OutlinedButton(
                onClick = { viewModel.navToLoginScreen(navController) },
                modifier = Modifier
                    .offset(x = 1.dp, y = -40.dp)
                    .size(width = 325.dp, height = 62.dp),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, Color.White)
            ) {
                Spacer(modifier = Modifier.width(8.dp))
                Text("Login",
                    modifier = Modifier
                        .offset(x = -10.dp),
                    fontSize = 16.sp,
                    color = Color.White)
            }

            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun StartScreenPreview() {
    val navController = rememberNavController()
    StartScreen(navController = navController, viewModel = StartViewModel(null, null), mainViewModel = MainViewModel())
}