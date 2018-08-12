package com.github.lexer.injektor.sample

import android.app.Application
import com.github.lexer.injektor.Injector

class App : Application() {
    lateinit var appInjector: Injector

    override fun onCreate() {
        super.onCreate()

        this.appInjector = Injector.create(modules = listOf(
                AppModule(this),
                NavigatorModule(),
                ControllersModule()))
        appInjector.startScope("app")
    }
}