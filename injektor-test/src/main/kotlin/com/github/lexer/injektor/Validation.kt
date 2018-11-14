package com.github.lexer.injektor


fun checkKontainer(kontainer: Kontainer): List<ModuleValidationError> {
    EAGER_INITIALIZATION = true
    val result = ArrayList<ModuleValidationError>()
    kontainer.bindings().forEach {
        try {
            kontainer.get(it.key)
        } catch (e: Exception) {
            result.add(ModuleValidationError(it.key, e))
        }
    }
    EAGER_INITIALIZATION = false
    return result
}
