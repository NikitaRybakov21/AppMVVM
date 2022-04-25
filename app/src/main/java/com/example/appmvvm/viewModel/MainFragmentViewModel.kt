package com.example.appmvvm.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.appmvvm.model.Model

class MainFragmentViewModel(private val model : Model = Model()) : ViewModel() , InterfaceViewModel {
    private val liveData : MutableLiveData<AppState> = MutableLiveData<AppState>()

    fun getLiveData() = liveData

    override fun sendServer(){
        val data = model.calc().toString()

        //////

        liveData.postValue(AppState.Success(data))
    }
}