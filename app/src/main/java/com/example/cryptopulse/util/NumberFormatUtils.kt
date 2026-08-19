package com.example.cryptopulse.util

import java.text.NumberFormat
import java.util.Currency
import java.util.Locale
import kotlin.math.abs

object NumberFormatUtils {

    fun formatPrice(price: Double?, currencyCode: String = "USD"): String {
        if (price == null) return "N/A"
        return try {
            val format = NumberFormat.getCurrencyInstance(getLocaleForCurrency(currencyCode))
            format.currency = Currency.getInstance(currencyCode.uppercase())
            when {
                abs(price) < 0.01 -> {
                    format.minimumFractionDigits = 6
                    format.maximumFractionDigits = 8
                }
                abs(price) < 1.0 -> {
                    format.minimumFractionDigits = 4
                    format.maximumFractionDigits = 6
                }
                abs(price) < 1000.0 -> {
                    format.minimumFractionDigits = 2
                    format.maximumFractionDigits = 2
                }
                else -> {
                    format.minimumFractionDigits = 2
                    format.maximumFractionDigits = 2
                }
            }
            format.format(price)
        } catch (e: Exception) {
            "$${String.format(Locale.US, "%.2f", price)}"
        }
    }

    fun formatLargeNumber(value: Double?, currencyCode: String = "USD"): String {
        if (value == null) return "N/A"
        val symbol = getCurrencySymbol(currencyCode)
        return when {
            abs(value) >= 1_000_000_000_000 -> "${symbol}${String.format(Locale.US, "%.2f", value / 1_000_000_000_000)}T"
            abs(value) >= 1_000_000_000 -> "${symbol}${String.format(Locale.US, "%.2f", value / 1_000_000_000)}B"
            abs(value) >= 1_000_000 -> "${symbol}${String.format(Locale.US, "%.2f", value / 1_000_000)}M"
            abs(value) >= 1_000 -> "${symbol}${String.format(Locale.US, "%.2f", value / 1_000)}K"
            else -> "${symbol}${String.format(Locale.US, "%.2f", value)}"
        }
    }

    fun formatPercentage(percentage: Double?): String {
        if (percentage == null) return "N/A"
        val sign = if (percentage >= 0) "+" else ""
        return "${sign}${String.format(Locale.US, "%.2f", percentage)}%"
    }

    fun formatSupply(supply: Double?, symbol: String = ""): String {
        if (supply == null) return "N/A"
        val formatted = when {
            supply >= 1_000_000_000 -> "${String.format(Locale.US, "%.2f", supply / 1_000_000_000)}B"
            supply >= 1_000_000 -> "${String.format(Locale.US, "%.2f", supply / 1_000_000)}M"
            supply >= 1_000 -> "${String.format(Locale.US, "%.2f", supply / 1_000)}K"
            else -> String.format(Locale.US, "%.0f", supply)
        }
        return if (symbol.isNotEmpty()) "$formatted $symbol" else formatted
    }

    private fun getCurrencySymbol(currencyCode: String): String {
        return try {
            Currency.getInstance(currencyCode.uppercase()).symbol
        } catch (e: Exception) {
            "$"
        }
    }

    private fun getLocaleForCurrency(currencyCode: String): Locale {
        return when (currencyCode.uppercase()) {
            "USD" -> Locale.US
            "EUR" -> Locale.GERMANY
            "GBP" -> Locale.UK
            "INR" -> Locale("en", "IN")
            else -> Locale.US
        }
    }
}
