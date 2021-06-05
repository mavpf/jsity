package com.example.jobsity.index

import ShowIndexApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jobsity.network.ShowIndex
import kotlinx.coroutines.launch

enum class ShowIndexStatus { LOADING, ERROR, DONE }
enum class ShowNameStatus { LOADING, ERROR, DONE }


class IndexViewModel : ViewModel() {

    private var _indexPage = 0

    fun indexPage(): Int {
        return _indexPage
    }

    fun indexPageDecrease() {
        _indexPage = _indexPage.dec()
    }

    fun indexPageIncrease() {
        _indexPage = _indexPage.inc()
    }

    private val _showIndexLiveData = MutableLiveData<List<ShowIndex>>()
    val showIndexLiveData: LiveData<List<ShowIndex>> = _showIndexLiveData

    private val _showNameStatus = MutableLiveData<ShowNameStatus>()
    private val _showIndexStatus = MutableLiveData<ShowIndexStatus>()


    init {
        getShowIndex(_indexPage)
    }

    fun getShowIndex(
        page: Int
    ) {
        viewModelScope.launch {
            ShowIndexStatus.LOADING
            try {
                _showIndexLiveData.value = ShowIndexApi.retrofitService.getIndex(page)
                _showIndexStatus.value = ShowIndexStatus.DONE
            } catch (e: Exception) {
                _showIndexStatus.value = ShowIndexStatus.ERROR
                _showIndexLiveData.value = listOf()
            }
        }
    }

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