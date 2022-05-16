package com.example.appmvvm.koin.someClass

import com.example.appmvvm.koin.someClass.IEnergy

class Energy(private val string : String = "string2") : IEnergy {

    override fun out(){
        System.out.println(string)
    }

}
