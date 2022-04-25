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
import koin.someClass.EnergyConstructor
import koin.someClass.IEnergy
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named

class MainFragment : Fragment() {
    private var _binding : FragmentBinding? = null
    private val binding : FragmentBinding get() = _binding!!

    private val scope = CoroutineScope(Dispatchers.Unconfined)

    private val energy : IEnergy by inject(named("B"))
    private val energyConstructor : EnergyConstructor by inject { parametersOf("stroke777") }
    private val viewModel : MainFragmentViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        energy.out()
        energyConstructor.out()

        viewModel.getLiveData().observe(viewLifecycleOwner, Observer { data -> render(data) })
        viewModel.sendServer()


        scope.launch {
            val data = sendServer() // suspend fun
            System.out.println("VVV$data")
        }

        val data = scope.async { // возвращает результат
            val data = sendServer() // suspend fun
            "VVV$data + qqqqqq"
        }
        scope.launch {
            System.out.println(data.await())
        }
    }

    private suspend fun sendServer() : Int{
        delay(5000)
        return 4
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