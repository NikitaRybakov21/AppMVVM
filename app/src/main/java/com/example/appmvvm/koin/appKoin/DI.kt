package koin.appKoin

import com.example.appmvvm.viewModel.MainFragmentViewModel
import koin.main.MainViewModel
import koin.someClass.Energy
import koin.someClass.Energy2
import koin.someClass.EnergyConstructor
import koin.someClass.IEnergy
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

object DI {
    val mainModule = module {
        single<IEnergy>(named("A")) { Energy()  }  // factory каждый раз новый экземпляр
        single<IEnergy>(named("B")) { Energy2() }
        single { (string : String) -> EnergyConstructor(string) }

        viewModel { MainFragmentViewModel() }
    }
}