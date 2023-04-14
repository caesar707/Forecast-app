package com.example.weather_app.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.widget.*
import com.example.weather_app.R
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeoutException

object AppUtils {

    fun Context.showToast(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    @SuppressLint("SimpleDateFormat")
    fun String.formatDate(): String {

        val oldFormat =
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
        val newFormat =
            SimpleDateFormat("dd MMM hh:mm a", Locale.ENGLISH)

        try {
            val date: Date = oldFormat.parse(this)!!
            return newFormat.format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return ""
    }

    private fun Activity.showLongToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

    fun getErrorMessage(error: Throwable): Int {

        return when (error) {
            is ServerErrorException ->
                R.string.errorHappened

            is NoConnection ->
                R.string.no_connection

            is TimeoutException ->
                R.string.time

            is ServerUnreachableException ->
                R.string.unreach

            is BadRequest ->
                R.string.bad

            is NotFound ->
                R.string.notFound

            is ManyRequest ->
                R.string.manyRequest

            else -> R.string.errorHappened
        }
    }
}