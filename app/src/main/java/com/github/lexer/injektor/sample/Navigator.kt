package com.github.lexer.injektor.sample

import android.view.LayoutInflater
import android.view.ViewGroup
import com.github.lexer.injektor.Injectable
import com.github.lexer.injektor.Injector
import com.github.lexer.injektor.inject

class Navigator(override val injector: Injector) : Injectable {
    private var rootView : ViewGroup? = null
    private val inflater : LayoutInflater by inject()
    private var currentPageViewController: ViewController? = null

    fun attach(rootView : ViewGroup) {
        this.rootView = rootView
    }

    fun detach() {
        currentPageViewController?.detach()
        rootView?.removeAllViews()
        rootView = null
    }

    fun navigate(page : Page<*>) {
        currentPageViewController?.detach()
        rootView?.removeAllViews()

        val presenter = injector.get(page.viewController)

        val view = inflater.inflate(page.layoutId, rootView, false)
        rootView?.addView(view)
        presenter.attach(view)
    }
}