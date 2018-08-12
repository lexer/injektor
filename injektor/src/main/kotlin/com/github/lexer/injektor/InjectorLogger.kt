package com.github.lexer.injektor

interface InjectorLogger {
    enum class MessageType {
        DEBUG, WARNING
    }

    fun log(type: MessageType, message: String)
}