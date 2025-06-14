package org.nunocky.myfirebasetextapp

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import org.nunocky.myfirebasetextapp.ui.theme.MyFirebaseTextAppTheme
import javax.inject.Inject

@HiltViewModel
class GreetingViewModel @Inject constructor(
    val application: Application
) : ViewModel() {

}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyFirebaseTextAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val vm = hiltViewModel<GreetingViewModel>()
                    Greeting(
                        modifier = Modifier.padding(innerPadding),
                        name = "Android",
                        viewModel = vm
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(
    modifier: Modifier = Modifier,
    name: String,
    viewModel: GreetingViewModel,
) {
    GreetingScreen(name = name, modifier = modifier)
}

@Composable
fun GreetingScreen(
    modifier: Modifier = Modifier,
    name: String,
) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyFirebaseTextAppTheme {
        GreetingScreen(name = "Android")
    }
}