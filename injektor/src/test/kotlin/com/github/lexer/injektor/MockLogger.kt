package com.github.lexer.injektor

class MockLogger : InjektorLogger {
    private val logs = ArrayList<Log>();

    override fun log(type: InjektorLogger.MessageType, message: String) {
        logs.add(Log(type, message))
    }

    fun getLogs(): List<Log> {
        return logs
    }

    data class Log(val type: InjektorLogger.MessageType, val message: String)
}