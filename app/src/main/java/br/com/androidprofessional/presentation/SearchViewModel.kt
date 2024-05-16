package br.com.androidprofessional.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.androidprofessional.utils.Actors

class SearchViewModel : ViewModel() {

    private var _list = MutableLiveData<List<Actors>>()
    val list: LiveData<List<Actors>>
        get() = _list

    init {
        loadActors()
    }

    fun loadActors() {
        _list.postValue(actorsListData())
    }

    fun performQuery(
        query: String,
    ) {
        val filteredList = ArrayList<Actors>()
        actorsListData().forEach { actors ->
            if (actors.actorName.lowercase().contains(query.lowercase()) || actors.conta.lowercase().contains(query.lowercase())) {
                filteredList.add(Actors(actors.actorName, actors.conta))
            }
        }
        _list.postValue(filteredList)
    }

    private fun actorsListData(): List<Actors> {

        val data = mutableListOf(
            Actors("Java101", "123456"),
            Actors("Spring101", "98989899"),
            Actors("Android", "7777777777"),
            Actors("Spring101", "555555555"),
        )

        val actorsList = ArrayList<Actors>()
        data.forEach {
            actorsList.add(Actors(it.actorName, it.conta))
        }

        return actorsList
    }
}