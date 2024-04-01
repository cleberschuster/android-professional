package br.com.androidprofessional.presentation

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.androidprofessional.data.api.Status
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoadingView(context: Context) {
    val viewModel: ObserveStateViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current
    val disposable: () -> Unit = {}

    DisposableEffect(lifecycleOwner) {
        onDispose {
            disposable.invoke()
        }
    }

//    Column {
//        Text("Counter value: ${uiState.data}")
//        Button(onClick = { viewModel.getNewComment(2) }) {
//            Text("Increment")
//        }
////        Button(onClick = { viewModel.getNewComment(binding.searchEditText.text.toString().toInt()) }) {
////            Text("Increment")
////        }
//    }

    when (uiState.status) {

        Status.LOADING -> Text("Carregando...")

        Status.SUCCESS -> {
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 16.dp)
            ) {
                Card(
//                modifier = Modifier
//                    .size((LocalConfiguration.current.screenWidthDp.dp)/0),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    elevation = CardDefaults.cardElevation(8.dp),
                    shape = RoundedCornerShape(10),
                ) {

                    Text(
                        modifier = Modifier.padding(vertical = 16.dp),
                        text = "Resultado: ${uiState.data?.comment}",
                        style = TextStyle(fontSize = 16.sp)
                    )
                    Text(
                        modifier = Modifier.padding(vertical = 16.dp),
                        text = "Resultado: ${uiState.data?.email}",
                        style = TextStyle(fontSize = 16.sp)
                    )
                    Text(
                        modifier = Modifier.padding(vertical = 16.dp),
                        text = "Resultado: ${uiState.data?.name}",
                        style = TextStyle(fontSize = 16.sp)
                    )
                    Text(
                        modifier = Modifier.padding(vertical = 16.dp),
                        text = "Resultado: ${uiState.data?.id}",
                        style = TextStyle(fontSize = 16.sp)
                    )
                }

            }
        }

        Status.ERROR -> Toast.makeText(context, "Ocorreu um erro ${uiState.message}", Toast.LENGTH_SHORT).show()
    }
}

//@Preview
//@Composable
//fun LoadingViewLightPreview() {
//    ArsenalTheme(useDarkTheme = false) {
//        LoadingView(ObserveStateViewModel().apply {
//            loadingStateFlow.value = true
//        })
//    }
//}
//
//@Preview
//@Composable
//fun LoadingViewDarkPreview() {
//    ArsenalTheme(useDarkTheme = true) {
//        LoadingView(ObserveStateViewModel().apply {
//            loadingStateFlow.value = false
//        })
//    }
//}
