package com.github.lexer.injektor

fun checkInjector(injector: Injector): List<ModuleValidationError> {
    EAGER_INJECTION = true
    val result = ArrayList<ModuleValidationError>()
    (injector as ProviderRegistry).providers().forEach {
        try {
            injector.get(it.key)
        } catch (e: Exception) {
            result.add(ModuleValidationError(it.key, e))
        }
    }
    EAGER_INJECTION = false
    return result
}
