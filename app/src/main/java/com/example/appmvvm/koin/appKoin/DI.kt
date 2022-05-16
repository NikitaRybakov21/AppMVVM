package com.example.appmvvm.koin.appKoin

import com.example.appmvvm.koin.someClass.*
import com.example.appmvvm.viewModel.MainFragmentViewModel
import com.example.appmvvm.ui.MainFragment
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

object DI {
    val mainModule = module {
        single<IEnergy>(named("A")) { Energy()  }  // factory каждый раз новый экземпляр
        single<IEnergy>(named("B")) { Energy2() }
        single { (string : String) -> EnergyConstructor(string) }

        viewModel { MainFragmentViewModel() }

        scope(named("scope_A")) {
            scoped { SomeClass() }
        }

        scope<MainFragment> {
            scoped { SomeClass() }
        }
    }
}