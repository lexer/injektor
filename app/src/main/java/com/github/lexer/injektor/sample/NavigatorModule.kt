package com.github.lexer.injektor.sample

import android.view.LayoutInflater
import com.github.lexer.injektor.Injector
import com.github.lexer.injektor.Module
import com.github.lexer.injektor.get

class NavigatorModule() : Module() {
    override fun configure(injector: Injector) {
        bind<LayoutInflater> { LayoutInflater.from(injector.get<App>()) }
        bind<Navigator> { Navigator(injector) }.scope("app")
    }
}