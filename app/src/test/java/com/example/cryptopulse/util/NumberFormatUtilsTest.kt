package com.example.cryptopulse.util

import org.junit.Assert.assertEquals
import org.junit.Test

class NumberFormatUtilsTest {

    @Test
    fun `formatPrice with null returns NA`() {
        assertEquals("N/A", NumberFormatUtils.formatPrice(null))
    }

    @Test
    fun `formatPrice formats large USD price correctly`() {
        val result = NumberFormatUtils.formatPrice(67420.31, "USD")
        // Should contain the number and dollar sign
        assert(result.contains("67") && result.contains("420"))
    }

    @Test
    fun `formatPrice formats small price with more decimals`() {
        val result = NumberFormatUtils.formatPrice(0.00001234, "USD")
        assert(result.contains("1234") || result.contains("0.00001"))
    }

    @Test
    fun `formatLargeNumber with null returns NA`() {
        assertEquals("N/A", NumberFormatUtils.formatLargeNumber(null))
    }

    @Test
    fun `formatLargeNumber formats trillions correctly`() {
        val result = NumberFormatUtils.formatLargeNumber(2_310_000_000_000.0)
        assert(result.contains("2.31") && result.contains("T"))
    }

    @Test
    fun `formatLargeNumber formats billions correctly`() {
        val result = NumberFormatUtils.formatLargeNumber(850_400_000_000.0)
        assert(result.contains("850.40") && result.contains("B"))
    }

    @Test
    fun `formatLargeNumber formats millions correctly`() {
        val result = NumberFormatUtils.formatLargeNumber(42_800_000.0)
        assert(result.contains("42.80") && result.contains("M"))
    }

    @Test
    fun `formatPercentage with null returns NA`() {
        assertEquals("N/A", NumberFormatUtils.formatPercentage(null))
    }

    @Test
    fun `formatPercentage formats positive correctly`() {
        val result = NumberFormatUtils.formatPercentage(4.82)
        assertEquals("+4.82%", result)
    }

    @Test
    fun `formatPercentage formats negative correctly`() {
        val result = NumberFormatUtils.formatPercentage(-3.17)
        assertEquals("-3.17%", result)
    }

    @Test
    fun `formatPercentage formats zero as positive`() {
        val result = NumberFormatUtils.formatPercentage(0.0)
        assertEquals("+0.00%", result)
    }

    @Test
    fun `formatSupply with null returns NA`() {
        assertEquals("N/A", NumberFormatUtils.formatSupply(null))
    }

    @Test
    fun `formatSupply formats millions with symbol`() {
        val result = NumberFormatUtils.formatSupply(19_700_000.0, "BTC")
        assert(result.contains("19.70") && result.contains("M") && result.contains("BTC"))
    }

    @Test
    fun `formatSupply formats billions without symbol`() {
        val result = NumberFormatUtils.formatSupply(1_500_000_000.0)
        assert(result.contains("1.50") && result.contains("B"))
    }
}
