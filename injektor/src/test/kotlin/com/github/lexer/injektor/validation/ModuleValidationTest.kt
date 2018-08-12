package com.github.lexer.injektor.validation

import com.github.lexer.injektor.coffee.CoffeeMakerModule
import com.github.lexer.injektor.coffee.Heater
import com.github.lexer.injektor.coffee.HeaterModule
import com.github.lexer.injektor.coffee.LoggerModule
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class ModuleValidationTest {
    @Test
    fun validateModule_validModuleWithoutDepedencies_noErrors() {
        assertThat(validateModule(LoggerModule())).isEmpty()
    }

    @Test
    fun validateModule_validModuleWithSingleDepedency_noErrors() {
        assertThat(validateModule(HeaterModule())).isEmpty()
    }

    @Test
    fun validateModule_validModuleWithRecursiveDepedencies_noErrors() {
        assertThat(validateModule(CoffeeMakerModule())).isEmpty()
    }

    @Test
    fun validateModule_invalidModuleWithUndeclaredDependency_singleUnresolvedDependency() {
        val errors = validateModule(HeaterModuleWithoutCorrectDepedency())
        assertThat(errors[0].unresolvedClass).isEqualTo(Heater::class)
    }
}