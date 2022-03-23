package com.example.signinsignup

import android.text.Editable
import android.text.TextWatcher
import java.lang.StringBuilder

class PhoneTextWatcher : TextWatcher {
    private var sb = StringBuilder()
    private var ignore = false
    private val numPlace = 'X'

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(editable: Editable) {
        if (!ignore) {
            removeFormat(editable.toString())
            applyFormat(sb.toString())
            ignore = true
            editable.replace(0, editable.length, sb.toString())
            ignore = false
        }
    }

    private fun removeFormat(text: String) {
        sb.setLength(0)
        for (element in text) {
            if (isNumberChar(element)) {
                sb.append(element)
            }
        }
    }

    private fun applyFormat(text: String) {
        val template = getTemplate(text)
        sb.setLength(0)
        var i = 0
        var textIndex = 0
        while (i < template.length && textIndex < text.length) {
            if (template[i] == numPlace) {
                sb.append(text[textIndex])
                textIndex++
            }
            else {
                sb.append(template[i])
            }
            i++
        }
    }

    private fun isNumberChar(c: Char): Boolean {
        return c in '0'..'9'
    }

    private fun getTemplate(text: String): String {

        return when {
            text.startsWith("7") -> {
                "+X (XXX) XXX-XX-XX"
            }
            text.startsWith("375") -> {
                "+XXX (XX) XXX-XX-XX"
            }
            else -> "+XXX (XXX) XX-XX-XX"
        }
    }
}