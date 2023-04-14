package com.example.weather_app.utils

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.ViewGroup
import com.example.weather_app.R

object ProgressLoading {

    private var progress: Dialog? = null

    private fun init(context: Context) {
        progress = Dialog(context)
        progress?.setContentView(R.layout.loading_main)
        progress?.setCancelable(false)
        progress?.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        progress?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

    } // init

    fun show(context: Context) {
        init(context)

        if (!(context as Activity).isFinishing) {
            //show dialog
            try {
                progress?.show()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    } // show

    fun isVisible(): Boolean {
        return try {
            progress!!.isShowing
        } catch (e: Exception) {
            false
        }
    } // fun of isVisible

    fun dismiss() {
        progress?.dismiss()
    } // dismiss

}