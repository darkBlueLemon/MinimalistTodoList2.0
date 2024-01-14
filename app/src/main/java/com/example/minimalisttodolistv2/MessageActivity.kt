package com.example.minimalisttodolistv2

import android.os.Bundle
import android.widget.Space
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.clip
import com.example.minimalisttodolistv2.ui.theme.MinimalistTodoListV2Theme
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MessageActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MinimalistTodoListV2Theme {
                val dotLength = 20
                val dotLengthShort = 8
                val interactionSource = remember { MutableInteractionSource() }
                var exit by remember {
                    mutableStateOf(false)
                }
                if(exit) finish()
                Column (
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black),
                ) {

                    AlertDialog(
                        modifier = Modifier.background(Color.Black),
                        onDismissRequest = {
                        }
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(shape = RoundedCornerShape(percent = 7))
                                .background(Color.Black)
                                .size(width = 350.dp, height = 500.dp)
                                .border(
                                    width = 2.dp,
                                    color = Color.White,
                                    shape = RoundedCornerShape(percent = 7)
                                )
                                .padding(20.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize(),
                                verticalArrangement = Arrangement.Top,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Happy Bday!!",
                                    color = Color.White,
                                    style = MaterialTheme.typography.headlineSmall,
                                    modifier = Modifier.padding(bottom = 4.dp)
                                )
                                Spacer(modifier = Modifier.size(height = 1.dp, width = 300.dp).background(Color(0x4FFFFFFF)))
                                LazyColumn(
                                    modifier = Modifier.fillMaxWidth().padding(top = 10.dp)
                                ) {
                                    item {
                                        Text(
                                            text = "Firstly im sorry for the notifications, i kinda wanted to see if i could be the first to " +
                                                    "wish you lol. \n Hopefully that worked and hopefully i didnt sus out the ppl around you (tho id consider that " +
                                                    "to be a win)",
                                            color = Color.White,
                                            softWrap = true,
                                            textAlign = TextAlign.Center,
                                            lineHeight = 20.sp,
                                            fontSize = 16.sp,
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    }
                                    items(dotLengthShort) {
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
                                            text = "Anyways HAPPY BDAY!!!!! \n You growing up too fast bro\n " +
                                                    "And it's sad that i cant do anything irl, why you gotta be so far away huh.",
                                            color = Color.White,
                                            softWrap = true,
                                            textAlign = TextAlign.Center,
                                            lineHeight = 20.sp,
                                            fontSize = 16.sp,
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    }
                                    items(dotLengthShort) {
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
                                            text = "Also *ahem* im not v sure if this will work, nothing i make usually does the first time, " +
                                                    "so if you do see this just lmk, you've no idea how annoying time zones are.\n " +
                                                    "And hopefully this was better than a text message.\nEither ways enjoy your day :)",
                                            color = Color.White,
                                            softWrap = true,
                                            textAlign = TextAlign.Center,
                                            lineHeight = 20.sp,
                                            fontSize = 16.sp,
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    }
                                    items(dotLength) {
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
                                            text = "What you scrolling for huh its over",
                                            color = Color.White,
                                            softWrap = true,
                                            textAlign = TextAlign.Center,
                                            lineHeight = 20.sp,
                                            fontSize = 16.sp,
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    }
                                    items(dotLength) {
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
                                    items(dotLength) {
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
                                            text = "I did warn you lol",
                                            color = Color.White,
                                            softWrap = true,
                                            textAlign = TextAlign.Center,
                                            lineHeight = 20.sp,
                                            fontSize = 16.sp,
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    }
                                    items(dotLength) {
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
                                                    " week is a lil ouch\n Edit: Future amith here its been a few more days, " +
                                                    "dont ask me why its taking so long to finish cause idk and its worse now thanks to you.\n" +
                                                    "Uk its a bit of a wth, twice i teared up out of nowhere during class cause i missed you\nlike-\n" +
                                                    "All that aside ik i dont say this often but learn well!! Ik you obv are but aside from your fam you've also " +
                                                    "got another person who's got faith in you, no pressure lmao.",
                                            color = Color.White,
                                            softWrap = true,
                                            textAlign = TextAlign.Center,
                                            lineHeight = 20.sp,
                                            fontSize = 16.sp,
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    }
                                    items(dotLengthShort) {
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
                                            text = "Also if you ever wanna read this again, idky you would but if you do " +
                                                    "just make a task called" +
                                                    " \"Bday\"",
                                            color = Color.White,
                                            softWrap = true,
                                            textAlign = TextAlign.Center,
                                            lineHeight = 20.sp,
                                            fontSize = 16.sp,
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    }
                                    items(dotLengthShort) {
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
                                            text = "Ok its actually over this time lol",
                                            color = Color.White,
                                            softWrap = true,
                                            textAlign = TextAlign.Center,
                                            lineHeight = 20.sp,
                                            fontSize = 16.sp,
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    }
                                    item {
                                        Column (
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.Center
                                        ) {
                                            Spacer(modifier = Modifier.size(height = 30.dp, width = 10.dp))
                                            Box(
                                                modifier = Modifier
                                                    .clickable(
                                                        interactionSource = interactionSource,
                                                        indication = null,
                                                        onClick = {
                                                            PreferencesManager.bdayVisible = false
                                                            exit = true
                                                        }
                                                    )
//                                                    .align(Alignment.CenterHorizontally)
                                                    .background(Color.Black)
                                                    .size(width = 100.dp, height = 40.dp)
                                                    .border(
                                                        width = 2.dp,
                                                        color = Color.White,
                                                        shape = RoundedCornerShape(percent = 50)
                                                    ),
                                            ) {
                                                Text(
                                                    text = "Byie",
                                                    color = Color.White,
                                                    fontSize = 15.sp,
                                                    fontWeight = FontWeight.SemiBold,
                                                    modifier = Modifier
                                                        .align(Alignment.Center)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}