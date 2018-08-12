package com.github.lexer.injektor.sample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.ViewGroup
import com.github.lexer.injektor.Injectable
import com.github.lexer.injektor.Injector
import com.github.lexer.injektor.inject

class MainActivity : AppCompatActivity(), Injectable {
    val navigator: Navigator by inject()

    override val injector: Injector by lazy { (application as App).appInjector }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val rootView = findViewById<ViewGroup>(R.id.root_view)

        navigator.attach(rootView)
        navigator.navigate(MAIN_PAGE)
    }

    override fun onDestroy() {
        super.onDestroy()

        navigator.detach()
    }
}
