package com.github.lexer.injektor

internal class NoOpLogger : InjektorLogger {
    override fun log(type: InjektorLogger.MessageType, message: String) {

    }
}