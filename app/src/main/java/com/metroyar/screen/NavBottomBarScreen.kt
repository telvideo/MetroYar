package com.metroyar.screen

import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.exyte.animatednavbar.AnimatedNavigationBar
import com.exyte.animatednavbar.animation.balltrajectory.Parabolic
import com.exyte.animatednavbar.animation.indendshape.Height
import com.exyte.animatednavbar.animation.indendshape.shapeCornerRadius
import com.metroyar.R
import com.metroyar.ui.theme.turnedOff
import com.metroyar.utils.playSound
import com.metroyar.utils.vibratePhone
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalMaterial3Api::class)
@RootNavGraph(start = true)
@Destination
@Composable
fun NavigationBottom(navigator: DestinationsNavigator) {
    val context = LocalContext.current
    var selectedMenuIndex by remember { mutableIntStateOf(1) }
    var selectedScreenTopBarTitle by remember { mutableStateOf(BottomNavItem.Navigation.title) }
    val bottomBarItems = remember { BottomNavItem.values() }

    Scaffold(
        containerColor=MaterialTheme.colorScheme.onPrimary,
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.height(50.dp),
                title = {
                    Box(Modifier.fillMaxHeight(), contentAlignment = Alignment.Center) {
                        Text(text = selectedScreenTopBarTitle)
                    }
                })
        },
        bottomBar = {
            AnimatedNavigationBar(
                selectedIndex = selectedMenuIndex,
                modifier = Modifier.height(70.dp).padding(start = 12.dp, bottom = 12.dp, end = 12.dp),
                cornerRadius = shapeCornerRadius(cornerRadius = 34.dp),
                ballAnimation = Parabolic(tween(300)),
                indentAnimation = Height(tween(600)),
                barColor = MaterialTheme.colorScheme.onSecondary,
                ballColor = MaterialTheme.colorScheme.secondaryContainer
            ) {
                bottomBarItems.forEach {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .nonRipple {
                                playSound(context = context, soundResourceId = R.raw.sound)
                                selectedMenuIndex = it.ordinal
                                selectedScreenTopBarTitle = it.title
                                vibratePhone(context)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            modifier = Modifier
                                .size(26.dp),
                            painter = painterResource(id = it.icon),
                            contentDescription = "",
                            tint = if (selectedMenuIndex == it.ordinal) MaterialTheme.colorScheme.secondaryContainer
                            else turnedOff
                        )
                    }
                }
            }
        }, content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
            ) {
                DeciderOfScreensInNavBotBar(input = selectedMenuIndex, navigator = navigator)
            }
        }
    )
}

enum class BottomNavItem(val icon: Int, val title: String) {
    Account(icon = R.drawable.baseline_account_circle_24, title = "مترویارمن"),
    Navigation(icon = R.drawable.baseline_near_me_24, title = "مسیریابی"),
    MetroMap(icon = R.drawable.baseline_map_24, title = "بروزترین نسخه نقشه مترو")
}

fun Modifier.nonRipple(onclick: () -> Unit): Modifier = composed {
    clickable(
        indication = null,
        interactionSource = remember {
            MutableInteractionSource()
        }) {
        onclick()
    }
}

@Composable
fun DeciderOfScreensInNavBotBar(input: Int, navigator: DestinationsNavigator) {
    when (input) {
        0 -> AccountScreen()
        1 -> NavigationScreen(context = LocalContext.current, navigator = navigator)
        2 -> MetroMapScreen()
    }
}