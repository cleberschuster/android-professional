package br.com.androidprofessional.presentation

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.androidprofessional.R
import br.com.androidprofessional.data.api.Status
import br.com.androidprofessional.ui.theme.ArsenalButton
import br.com.androidprofessional.ui.theme.ArsenalTheme
import br.com.androidprofessional.ui.theme.ArsenalThemeExtended
import br.com.androidprofessional.utils.ArsenalCircularProgressIndicator
import org.koin.androidx.compose.koinViewModel

// 1) COMO OBSERVAR ESTADOS EM COMPOSE COM VIEWMODEL
// 2) COMO CRIAR UM COMPONENTE REUTILIZÁVEL PARA LOADING SCREENS
// 3) COMO PASSAR ESTADO PARA COMPOSABLE E COMO ALTERA-LO (LOADING VIEW)
// 4) COMO INSTANCIAR O FLOW NO PREVIEW USANDO VIEW MODEL

// LINK VIDEO: https://youtu.be/kuwZX2fSj5A

@Composable
fun LoadingView(context: Context) {
    val viewModel: ObserveStateViewModel = koinViewModel()
    val uiState by viewModel.commentState.collectAsStateWithLifecycle()
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

        Status.SUCCESS -> {
            Text("Resultado: ${uiState.data?.comment}")
        }

        Status.ERROR -> Toast.makeText(context, "Ocorreu um erro, tenta novamente", Toast.LENGTH_SHORT).show()
        Status.LOADING -> Text("Carregando...")
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
