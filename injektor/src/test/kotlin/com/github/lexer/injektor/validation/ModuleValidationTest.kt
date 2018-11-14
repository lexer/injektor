package com.github.lexer.injektor.validation

import com.github.lexer.injektor.Kontainer
import com.github.lexer.injektor.coffee.CoffeeMakerModule
import com.github.lexer.injektor.coffee.Heater
import com.github.lexer.injektor.coffee.HeaterModule
import com.github.lexer.injektor.coffee.LoggerModule
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class ModuleValidationTest {
    @Test
    fun checkKontainer_validKontainerWithCompleteGraph_noErrors() {
        val kontainer = Kontainer.create(modules = listOf(LoggerModule(), HeaterModule(), CoffeeMakerModule()))
        assertThat(checkKontainer(kontainer)).isEmpty()
    }

    @Test
    fun checkKontainer_kontainerMissingHeaterModule_noErrors() {
        val kontainer = Kontainer.create(modules = listOf(LoggerModule(), CoffeeMakerModule()))
        val errors = checkKontainer(kontainer)
        assertThat(errors[0].unresolvedClass).isEqualTo(Heater::class)
    }

}