package com.example.appmvvm.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.appmvvm.databinding.FragmentBinding
import com.example.appmvvm.viewModel.AppState
import com.example.appmvvm.viewModel.MainFragmentViewModel
import com.example.appmvvm.koin.someClass.EnergyConstructor
import com.example.appmvvm.koin.someClass.IEnergy
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named

class MainFragment : Fragment() {
    private var _binding : FragmentBinding? = null
    private val binding : FragmentBinding get() = _binding!!

    private val energy : IEnergy by inject(named("B"))
    private val energyConstructor : EnergyConstructor by inject { parametersOf("stroke777") }
    private val viewModel : MainFragmentViewModel by viewModel()

    private val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        System.out.println("ERROR$throwable")
    }

    private val scope = CoroutineScope(Dispatchers.IO + exceptionHandler + SupervisorJob())
    private var job1 : Job? = null
    private var job2 : Job? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding){
        super.onViewCreated(view, savedInstanceState)

        energy.out()
        energyConstructor.out()

        viewModel.getLiveData().observe(viewLifecycleOwner, Observer { data -> render(data) })
        viewModel.sendServer()

        textEdit()

        start1.setOnClickListener {
            job1 = scope.launch {
                getFlow().collect {
                    withContext(Dispatchers.Main) {
                        binding.textViewTime1.text = it.toString()
                    }
                }
            }
        }

        start2.setOnClickListener {
            job2 = scope.launch {
                getFlow().collect {
                    withContext(Dispatchers.Main) {
                        binding.textViewTime2.text = it.toString()
                    }
                }
            }
        }

        stop1.setOnClickListener { job1?.cancel() }
        stop2.setOnClickListener { job2?.cancel() }
    }


    private fun getFlow(): Flow<Int> {
        var i = 0
        val flow = flow {
            while (true) {
                timer()
                ++i
                emit(i)
            }
        }
        return flow
    }

    private suspend fun timer() : Int{
        delay(1000)
        return 4
    }

    private fun textEdit(){

    }

    private fun render(data : AppState){
        when(data){
            is AppState.Success -> { Toast.makeText(requireContext(), data.dataString,Toast.LENGTH_SHORT ).show() }
            is AppState.Error   -> { Toast.makeText(requireContext(), "Error",Toast.LENGTH_SHORT ).show()     }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        fun newInstance() = MainFragment()
    }
}