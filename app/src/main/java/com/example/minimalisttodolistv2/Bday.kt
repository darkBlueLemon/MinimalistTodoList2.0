package com.example.minimalisttodolistv2

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun Bday() {
    val dotLength = 2
    val dotLengthShort = 1
    AlertDialog(
        modifier = Modifier,
        onDismissRequest = {
        }
    ) {
        Box(
            modifier = Modifier
                .clip(shape = RoundedCornerShape(percent = 7))
                .background(Color.Black)
                .size(width = 350.dp, height = 700.dp)
                .border(
                    width = 2.dp,
                    color = Color.White,
                    shape = RoundedCornerShape(percent = 7)
                )
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            Column (
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(
                    text = "Happy Bday!!",
                    color = Color.White,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 10.dp)
                )
                LazyColumn(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    item {
                        Text(
                            text = "Firstly im sorry for the notifications, i wanted to see if i could be the first one to " +
                                    "wish you. \n Hopefully that worked and hopefully i didnt sus out the ppl around you but" +
                                    " les see",
                            color = Color.White,
                            softWrap = true,
                            textAlign = TextAlign.Center,
                            lineHeight = 20.sp,
                            fontSize = 16.sp,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    items (dotLengthShort) {
                        Text(
                            text = ".",
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            fontSize = 16.sp,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    item {
                        Text(
                            text = "Anyways HAPPY BDAY!! \n You growing up too fast imma be honest\nAlso You finally old " +
                                    "enough to drink (:\n Enjoy your day :)",
                            color = Color.White,
                            softWrap = true,
                            textAlign = TextAlign.Center,
                            lineHeight = 20.sp,
                            fontSize = 16.sp,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    items (dotLengthShort) {
                        Text(
                            text = ".",
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            fontSize = 16.sp,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    item {
                        Text(
                            text = "Also *ahem* im not v sure if this will work so if you do see this just lmk",
                            color = Color.White,
                            softWrap = true,
                            textAlign = TextAlign.Center,
                            lineHeight = 20.sp,
                            fontSize = 16.sp,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    items (dotLength) {
                        Text(
                            text = ".",
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            fontSize = 16.sp,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    item {
                        Text(
                            text = "What you scrolling for its over",
                            color = Color.White,
                            softWrap = true,
                            textAlign = TextAlign.Center,
                            lineHeight = 20.sp,
                            fontSize = 16.sp,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    items (dotLength) {
                        Text(
                            text = ".",
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            fontSize = 16.sp,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    item {
                        Text(
                            text = "You sure you wanna go on?",
                            color = Color.White,
                            softWrap = true,
                            textAlign = TextAlign.Center,
                            lineHeight = 20.sp,
                            fontSize = 16.sp,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    items (dotLength) {
                        Text(
                            text = ".",
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            fontSize = 16.sp,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    item {
                        Text(
                            text = "You've been warned",
                            color = Color.White,
                            softWrap = true,
                            textAlign = TextAlign.Center,
                            lineHeight = 20.sp,
                            fontSize = 16.sp,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    items (dotLength) {
                        Text(
                            text = ".",
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            fontSize = 16.sp,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    item {
                        Text(
                            text = "You've no idea how much i miss you\n and fact that you gonna see this after like a" +
                                    " week is a lil ouch",
                            color = Color.White,
                            softWrap = true,
                            textAlign = TextAlign.Center,
                            lineHeight = 20.sp,
                            fontSize = 16.sp,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    items (dotLength) {
                        Text(
                            text = ".",
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            fontSize = 16.sp,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}