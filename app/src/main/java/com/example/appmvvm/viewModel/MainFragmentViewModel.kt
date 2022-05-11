package com.example.appmvvm.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.appmvvm.model.Model
import kotlinx.coroutines.*

class MainFragmentViewModel(private val model : Model = Model()) : ViewModel() , InterfaceViewModel {
    private val liveData : MutableLiveData<AppState> = MutableLiveData<AppState>()

    private val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        System.out.println("ERROR$throwable")
    }
    private val scope = CoroutineScope(Dispatchers.IO + exceptionHandler + SupervisorJob())

    fun getLiveData() = liveData

    override fun sendServer(){

        scope.launch {
            val data = model.calc().toString()
            liveData.postValue(AppState.Success(data))
        }
    }
}