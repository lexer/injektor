package com.github.lexer.injektor.validation

import kotlin.reflect.KClass

data class ModuleValidationError(val unresolvedClass: KClass<*>, val exception: Exception)