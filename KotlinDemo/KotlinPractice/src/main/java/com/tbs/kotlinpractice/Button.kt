package com.tbs.kotlinpractice

open class Button: Clickable, Focusable {

    open fun disable() {

    }

    override fun showOff() {
        super<Clickable>.showOff()
    }

    override fun click() {
        println("l was clicked")
    }
}