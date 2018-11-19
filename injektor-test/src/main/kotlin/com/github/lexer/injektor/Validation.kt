package com.github.lexer.injektor


fun checkInjector(injector: Injector): List<ModuleValidationError> {
    EAGER_INITIALIZATION = true
    val result = ArrayList<ModuleValidationError>()
    injector.providers().forEach {
        try {
            injector.get(it.key)
        } catch (e: Exception) {
            result.add(ModuleValidationError(it.key, e))
        }
    }
    EAGER_INITIALIZATION = false
    return result
}
