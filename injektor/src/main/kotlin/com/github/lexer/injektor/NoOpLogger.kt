package com.github.lexer.injektor

internal class NoOpLogger : InjectorLogger {
    override fun log(type: InjectorLogger.MessageType, message: String) {

    }
}