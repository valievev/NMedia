package ru.netology.nmedia.objects

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewTreeObserver.OnWindowFocusChangeListener
import android.view.inputmethod.InputMethodManager

object AndroidUtils {
    @SuppressLint("ServiceCast")
    fun hideKeyboard(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun showKeyboard(view: View) {
        view.requestFocus()
        if (view.hasWindowFocus()) {
            showKeyboardNow(view)
        } else {
            view.viewTreeObserver.addOnWindowFocusChangeListener(object : OnWindowFocusChangeListener {
                override fun onWindowFocusChanged(hasFocus: Boolean) {
                    if (hasFocus) {
                        showKeyboardNow(view)
                        view.viewTreeObserver.removeOnWindowFocusChangeListener(this)
                    }
                }
            })
        }
    }

    private fun showKeyboardNow(view: View) {
        if (!view.isFocused) return

        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }
}