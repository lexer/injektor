package com.github.lexer.injektor.coffee

interface Heater {
    fun on()
    fun off()
    fun isHot(): Boolean
}