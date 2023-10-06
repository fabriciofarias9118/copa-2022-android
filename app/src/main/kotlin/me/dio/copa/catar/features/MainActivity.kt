package me.dio.copa.catar.features

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import dagger.hilt.android.AndroidEntryPoint
import me.dio.copa.catar.extensions.observe
import me.dio.copa.catar.notification.scheduler.extensions.NotificationMatchWorker
import me.dio.copa.catar.ui.theme.Copa2022Theme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeAction()
        setContent {
            Copa2022Theme {
                val state by viewModel.state.collectAsState()
                MainScreen(matches = state.matches, viewModel::toggleNotification)
                Log.e("TAG", "onCreate: ${state.matches}")
            }
        }
    }

    private fun observeAction() {
        viewModel.action.observe(this) { action ->
            when (action) {
                is MainUiAction.DisableNotification ->
                    NotificationMatchWorker.cancel(applicationContext,action.match)
                is MainUiAction.EnableNotification ->
                    NotificationMatchWorker.start(applicationContext,action.match)
                is MainUiAction.MatchesNotFound -> TODO()
                MainUiAction.Unexpected -> TODO()
            }
        }
    }

}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Composable
@Preview(showBackground = true)
fun DefaultPreview() {
    Copa2022Theme {
        Greeting("Android")
    }
}
