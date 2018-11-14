package com.github.lexer.injektor.validation

import com.github.lexer.injektor.Kontainer

fun checkKontainer(kontainer: Kontainer): List<ModuleValidationError> {
    val result = ArrayList<ModuleValidationError>()
    kontainer.bindings().forEach {
        try {
            kontainer.get(it.key);
        } catch (e: Exception) {
            result.add(ModuleValidationError(it.key, e))
        }
    }
    return result
}
