package com.github.lexer.injektor.validation

import com.github.lexer.injektor.Module

fun validateModule(module: Module): List<ModuleValidationError> {
    val kontainer = ValidatorKontainer()

    recursivelyInitializeDependencies(module, kontainer)

    return  kontainer.resolveAll()
}

private fun recursivelyInitializeDependencies(module: Module, kontainer: ValidatorKontainer) {
    module.dependencies().forEach {
        it.initialize(kontainer)
        recursivelyInitializeDependencies(it, kontainer)
    }

    module.initialize(kontainer)
}
