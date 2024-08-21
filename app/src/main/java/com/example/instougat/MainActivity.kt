package com.example.instougat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHost
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.instougat.screens.AccountsScreenObject
import com.example.instougat.screens.CustomPlayerView
import com.example.instougat.screens.MainViewModel
import com.example.instougat.screens.PostsScreenObject
import com.example.instougat.screens.UserPostsScreen
import com.example.instougat.screens.VideoScreenObject
import com.example.instougat.screens.accounts.AccountsScreen
import com.example.instougat.screens.accounts.AccountsViewModel
import com.example.instougat.screens.newuser.NewScreen
import com.example.instougat.ui.theme.InstougatTheme
import com.example.network.models.response.domain.Post
import com.example.network.models.response.remote.RemoteUser

class MainActivity : ComponentActivity() {


    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            InstougatTheme {

                val navController = rememberNavController()
                val sheetState = rememberModalBottomSheetState()
                val scope = rememberCoroutineScope()
                var showBottomSheet by remember { mutableStateOf(false) }

                Scaffold(

                    bottomBar = {
                        BottomAppBar(


                            actions = {
                                IconButton(onClick = {
                                    navController.navigate(AccountsScreenObject){
                                        popUpTo(AccountsScreenObject)
                                        launchSingleTop = true
                                        // Restore state when reselecting a previously selected item
                                        restoreState = true
                                    }

                                }) {
                                    Icon(

                                        Icons.Filled.Person,
                                        contentDescription = "Localized description",
                                    )
                                }
                            },
                            floatingActionButton = {
                                FloatingActionButton(
                                    onClick = { showBottomSheet = true },
                                    containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                                    elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                                ) {
                                    Icon(Icons.Filled.Add, "Localized description")
                                }
                            }
                        )
                    },
                ) {

                    if (showBottomSheet) {
                        ModalBottomSheet(
                            onDismissRequest = {
                                showBottomSheet = false
                            },
                            sheetState = sheetState
                        ) {
                            NewScreen(modifier = Modifier)
                        }
                    }



                    NavHost(
                        navController = navController,
                        startDestination = AccountsScreenObject,
                        modifier = Modifier.padding(it)
                    ) {
                        composable<AccountsScreenObject> {
                            AccountsScreen(navController = navController)
                        }
                        composable("new") {
                            NewScreen()
                        }
                        composable<PostsScreenObject> { navBackStackEntry ->
                            val args = navBackStackEntry.toRoute<PostsScreenObject>()

                            UserPostsScreen(navController = navController,id = args.id)
                        }
                        composable<VideoScreenObject> { navBackStackEntry ->
                            val args = navBackStackEntry.toRoute<VideoScreenObject>()
                            CustomPlayerView(videoUrl = args.url)
                        }
                    }
                }
            }
        }
    }
}
