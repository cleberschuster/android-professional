package br.com.androidprofessional.presentation

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
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
import br.com.androidprofessional.data.api.Status
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoadingView(context: Context) {
    val viewModel: ObserveStateViewModel = koinViewModel()

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

            CarsContent(context, viewModel)
        }
    }
}

@Composable
fun CarsContent(context: Context, viewModel: ObserveStateViewModel) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current
    val disposable: () -> Unit = {}

    DisposableEffect(lifecycleOwner) {
        onDispose {
            disposable.invoke()
        }
    }

    //Inicio Faz a Busca quando clica no botao
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {

        var textState by remember { mutableStateOf("") }
        TextField(
            placeholder = { Text("Pesquisar") },
            value = textState,
            onValueChange = { textState = it },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            )
        )
        IconButton(onClick = {
            viewModel.getNewComment(textState.toInt())
        }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_warning),
                contentDescription = null,
            )
        }
        Text("INFO: ${textState}")
    }//Fim Faz a Busca quando clica no botao

    //Inicio Faz uma nova Busca a cada caractere digitado
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        var search by rememberSaveable(stateSaver = TextFieldValue.Saver) {
            mutableStateOf(TextFieldValue())
        }

        SearchTextField(
            value = search, onValueChange = {
                search = it
                viewModel.getNewComment(search.text.toInt())
            }, hint = stringResource(R.string.search),
            color = MaterialTheme.colorScheme.background
        )
        IconButton(onClick = {
            viewModel.getNewComment(search.text.toInt())
        }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_warning),
                contentDescription = null,
            )
        }
    }                     //Fim Faz uma nova Busca a cada caractere digitado

    when (uiState.status) {
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
                            text = "COMENTARIO: ${uiState.data?.comment}",
                            style = TextStyle(fontSize = 16.sp)
                        )

                        Spacer(modifier = Modifier.padding(4.dp))
                        Text(
                            modifier = Modifier.padding(vertical = 16.dp),
                            text = "EMAIL: ${uiState.data?.email}",
                            style = TextStyle(fontSize = 16.sp)
                        )

                        Spacer(modifier = Modifier.padding(4.dp))
                        Text(
                            modifier = Modifier.padding(vertical = 16.dp),
                            text = "NOME: ${uiState.data?.name}",
                            style = TextStyle(fontSize = 16.sp)
                        )

                        Spacer(modifier = Modifier.padding(4.dp))
                        Text(
                            modifier = Modifier.padding(vertical = 16.dp),
                            text = "ID: ${uiState.data?.id}",
                            style = TextStyle(fontSize = 16.sp)
                        )

                        Spacer(modifier = Modifier.padding(4.dp))
                        Text(
                            modifier = Modifier.padding(vertical = 16.dp),
                            text = "POST ID: ${uiState.data?.postId}",
                            style = TextStyle(fontSize = 16.sp)
                        )
                    }
                }
            }
        }
        Status.ERROR -> Toast.makeText(context, "Ocorreu um erro ${uiState.message}", Toast.LENGTH_LONG).show()
        Status.LOADING -> ShimmerScreen()
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



//package br.com.androidprofessional.presentation
//
//import android.content.Context
//import android.widget.Toast
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.material3.Card
//import androidx.compose.material3.CardDefaults
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.material3.TextField
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.DisposableEffect
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.saveable.rememberSaveable
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalConfiguration
//import androidx.compose.ui.platform.LocalLifecycleOwner
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.res.stringResource
//import androidx.compose.ui.text.TextStyle
//import androidx.compose.ui.text.input.KeyboardType
//import androidx.compose.ui.text.input.TextFieldValue
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.lifecycle.compose.collectAsStateWithLifecycle
//import br.com.androidprofessional.R
//import br.com.androidprofessional.data.api.Status
//import org.koin.androidx.compose.koinViewModel
//
//@Composable
//fun LoadingView(context: Context) {
//    val viewModel: ObserveStateViewModel = koinViewModel()
//
//    Column(
//        modifier = Modifier
//            .padding(horizontal = 16.dp)
//            .padding(top = 16.dp)
//    ) {
//        Card(
//            modifier = Modifier
//                .size((LocalConfiguration.current.screenWidthDp.dp)/0),
//            colors = CardDefaults.cardColors(
//                containerColor = Color.White
//            ),
//            elevation = CardDefaults.cardElevation(8.dp),
//            shape = RoundedCornerShape(10),
//        ) {
//
//            CarsContent(context, viewModel)
//        }
//    }
//}
//
//@Composable
//fun CarsContent(context: Context, viewModel: ObserveStateViewModel) {
//
//    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
//    val lifecycleOwner = LocalLifecycleOwner.current
//    val disposable: () -> Unit = {}
//
//    DisposableEffect(lifecycleOwner) {
//        onDispose {
//            disposable.invoke()
//        }
//    }
//
//    //Inicio Faz a Busca quando clica no botao
//    Row(
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//
//        val textState = remember { mutableStateOf("") }
//        TextField(
//            placeholder = { Text("Pesquisar") },
//            value = textState.value,
//            onValueChange = { textState.value = it },
//            keyboardOptions = KeyboardOptions(
//                keyboardType = KeyboardType.Number
//            )
//        )
//        IconButton(onClick = {
//            viewModel.getNewComment(textState.value.toInt())
//        }) {
//            Icon(
//                painter = painterResource(id = R.drawable.ic_warning),
//                contentDescription = null,
//            )
//        }
//        Text("INFO: ${textState.value}")
//    }//Fim Faz a Busca quando clica no botao
//
//    //Inicio Faz uma nova Busca a cada caractere digitado
//    Row(
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        var search by rememberSaveable(stateSaver = TextFieldValue.Saver) {
//            mutableStateOf(TextFieldValue())
//        }
//
//        SearchTextField(
//            value = search, onValueChange = {
//                search = it
//                viewModel.getNewComment(it.text.toInt())
//            }, hint = stringResource(R.string.search),
//            color = MaterialTheme.colorScheme.background
//        )
//        IconButton(onClick = {
//            viewModel.getNewComment(search.text.toInt())
//        }) {
//            Icon(
//                painter = painterResource(id = R.drawable.ic_warning),
//                contentDescription = null,
//            )
//        }
//    }                     //Fim Faz uma nova Busca a cada caractere digitado
//
//    when (uiState.status) {
//        Status.SUCCESS -> {
//
//            Text(
//                modifier = Modifier.padding(vertical = 16.dp),
//                text = "COMENTARIO: ${uiState.data?.comment}",
//                style = TextStyle(fontSize = 16.sp)
//            )
//            Text(
//                modifier = Modifier.padding(vertical = 16.dp),
//                text = "EMAIL: ${uiState.data?.email}",
//                style = TextStyle(fontSize = 16.sp)
//            )
//            Text(
//                modifier = Modifier.padding(vertical = 16.dp),
//                text = "NOME: ${uiState.data?.name}",
//                style = TextStyle(fontSize = 16.sp)
//            )
//            Text(
//                modifier = Modifier.padding(vertical = 16.dp),
//                text = "ID: ${uiState.data?.id}",
//                style = TextStyle(fontSize = 16.sp)
//            )
//            Text(
//                modifier = Modifier.padding(vertical = 16.dp),
//                text = "POST ID: ${uiState.data?.postId}",
//                style = TextStyle(fontSize = 16.sp)
//            )
//        }
//        Status.ERROR -> Toast.makeText(context, "Ocorreu um erro ${uiState.message}", Toast.LENGTH_LONG).show()
//        Status.LOADING -> Text("Carregando...")
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
