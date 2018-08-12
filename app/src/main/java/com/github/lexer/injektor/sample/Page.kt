package com.github.lexer.injektor.sample

import kotlin.reflect.KClass

data class Page<T : ViewController>(val layoutId : Int, val viewController: KClass<T>)