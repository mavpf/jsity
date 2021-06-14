package com.example.jobsity.index

import ShowIndexApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jobsity.network.ShowIndex
import kotlinx.coroutines.launch

class IndexViewModel : ViewModel() {

    //Enum class for API status
    enum class ShowIndexStatus { LOADING, ERROR, DONE }
    enum class ShowNameStatus { LOADING, ERROR, DONE }

    //Always start on first page
    private var _indexPage = 0

    //Page getter
    fun indexPage(): Int {
        return _indexPage
    }

    //Page setter up
    fun indexPageDecrease() {
        _indexPage = _indexPage.dec()
    }

    //Page setter down
    fun indexPageIncrease() {
        _indexPage = _indexPage.inc()
    }

    //Livedata from API
    private val _showIndexLiveData = MutableLiveData<List<ShowIndex>>()
    val showIndexLiveData: LiveData<List<ShowIndex>> = _showIndexLiveData

    //Status from API
    private val _showNameStatus = MutableLiveData<ShowNameStatus>()
    private val _showIndexStatus = MutableLiveData<ShowIndexStatus>()
    val showIndexStatus: LiveData<ShowIndexStatus> = _showIndexStatus

    //Teste
    private val _showIndexData: MutableList<ShowIndex> = mutableListOf()
    val showIndexData = _showIndexData


    //Load initial info
    init {
        getShowIndex(_indexPage)
    }

    //Get shows by page from API
    fun getShowIndex(
        page: Int
    ) {
        viewModelScope.launch {
            ShowIndexStatus.LOADING
            try {
                _showIndexData.addAll(ShowIndexApi.retrofitService.getIndex(page))
                _showIndexStatus.value = ShowIndexStatus.DONE
            } catch (e: Exception) {
                _showIndexStatus.value = ShowIndexStatus.ERROR
                _showIndexLiveData.value = listOf()
            }
        }
    }

    //Get shows by name from API
    fun getShowNames(
        name: String
    ) {
        viewModelScope.launch {
            ShowNameStatus.LOADING
            try {
                val searchList: MutableList<ShowIndex> = mutableListOf()

                ShowIndexApi.retrofitService.getShowNames(name).forEach {
                    searchList.add(it.show)
                }
                _showIndexLiveData.value = searchList
                _showNameStatus.value = ShowNameStatus.DONE
            } catch (e: Exception) {
                _showNameStatus.value = ShowNameStatus.ERROR
                _showIndexLiveData.value = listOf()
            }
        }
    }
}