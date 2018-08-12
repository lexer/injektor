package com.github.lexer.injektor.sample

import android.app.Application
import com.github.lexer.injektor.Kontainer

class App : Application() {
    lateinit var appKontainer: Kontainer

    override fun onCreate() {
        super.onCreate()

        this.appKontainer = Kontainer.create(modules = AppModule(this))
    }
}