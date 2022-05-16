package com.example.appmvvm.ui

import android.view.View
import kotlin.reflect.KProperty

class DelegateViewById<out T : View>(private val view: () -> View?, private val id: Int) {

    operator fun getValue(mainFragment: MainFragment, property: KProperty<*>): T {
        return view()!!.findViewById(id)
    }
}