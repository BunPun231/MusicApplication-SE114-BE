package com.example.musicapplicationse114.ui.screen.logout

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.musicapplicationse114.ui.theme.MusicApplicationSE114Theme

@Composable
fun LogoutScreen(
    onDismiss: () -> Unit,
    onConfirmLogout: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        LogoutDialogContent(onDismiss = onDismiss, onConfirmLogout = onConfirmLogout)
    }
}

@Composable
fun LogoutDialogContent(
    onDismiss: () -> Unit,
    onConfirmLogout: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(Color.Black, shape = MaterialTheme.shapes.medium)
    ) {
        // Header: Title + Close
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Logout",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close",
                tint = Color.White,
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onDismiss() }
            )
        }

        // Message
        Text(
            text = "Are you sure you want to logout of Musico?",
            color = Color.Gray,
            fontSize = 16.sp,
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .padding(bottom = 24.dp)
        )

        Divider(color = Color.DarkGray)

        // Confirm Button
        Button(
            onClick = onConfirmLogout,
            colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text("Logout", color = Color.Black, fontSize = 16.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLogoutDialogContent() {
    MusicApplicationSE114Theme(darkTheme = true) {
        LogoutDialogContent(
            onDismiss = {},
            onConfirmLogout = {}
        )
    }
}
