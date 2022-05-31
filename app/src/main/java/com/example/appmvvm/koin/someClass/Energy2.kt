package com.example.appmvvm.koin.someClass

import com.example.appmvvm.koin.someClass.IEnergy

class Energy2(private val string : String = "string2") : IEnergy {

    override fun out(){
        System.out.println(string + "888")
    }

}