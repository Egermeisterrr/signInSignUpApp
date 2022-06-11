package com.example.signinsignup.adapters

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import java.util.*

class DataTextWatcher(private val input: EditText) : TextWatcher {
    private var current = ""
    private val ddmmyyyy = "DDMMYYYY"
    private val cal = Calendar.getInstance()

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
    override fun afterTextChanged(s: Editable) {}

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        if (s.toString() == current) {
            return
        }

        var clean = s.toString().replace("[^\\d.]|\\.".toRegex(), "")
        val cleanC = current.replace("[^\\d.]|\\.".toRegex(), "")
        val cl = clean.length
        var sel = cl
        var i = 2

        while (i <= cl && i < 6) {
            sel++
            i += 2
        }

        if (clean == cleanC) {
            sel--
        }

        if (clean.length < 8) {
            clean += ddmmyyyy.substring(clean.length)
        }
        else {
            var day = clean.substring(0, 2).toInt()
            var mon = clean.substring(2, 4).toInt()
            var year = clean.substring(4, 8).toInt()

            mon = when {
                mon < 1 -> 1
                mon > 12 -> 12
                else -> mon
            }

            cal[Calendar.MONTH] = mon - 1
            year = if (year < 1900) 1900 else if (year > 2100) 2100 else year
            cal[Calendar.YEAR] = year

            day =
                if (day > cal.getActualMaximum(Calendar.DATE)) cal.getActualMaximum(Calendar.DATE)
                else day

            clean = String.format("%02d%02d%02d", day, mon, year)
        }

        clean = String.format(
            "%s.%s.%s", clean.substring(0, 2),
            clean.substring(2, 4),
            clean.substring(4, 8)
        )

        sel =
            if (sel < 0)
                0
            else
                sel

        current = clean
        input.setText(current)
        input.setSelection(

            if (sel < current.length)
                sel
            else
                current.length
        )
    }
}