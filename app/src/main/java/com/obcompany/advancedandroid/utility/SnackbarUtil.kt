package com.obcompany.advancedandroid.utility

import android.graphics.Color
import android.view.View
import android.widget.TextView
import com.google.android.material.snackbar.ContentViewCallback
import com.google.android.material.snackbar.Snackbar

class SnackbarUtil {
    companion object{
        @JvmStatic
        fun default(view: View, message: String){
            val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
            /*val snackbarView = snackbar.view

            snackbarView.findViewById<TextView>(R.id.snackbar_text).setTextColor(Color.LTGRAY)
            snackbarView.setBackgroundColor(Color.WHITE)*/

            snackbar.show()
        }

        @JvmStatic
        fun action(view: View, message: String, action: String, listener: (View) -> Unit){
            val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
            snackbar.setAction(action,  listener)
            snackbar.show()
        }
    }
}