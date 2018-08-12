package com.github.lexer.injektor.sample

import com.github.lexer.injektor.Injector
import com.github.lexer.injektor.Module

class ControllersModule() : Module() {
    override fun configure(injector: Injector) {
        bind<MainPageViewController> { MainPageViewController(injector) }
        bind<OtherPageViewController> { OtherPageViewController(injector) }
    }
}