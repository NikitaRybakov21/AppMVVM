package com.example.appmvvm.ui

import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.room.Dao
import androidx.room.Room
import com.bumptech.glide.Glide
import com.bumptech.glide.TransitionOptions
import com.example.appmvvm.databinding.FragmentBinding
import com.example.appmvvm.viewModel.AppState
import com.example.appmvvm.viewModel.MainFragmentViewModel
import com.example.appmvvm.koin.someClass.EnergyConstructor
import com.example.appmvvm.koin.someClass.IEnergy
import com.example.appmvvm.room.dao.WordsDao
import com.example.appmvvm.room.db.DataBase
import com.example.appmvvm.room.entites.Words
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named
import java.util.concurrent.Executors

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

    private lateinit var room : DataBase
    private lateinit var wordsDao : WordsDao

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding){
        super.onViewCreated(view, savedInstanceState)

        room = Room.databaseBuilder(requireContext(), DataBase::class.java,"wordDB").build()
        wordsDao = room.dataBase()

        energy.out()
        energyConstructor.out()

        viewModel.getLiveData().observe(viewLifecycleOwner, Observer { data -> render(data) })
        viewModel.sendServer()

        timers()

        if(savedInstanceState == null){
            createDataDB()
        }
        database()

    }
    private fun timers() = with(binding){
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

    private fun createDataDB(){

        val listWords = listOf(
            Words(word = "Sea",  wordURI = "https://zagge.ru/wp-content/uploads/2018/02/122.jpg"),
            Words(word = "Snow", wordURI = "https://i.mycdn.me/i?r=AzEPZsRbOZEKgBhR0XGMT1RkfQBLI9uh61EZC9_O7XC4SqaKTM5SRkZCeTgDn6uOyic")
        )

        Executors.newSingleThreadExecutor().execute {
            wordsDao.insertWords(listWords)
        }
    }

    private fun database(){

        binding.search.setOnClickListener {
            val s = binding.editText.text.toString()

            Executors.newSingleThreadExecutor().execute {
                val word = wordsDao.getWordName(s)

                scope.launch {
                    withContext(Dispatchers.Main){
                        binding.word.text = word[0].word
                        binding.word.text = word[0].word

                        Glide.with(requireActivity()).load(word[0].wordURI).into(binding.imageView)
                    }
                }

            }
        }
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