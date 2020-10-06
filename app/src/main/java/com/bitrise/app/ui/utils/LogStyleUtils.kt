package com.bitrise.app.ui.utils

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.text.ParcelableSpan
import android.text.Spannable
import android.text.SpannableString
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import androidx.core.content.ContextCompat
import com.bitrise.app.R
import java.util.*

private val ansiRegex by lazy { Regex("\u001B\\[[;\\d]*m") }

fun ansiEscapeToSpannable(context: Context, text: String): Spannable {
    val spannable = SpannableString(text.replace(ansiRegex, ""))
    val stack = Stack<AnsiSpan>()
    val spans = mutableListOf<AnsiSpan>()
    val matches = ansiRegex.findAll(text)
    var offset = 0

    matches.forEach { result ->
        val stringCode = result.value
        val start = result.range.last
        val end = result.range.last + 1
        val ansiInstruction = AnsiInstruction(context, stringCode)
        offset += stringCode.length
        if (ansiInstruction.decorationCode == "0" && stack.isNotEmpty()) {
            spans.add(stack.pop().copy(end = end - offset))
        } else {
            val span = AnsiSpan(AnsiInstruction(context, stringCode), start - offset, 0)
            stack.push(span)
        }
    }

    spans.forEach { ansiSpan ->
        ansiSpan.instruction.spans.forEach {
            spannable.setSpan(it, ansiSpan.start, ansiSpan.end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        }
    }

    return spannable
}

data class AnsiSpan(
    val instruction: AnsiInstruction,
    val start: Int,
    val end: Int
)

class AnsiInstruction(context: Context, code: String) {

    val spans: List<ParcelableSpan> by lazy {
        listOfNotNull(
            getSpan(colorCode, context),
            getSpan(decorationCode, context)
        )
    }

    var colorCode: String? = null
        private set

    var decorationCode: String? = null
        private set

    init {
        val colorCodes =
            code
                .substringAfter('[')
                .substringBefore('m')
                .split(';')

        when (colorCodes.size) {
            3 -> {
                colorCode = colorCodes[1]
                decorationCode = colorCodes[2]
            }
            2 -> {
                colorCode = colorCodes[0]
                decorationCode = colorCodes[1]
            }
            1 -> decorationCode = colorCodes[0]
        }
    }
}

fun getSpan(code: String?, context: Context): ParcelableSpan? =
    when (code) {
        "0" -> null
        "1" -> StyleSpan(Typeface.NORMAL)
        "3" -> StyleSpan(Typeface.ITALIC)
        "4" -> UnderlineSpan()
        "30" -> ForegroundColorSpan(Color.BLACK)
        "31" -> ForegroundColorSpan(ContextCompat.getColor(context, R.color.log_red))
        "32" -> ForegroundColorSpan(ContextCompat.getColor(context, R.color.log_green))
        "33" -> ForegroundColorSpan(ContextCompat.getColor(context, R.color.log_yellow))
        "34" -> ForegroundColorSpan(ContextCompat.getColor(context, R.color.log_blue))
        "35" -> ForegroundColorSpan(ContextCompat.getColor(context, R.color.log_purple))
        "36" -> ForegroundColorSpan(ContextCompat.getColor(context, R.color.log_blue_light))
        "37" -> ForegroundColorSpan(ContextCompat.getColor(context, R.color.log_white))
        "40" -> BackgroundColorSpan(Color.BLACK)
        "41" -> BackgroundColorSpan(ContextCompat.getColor(context, R.color.log_red))
        "42" -> BackgroundColorSpan(ContextCompat.getColor(context, R.color.log_green))
        "43" -> BackgroundColorSpan(ContextCompat.getColor(context, R.color.log_yellow))
        "44" -> BackgroundColorSpan(ContextCompat.getColor(context, R.color.log_blue))
        "45" -> BackgroundColorSpan(ContextCompat.getColor(context, R.color.log_red_light))
        "46" -> BackgroundColorSpan(ContextCompat.getColor(context, R.color.log_blue_light))
        "47" -> BackgroundColorSpan(ContextCompat.getColor(context, R.color.log_white))
        else -> null
    }