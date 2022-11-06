package com.example.homepage.storeTab.Model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.homepage.storeTab.Repo.StoreRepository

class StoreViewModel : ViewModel() {

    private val repository : StoreRepository = StoreRepository().getInstance()
    private  val _allStores = MutableLiveData<List<Materials>>()
     val allStore :LiveData<List<Materials>> = _allStores

    init{
        repository.loadStore(_allStores)
    }
}