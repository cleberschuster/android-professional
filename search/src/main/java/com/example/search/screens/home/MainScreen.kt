package com.example.search.screens.home

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

import coil.compose.rememberAsyncImagePainter


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen() {

    val viewModel = SearchViewModel()

    //Collecting states from ViewModel
    val searchText by viewModel.searchText.collectAsStateWithLifecycle()
//    val isSearching by viewModel.isSearching.collectAsState()
    val countriesList by viewModel.countriesList.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            SearchBar(
                query = searchText,//text showed on SearchBar
                onQueryChange = viewModel::onSearchTextChange, //update the value of searchText
                onSearch = {}, //the callback to be invoked when the input service triggers the ImeAction.Search action
                active = true, //whether the user is searching or not
                onActiveChange = {}, //the callback to be invoked when this search bar's active state is changed
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                LazyColumn {
                    items(countriesList) { country ->
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
//                        Text(
//                            text = country,
//                            modifier = Modifier.padding(
//                                start = 8.dp,
//                                top = 4.dp,
//                                end = 8.dp,
//                                bottom = 4.dp)
//                        )
                    }
                }
            }
        }
    ) {

    }
}