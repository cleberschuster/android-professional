package br.com.androidprofessional.presentation

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.androidprofessional.R
import br.com.androidprofessional.data.api.ApiState
import br.com.androidprofessional.data.api.Status
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun InfoScreen(viewModel: ObserveStateViewModel = koinViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current
    val disposable: () -> Unit = {}

//    DisposableEffect(lifecycleOwner) {
//        onDispose {
//            disposable.invoke()
//        }
//    }
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp)
    ) {
        Card(
            modifier = Modifier
                .size((LocalConfiguration.current.screenWidthDp.dp)/0),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(8.dp),
            shape = RoundedCornerShape(10),
        ) {

            ScreenContent(viewModel, uiState)
        }
    }
}

@Composable
fun ScreenContent(viewModel: ObserveStateViewModel, uiStateValue: ApiState) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        var searchText by rememberSaveable(stateSaver = TextFieldValue.Saver) {
            mutableStateOf(TextFieldValue())
        }
//        var searchText by rememberSaveable { mutableStateOf("") }

        SearchTextField(
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            value = searchText,
            onValueChange = {
                searchText = it
            },
            hint = stringResource(R.string.search),
            color = MaterialTheme.colorScheme.background
        )

        IconButton(onClick = {
            if (searchText.text.isNotEmpty()) {
                // Pass latest query to refresh search results.
                coroutineScope.launch { viewModel.getNewComment(searchText.text.toInt()) }
                Toast.makeText(context, "Call APi onValueChange", Toast.LENGTH_SHORT).show()

            } else {
                Toast.makeText(context, "O text field esta vazio", Toast.LENGTH_SHORT).show()
            }

        }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_warning),
                contentDescription = null,
            )
        }
        Text("INFO: ${searchText.text}")
    }

    when (uiStateValue.status) {
        Status.SUCCESS -> {

            Box(
                modifier = Modifier
//            .fillMaxSize()
                    .background(color = Color.White)
                    .padding(48.dp)
            ) {
                Column(
                    modifier = Modifier.align(alignment = Alignment.TopCenter)
                ) {

                    Column {
                        Text(
                            modifier = Modifier.padding(vertical = 16.dp),
                            text = "COMENTARIO: ${uiStateValue.data?.comment}",
                            style = TextStyle(fontSize = 16.sp)
                        )

                        Spacer(modifier = Modifier.padding(4.dp))
                        Text(
                            modifier = Modifier.padding(vertical = 16.dp),
                            text = "EMAIL: ${uiStateValue.data?.email}",
                            style = TextStyle(fontSize = 16.sp)
                        )

                        Spacer(modifier = Modifier.padding(4.dp))
                        Text(
                            modifier = Modifier.padding(vertical = 16.dp),
                            text = "NOME: ${uiStateValue.data?.name}",
                            style = TextStyle(fontSize = 16.sp)
                        )

                        Spacer(modifier = Modifier.padding(4.dp))
                        Text(
                            modifier = Modifier.padding(vertical = 16.dp),
                            text = "ID: ${uiStateValue.data?.id}",
                            style = TextStyle(fontSize = 16.sp)
                        )

                        Spacer(modifier = Modifier.padding(4.dp))
                        Text(
                            modifier = Modifier.padding(vertical = 16.dp),
                            text = "POST ID: ${uiStateValue.data?.postId}",
                            style = TextStyle(fontSize = 16.sp)
                        )
                    }
                }
            }
        }

        Status.ERROR -> {
            ErrorScreen(uiStateError = uiStateValue.message.toString())
            LaunchedEffect(true) {
                Toast.makeText(context, "Ocorreu um erro ${uiStateValue.message}", Toast.LENGTH_LONG).show()
            }

        }

        Status.LOADING -> ShimmerScreen()
    }
}


//Exwemplo Hoisting State
//@Composable
//fun MyScreen(viewModel: MyViewModel) {
//    val uiState by viewModel.uiState.collectAsState()
//    DisplayButton(
//        text = uiState.counter.toString(),
//        onClick = { viewModel.incrementCounter() }
//    )
//}
//@Composable
//fun DisplayButton(
//    text: String,
//    onClick: () -> Unit
//) {
//    Column {
//        Text( text = "Counter value: $text")
//        Button( onClick = onClick) {
//            Text( "Increment" )
//        }
//    }
//}



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

