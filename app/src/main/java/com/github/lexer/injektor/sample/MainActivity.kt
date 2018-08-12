package com.github.lexer.injektor.sample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import lexer.github.com.injektor.R
import com.github.lexer.injektor.Injectable
import com.github.lexer.injektor.Kontainer
import com.github.lexer.injektor.inject

class MainActivity : AppCompatActivity(), Injectable {
    val foo: Foo by inject()

    lateinit var kontainer: Kontainer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val appKontainer = (application as App).appKontainer;
        kontainer = Kontainer.create(appKontainer, MainActivityModule())

        Log.d("qwerty", foo.doSmth())
        Log.d("qwerty", foo.doSmth())

        Log.d("qwerty", kontainer.get(Foo::class).doSmth())
        Log.d("qwerty", kontainer.get(Foo::class).doSmth())
    }

    override fun kontainer(): Kontainer {
        return kontainer
    }

}
