package com.example.instougat.screens.newuser

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun NewScreen(modifier: Modifier = Modifier) {

    var username by remember {
        mutableStateOf("")
    }
    val viewmodel  = viewModel<NewUserViewModel>()
    val result by remember {
        viewmodel.res
    }

    Box(modifier = modifier.fillMaxWidth().padding(12.dp), contentAlignment = Alignment.Center) {
        Column {

            TextField(value = username, onValueChange = {
                username = it
            }, placeholder = { Text(text = "username")})
            Button(onClick = { viewmodel.getUser(username) }) {
                Text(text = "submit")
            }
            Text(text = result)
        }
    }
}