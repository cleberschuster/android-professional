//package br.com.androidprofessional.presentation
//
//import android.annotation.SuppressLint
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.wrapContentWidth
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.text.KeyboardOptions
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.rounded.Clear
//import androidx.compose.material.icons.rounded.Search
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Surface
//import androidx.compose.material3.Text
//import androidx.compose.material3.TextField
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.livedata.observeAsState
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.saveable.rememberSaveable
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.RectangleShape
//import androidx.compose.ui.res.stringResource
//import androidx.compose.ui.text.input.KeyboardType
//import androidx.compose.ui.unit.dp
//import androidx.lifecycle.viewmodel.compose.viewModel
//import br.com.androidprofessional.R
//
//
//@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
//@Composable
//fun SearchScreen(
//    searchViewModel: SearchViewModel = viewModel(),
//) {
//    Surface(
//        color = MaterialTheme.colorScheme.background
//    ) {
//        Scaffold(
//            topBar = { SearchAppBar(searchViewModel) }
//        ) {
//            Box {
//                ActorsList(searchViewModel)
//            }
//        }
//    }
//}
//
//@Composable
//private fun ActorsList(
//    viewModel: SearchViewModel
//) {
//    val actorsList = viewModel.list.observeAsState().value
//
//    LazyColumn(
//        modifier = Modifier.padding(top = 48.dp)
//    ) {
//        if (!actorsList.isNullOrEmpty()) {
//            items(actorsList) { actor ->
//                Text(
//                    text ="Nome: ${actor.actorName} Numero: ${actor.conta}",
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(horizontal = 20.dp, vertical = 12.dp)
//                        .wrapContentWidth(Alignment.Start)
//                )
//            }
//        }
//    }
//}
//
//@Composable
//private fun SearchAppBar(
//    viewModel: SearchViewModel,
//) {
//    // Immediately update and keep track of query from text field changes.
//    var query: String by rememberSaveable { mutableStateOf("") }
//    var showClearIcon by rememberSaveable { mutableStateOf(false) }
//
//    if (query.isEmpty()) {
//        showClearIcon = false
//    } else if (query.isNotEmpty()) {
//        showClearIcon = true
//    }
//
//    TextField(
//        value = query,
//        onValueChange = { onQueryChanged ->
//            // If user makes changes to text, immediately updated it.
//            query = onQueryChanged
//            // To avoid crash, only query when string isn't empty.
//            if (query.isNotEmpty()) {
//                // Pass latest query to refresh search results.
//                viewModel.performQuery(query)
//            } else {
//                viewModel.loadActors()
//            }
//        },
//        leadingIcon = {
//            Icon(
//                imageVector = Icons.Rounded.Search,
//                tint = MaterialTheme.colorScheme.onBackground,
//                contentDescription = "Search Icon"
//            )
//        },
//        trailingIcon = {
//            if (showClearIcon) {
//                IconButton(onClick = { query = ""; viewModel.loadActors() }) {
//                    Icon(
//                        imageVector = Icons.Rounded.Clear,
//                        tint = MaterialTheme.colorScheme.onBackground,
//                        contentDescription = "Clear Icon"
//                    )
//                }
//            }
//        },
//        maxLines = 1,
////        colors = TextFieldDefaults.textFieldColors(color = Color.Transparent),
//        placeholder = { Text(text = stringResource(R.string.search)) },
//        textStyle = MaterialTheme.typography.titleSmall,
//        singleLine = true,
//        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
//        modifier = Modifier
//            .fillMaxWidth()
//            .background(color = MaterialTheme.colorScheme.background, shape = RectangleShape)
//    )
//}