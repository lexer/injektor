package com.github.lexer.injektor.coffee

class Logger {
    val logs = ArrayList<String>()

    init {
        log("logger created")
    }

    fun log(message: String) {
        System.out.println(message)
        logs.add(message)
    }

    fun popFirst(): String {
        val first = logs.first()
        logs.remove(first)
        return first
    }
}