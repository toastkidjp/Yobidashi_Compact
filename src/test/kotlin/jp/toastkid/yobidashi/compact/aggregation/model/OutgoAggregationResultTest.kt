package jp.toastkid.yobidashi.compact.aggregation.model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class OutgoAggregationResultTest {

    private lateinit var outgoAggregationResult: OutgoAggregationResult

    @BeforeEach
    fun setUp() {
        outgoAggregationResult = OutgoAggregationResult("target")
    }

    @Test
    fun testAdd() {
        outgoAggregationResult.add("2021-04-30", "Orange", 100)
        outgoAggregationResult.add("2021-04-30", "Ramen", 211)

        assertEquals(2, outgoAggregationResult.itemArrays().size)
        assertFalse(outgoAggregationResult.isEmpty())
        assertEquals("Total: 311", outgoAggregationResult.resultTitleSuffix())
    }

}