package ru.netology.nmedia

import android.icu.text.DecimalFormat
import kotlin.math.floor

class CountersFormatting {

    fun toShorted(number: Int): String {

        return when {

            number in 1_000..<10_000 -> {
                val format = DecimalFormat("0.#")
                val newNumber = format.format(floor((number.toDouble() / 1_000) * 10) / 10) + "K"
                newNumber
            }

            number in 10_000..<1_000_000 -> {
                val newNumber = (number.toDouble() / 1_000).toInt().toString() + "K"
                newNumber
            }

            number >= 1_000_000 -> {
                val format = DecimalFormat("0.#")
                val newNumber = format.format(floor((number.toDouble() / 1_000_000) * 10) / 10) + "M"
                newNumber
            }

            else -> {
                number.toString()
            }
        }
    }
}