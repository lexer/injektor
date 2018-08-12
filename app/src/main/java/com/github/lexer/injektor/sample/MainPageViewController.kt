package com.github.lexer.injektor.sample

import android.view.View
import android.widget.Button
import com.github.lexer.injektor.Injectable
import com.github.lexer.injektor.Injector
import com.github.lexer.injektor.inject

class MainPageViewController(override val injector: Injector) : Injectable, ViewController {
    private lateinit var otherPageButton: Button
    private val navigator : Navigator by inject()

    override fun attach(view: View) {
        otherPageButton = view.findViewById<Button>(R.id.other_page_button)

        otherPageButton.setOnClickListener {
            navigator.navigate(OTHER_PAGE)
        }
    }

    override fun detach() {
    }

}