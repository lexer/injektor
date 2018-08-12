package com.github.lexer.injektor.sample

import android.view.View

interface ViewController {
    fun attach(view : View)
    fun detach()
}