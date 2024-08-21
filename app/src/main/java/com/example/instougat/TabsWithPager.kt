package com.example.instougat

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.instougat.ui.theme.InstougatTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TabsWithViewPager() {
    val tabs = listOf("Tab 1", "Tab 2", "Tab 3")
    val pagerState = rememberPagerState(3){0}
    val coroutineScope = rememberCoroutineScope()

    Row(modifier = Modifier.fillMaxSize()) {
        VerticalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
        ) { page ->
            when (page) {
                0 -> TabContent1()
                1 -> TabContent2()
                2 -> TabContent3()
            }
        }
        Column(
            modifier = Modifier.wrapContentWidth().weight(1f)
        ) {
            tabs.forEachIndexed { index, tab ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    modifier = Modifier.padding(vertical = 16.dp)
                ) {
                    Text(
                        text = tab,
                        fontSize = 18.sp,
                        modifier = Modifier.graphicsLayer {
                            rotationZ = 270f
                        }
                    )
                }
            }
        }

    }
}

@Composable
fun TabContent1() {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(text = "Content for Tab 1", modifier = Modifier.align(Alignment.Center))
    }
}

@Composable
fun TabContent2() {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(text = "Content for Tab 2", modifier = Modifier.align(Alignment.Center))
    }
}

@Composable
fun TabContent3() {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(text = "Content for Tab 3", modifier = Modifier.align(Alignment.Center))
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    InstougatTheme {
        TabsWithViewPager()
    }
}
