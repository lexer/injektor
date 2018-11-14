package com.github.lexer.injektor

import com.github.lexer.injektor.coffee.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class ValidationTest {
    @Test
    fun checkKontainer_validKontainerWithCompleteGraph_noErrors() {
        val kontainer = Kontainer.create(modules = listOf(LoggerModule(), HeaterModule(), CoffeeMakerModule()))
        assertThat(checkKontainer(kontainer)).isEmpty()
    }

    @Test
    fun checkKontainer_kontainerMissingHeaterModule_classesWontBeResolved() {
        val kontainer = Kontainer.create(modules = listOf(LoggerModule(), CoffeeMakerModule()))
        val errors = checkKontainer(kontainer)
        assertThat(errors[0].unresolvedClass).isEqualTo(CoffeeMaker::class)
        assertThat(errors[1].unresolvedClass).isEqualTo(Pump::class)
    }

}