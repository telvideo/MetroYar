package com.metroyar.activity

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.exyte.animatednavbar.AnimatedNavigationBar
import com.exyte.animatednavbar.animation.balltrajectory.Parabolic
import com.exyte.animatednavbar.animation.indendshape.Height
import com.exyte.animatednavbar.animation.indendshape.shapeCornerRadius
import com.metroyar.GlobalObjects
import com.metroyar.R
import com.metroyar.Result
import com.metroyar.ui.theme.MetroYarTheme
import com.metroyar.utils.initiateStationsAndAdjNodesLineNum
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import kotlin.system.measureTimeMillis

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MetroYarTheme {
                initiateStationsAndAdjNodesLineNum(LocalContext.current)
                Log.i(
                    GlobalObjects.TAG,
                    Result(
                        LocalContext.current,
                        "میدان حضرت ولی\u200Cعصر",
                        "خواجه عبدالله انصاری"
                    ).convertPathToUserUnderstandableForm().toString()
                )

                DestinationsNavHost(navGraph = NavGraphs.root)
            }
        }
    }
}

@RootNavGraph(start = true)
@Destination
@Composable
fun Greeting() {
    var selectedIndex by remember { mutableStateOf(1) }
    val navBarItems = remember { NavBarItems.values() }

    Scaffold(
        modifier = Modifier.padding(all = 12.dp),
        bottomBar = {
            AnimatedNavigationBar(
                selectedIndex = selectedIndex,
                modifier = Modifier.height(64.dp),
                cornerRadius = shapeCornerRadius(cornerRadius = 34.dp),
                ballAnimation = Parabolic(tween(300)),
                indentAnimation = Height(tween(600)),
                barColor = MaterialTheme.colorScheme.primary,
                ballColor = MaterialTheme.colorScheme.primary
            ) {
                navBarItems.forEach {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .nonRipple { selectedIndex = it.ordinal },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            modifier = Modifier.size(26.dp),
                            imageVector = it.icon,
                            contentDescription = "",
                            tint = if (selectedIndex == it.ordinal) MaterialTheme.colorScheme.secondaryContainer
                            else MaterialTheme.colorScheme.onSecondary
                        )
                    }
                }
            }
        }, content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
            ) {
                Decider(input = selectedIndex)
            }
        }
    )
}

enum class NavBarItems(val icon: ImageVector) {
    Person(icon = Icons.Default.Person),
    Call(icon = Icons.Outlined.Share),
    Settings(icon = Icons.Default.Settings),
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
fun PersonNavItem() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Person")
    }
}

@Composable
fun CallNavItem() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Direction")
    }
}

@Composable
fun SettingsNavItem() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Settings")
    }
}

@Composable
fun Decider(input: Int) {

    when (input) {
        0 -> PersonNavItem()
        1 -> CallNavItem()
        2 -> SettingsNavItem()
    }
}
