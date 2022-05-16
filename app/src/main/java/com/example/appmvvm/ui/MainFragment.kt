package com.example.appmvvm.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.room.Room
import com.bumptech.glide.Glide
import com.example.appmvvm.R
import com.example.appmvvm.databinding.FragmentBinding
import com.example.appmvvm.viewModel.AppState
import com.example.appmvvm.viewModel.MainFragmentViewModel
import com.example.appmvvm.koin.someClass.EnergyConstructor
import com.example.appmvvm.koin.someClass.IEnergy
import com.example.appmvvm.koin.someClass.SomeClass
import com.example.appmvvm.room.dao.WordsDao
import com.example.appmvvm.room.db.DataBase
import com.example.appmvvm.room.entites.Words
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.koin.android.ext.android.inject
import org.koin.android.scope.getOrCreateScope
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinScopeComponent
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope
import java.util.concurrent.Executors

fun <T : View> Fragment.viewById(@IdRes viewId: Int): DelegateViewById<T> {
    return DelegateViewById({view},viewId)
}

class MainFragment : Fragment() , KoinScopeComponent {
    private var _binding : FragmentBinding? = null
    private val binding : FragmentBinding get() = _binding!!

    private val energy : IEnergy by inject(named("B"))
    private val energyConstructor : EnergyConstructor by inject { parametersOf("stroke777") }
    private val viewModel : MainFragmentViewModel by viewModel()

    private val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        System.out.println("ERROR$throwable")
    }

    private val scopeCoroutine = CoroutineScope(Dispatchers.IO + exceptionHandler + SupervisorJob())
    private var job1 : Job? = null
    private var job2 : Job? = null

    private lateinit var room : DataBase
    private lateinit var wordsDao : WordsDao

    private val scopeA : Scope by lazy { getKoin().getOrCreateScope(this.toString(), named("scope_A"))}
    override val scope: Scope by getOrCreateScope()

    private val someClass : SomeClass by inject()

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

        koinScope()
        delegate()
    }

    private val view : TextView by viewById(R.id.textViewTime1)

    @SuppressLint("SetTextI18n")
    private fun delegate() {
        val textView = view
        textView.text = "some Text"
    }

    private fun koinScope(){
        scopeA.get<SomeClass>().out()
        someClass.out()
    }

    private fun timers() = with(binding){
        start1.setOnClickListener {
            job1 = scopeCoroutine.launch {
                getFlow().collect {
                    withContext(Dispatchers.Main) {
                        binding.textViewTime1.text = it.toString()
                    }
                }
            }
        }

        start2.setOnClickListener {
            job2 = scopeCoroutine.launch {
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

                scopeCoroutine.launch {
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
        scope.close()
    }

    companion object {
        fun newInstance() = MainFragment()
    }
}

