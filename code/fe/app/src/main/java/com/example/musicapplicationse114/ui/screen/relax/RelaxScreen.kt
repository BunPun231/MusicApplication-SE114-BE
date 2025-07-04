package com.example.musicapplicationse114.ui.screen.relax

import android.graphics.drawable.Icon
import android.media.Image
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.musicapplicationse114.R

@Composable
fun RelaxScreen()
{
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Black
    )
    {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(start = 24.dp,
            )
        ) {
            Spacer(modifier = Modifier.height(50.dp))
            HelloLine()
            Spacer(modifier = Modifier.height(30.dp))
            TabBar()
            Spacer(modifier = Modifier.height(30.dp))
//            FeaturingToday()
//            Spacer(modifier = Modifier.height(18.dp))
//            RecentlyPlayed()
//            Spacer(modifier = Modifier.height(12.dp))
            Box {
                Relax()
                NavigationBar()
            }
//        LazyColumn {
//            FeaturingToday()
//            Spacer(modifier = Modifier.height(10.dp))
//            RecentlyPlayed()
//            Spacer(modifier = Modifier.height(10.dp))
//            MixedForYou()
//        }

        }

    }
}

@Composable
//ở cái này thì ảnh đại diện thay đổi theo profile, chưa làm
//Còn lời chào good evening nữa
//giờ để tạm cố định để demoUI, sau phát triển lên thêm
fun HelloLine()
{
    //gồm hi Logan, good evening, chuông và ảnh đại diện
    //Cố định trên mành hình không bị mất đi khi scroll dọc
    Row (verticalAlignment = Alignment.CenterVertically){
        Column(horizontalAlignment = Alignment.Start) {
            Row (verticalAlignment = Alignment.CenterVertically){
                //Spacer(modifier = Modifier.height(20.dp))
                Image(
                    painter = painterResource(id = R.drawable.hello),
                    contentDescription = null,
                    modifier = Modifier
                        .size(23.dp)
                )

                Spacer(modifier = Modifier.width(2.dp))

                Text("Hi Logan,",
                    fontSize = 20.sp,
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text("Good Evening",
                fontSize = 24.sp,
                color = Color.White,
                fontWeight = FontWeight.W700
            )
        }
        Spacer(modifier = Modifier.width(95.dp))
        Image(painter = painterResource(id = R.drawable.bell),
            contentDescription = null,
            modifier = Modifier
                .size(30.dp)
        )
        Spacer(modifier = Modifier.width(15.dp))
        Image(painter = painterResource(id = R.drawable.logan),
            contentDescription = null,
            modifier = Modifier
                .size(50.dp))
    }

}

@Composable
//ở cái này thì gồm các tab để chuyển sang các pager khác nhau
//chưa làm chủ yếu bây giờ demo UI trước
fun TabBar()
{
    //gồm dòng chữ For you...
    //Cố định trên mành hình không bị mất đi khi scroll dọc
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text("For you",
            fontSize = 19.sp,
            color = Color.LightGray
        )
        Spacer(modifier = Modifier.width(25.dp))
        Box (modifier = Modifier.
        background(color = Color.DarkGray,
            shape = RoundedCornerShape(18.dp))
            .padding(horizontal = 18.dp, vertical = 8.dp)
        ) {
            Text(
                "Relax",
                fontSize = 19.sp,
                color = Color.LightGray
            )
        }
        Spacer(modifier = Modifier.width(25.dp))
        Text("Work out",
            fontSize = 19.sp,
            color = Color.LightGray)
        Spacer(modifier = Modifier.width(25.dp))
        Text("Travel",
            maxLines = 1,
            fontSize = 19.sp,
            color = Color.LightGray)
    }
}

@Composable
//Featuring Today
fun FeaturingToday()
{
    Image(painter = painterResource(R.drawable.feature),
        contentDescription = null,
        modifier = Modifier
            .size(width = 400.dp, height = 200.dp)
            .offset(x = -10.dp))
}

@Composable
//Recently Played
//hình ảnh ở đây tạm thời kéo vào android studio demo, và tạo các thành phần
//bài hát không có tính tái sử dụng, tương lai sẽ update và link tới bài hát khi có
//backend
fun RecentlyPlayed()
{
    Image(painter = painterResource(R.drawable.recent_play),
        contentDescription = null,
        modifier = Modifier
            .size(width = 400.dp, height = 200.dp)
            .offset(x = -10.dp))
}

@Composable
//Mixed for you
fun Relax()
{
    Image(painter = painterResource(R.drawable.relax),
        contentDescription = null,
        modifier = Modifier
            .size(width = 1000.dp, height = 1000.dp)
            .offset(x = -10.dp))
}

@Composable
//Navigation bar
fun NavigationBar()
{
    Box (modifier = Modifier
        .fillMaxWidth()
        .height(80.dp)
        .offset(y = 575.dp)
        .background(Color.Black.copy(alpha = 0.8f))
    ){
        Spacer(modifier = Modifier.height(20.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Spacer(modifier = Modifier.width(60.dp))
            Column (horizontalAlignment = Alignment.CenterHorizontally){
                Icon(Icons.Filled.Home, contentDescription = null,tint = Color.LightGray, modifier = Modifier
                    .size(30.dp))
                Spacer(modifier = Modifier.height(3.dp))
                Text("Home",
                    fontSize = 12.sp,
                    color = Color.LightGray)
            }
            Spacer(modifier = Modifier.width(60.dp))
            Column (horizontalAlignment = Alignment.CenterHorizontally){
                Icon(Icons.Filled.Search, contentDescription = null,tint = Color.LightGray, modifier = Modifier
                    .size(30.dp))
                Spacer(modifier = Modifier.height(3.dp))
                Text("Search",
                    fontSize = 12.sp,
                    color = Color.LightGray)
            }
            Spacer(modifier = Modifier.width(60.dp))
            Column (horizontalAlignment = Alignment.CenterHorizontally){
                Icon(Icons.Filled.Menu, contentDescription = null,tint = Color.LightGray, modifier = Modifier
                    .size(30.dp))
                Spacer(modifier = Modifier.height(3.dp))
                Text("Library",
                    fontSize = 12.sp,
                    color = Color.LightGray)
            }
        }
    }
}
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RelaxScreenPreview()
{
    RelaxScreen()
}