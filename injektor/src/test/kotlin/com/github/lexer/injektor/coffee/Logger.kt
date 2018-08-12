package com.github.lexer.injektor.coffee

class Logger {
    val logs = ArrayList<String>()

    init {
        log("logger created")
    }

    fun log(message: String) {
        logs.add(message)
    }

    fun popFirst(): String {
        val first = logs.first()
        logs.remove(first)
        return first
    }
}