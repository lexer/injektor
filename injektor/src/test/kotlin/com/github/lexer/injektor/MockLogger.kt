package com.github.lexer.injektor

class MockLogger : InjectorLogger {
    private val logs = ArrayList<Log>()

    override fun log(type: InjectorLogger.MessageType, message: String) {
        logs.add(Log(type, message))
    }

    fun getLogs(): List<Log> {
        return logs
    }

    data class Log(val type: InjectorLogger.MessageType, val message: String)
}