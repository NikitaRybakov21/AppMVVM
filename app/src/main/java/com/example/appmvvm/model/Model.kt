package com.example.appmvvm.model

class Model : InterfaceModel{
    override fun calc() : Int{
        Thread.sleep(3000)
        return 5*5 - 6*3
    }
}