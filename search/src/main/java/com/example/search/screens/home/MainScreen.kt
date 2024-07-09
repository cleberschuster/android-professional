package com.example.search.screens.home

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

import coil.compose.rememberAsyncImagePainter


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(viewModel: SearchViewModel = viewModel()) {

    val textoDigitado by viewModel.textoDigitado.collectAsStateWithLifecycle()
    val listaFiltrada by viewModel.listaFiltrada.collectAsStateWithLifecycle()
//    val isSearching by viewModel.isSearching.collectAsState()

    var textoQuery by rememberSaveable {
        mutableStateOf(
            textoDigitado
        )
    }

    val keyboardController = LocalSoftwareKeyboardController.current

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE  -> {}/* onCreate  */
                Lifecycle.Event.ON_START   -> {}/* onStart   */
                Lifecycle.Event.ON_RESUME  -> {
                    Toast.makeText(context, "estou ON_RESUME", Toast.LENGTH_SHORT).show()}/* onResume  */
                Lifecycle.Event.ON_PAUSE   -> {}/* onPause   */
                Lifecycle.Event.ON_STOP    -> {}/* onStop    */
                Lifecycle.Event.ON_DESTROY -> {}/* onDestroy */
                Lifecycle.Event.ON_ANY     -> {}/* Em qualquer evento */
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Scaffold(
        topBar = {
            SearchBar(
                query = textoQuery,//text showed on SearchBar
                onQueryChange = {
                    textoQuery = it
                    viewModel.onTextChanged(it)
                }, //update the value of searchText
                onSearch = { keyboardController?.hide() }, //the callback to be invoked when the input service triggers the ImeAction.Search action
                active = true, //whether the user is searching or not
                onActiveChange = {}, //the callback to be invoked when this search bar's active state is changed
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))
//                if(isSearching) {
//                    Box(modifier = Modifier.fillMaxSize()) {
//                        CircularProgressIndicator(
//                            modifier = Modifier.align(Alignment.Center)
//                        )
//                    }
//                } else {
//                    LazyColumn {

                if (listaFiltrada.isEmpty()) {
                    MovieListEmptyState()
                } else {
                    LazyVerticalGrid(GridCells.Fixed(2), modifier = Modifier.fillMaxSize()) {

                        items(
                            items = listaFiltrada,
                            key = { item ->
                                // Return a stable + unique key for the item
                                item.idCategory
                            },
                            itemContent = {country ->
                                Image(
                                    painter = rememberAsyncImagePainter(country.strCategoryThumb),
                                    contentDescription = country.strCategoryDescription,
                                    modifier = Modifier.aspectRatio(1f)
                                )
                                Text(
                                    text = "${country.idCategory} - ${country.strCategory}",
                                    style = MaterialTheme.typography.headlineMedium,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                        )
                        //Assim tambem funciona, fora do itemContent = {} acima
//                        { country ->
//                            Image(
//                                painter = rememberAsyncImagePainter(country.strCategoryThumb),
//                                contentDescription = country.strCategoryDescription,
//                                modifier = Modifier.aspectRatio(1f)
//                            )
//                            Text(
//                                text = "${country.idCategory} - ${country.strCategory}",
//                                style = MaterialTheme.typography.headlineMedium,
//                                modifier = Modifier.padding(top = 4.dp)
//                            )
//                        }
                    }
                }
            }
//            }
        }
    ) {

    }
}

@Composable
fun MovieListEmptyState(
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        Text(
            text = "No movies found",
            style = MaterialTheme.typography.titleSmall
        )
        Text(
            text = "Try adjusting your search",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}