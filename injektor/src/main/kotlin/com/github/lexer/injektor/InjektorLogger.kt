package com.github.lexer.injektor

interface InjektorLogger {
    enum class MessageType {
        DEBUG, WARNING
    }

    fun log(type: MessageType, message: String)
}