package com.example.appmvvm.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.appmvvm.databinding.FragmentBinding
import com.example.appmvvm.viewModel.AppState
import com.example.appmvvm.viewModel.MainFragmentViewModel

class MainFragment : Fragment() {
    private var _binding : FragmentBinding? = null
    private val binding : FragmentBinding get() = _binding!!

    private val viewModel : MainFragmentViewModel by lazy {
        ViewModelProvider(this).get(MainFragmentViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getLiveData().observe(viewLifecycleOwner, Observer { data -> render(data) })

        viewModel.sendServer()
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