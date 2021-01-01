package jp.toastkid.yobidashi.compact.aggregation.service

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class CompoundInterestCalculatorServiceTest {

    private lateinit var compoundInterestCalculatorService: CompoundInterestCalculatorService

    @BeforeEach
    fun setUp() {
        compoundInterestCalculatorService = CompoundInterestCalculatorService()
    }

    @Test
    fun test() {
        val result = compoundInterestCalculatorService.invoke(120000, 0.01, 10)
        result.itemArrays().forEach { println(it.contentToString()) }
        val tenYearsLater = result.get(10) ?: fail("This case does not allow null.")
        assertEquals(1212000, tenYearsLater.second)
        assertEquals(1255466, tenYearsLater.third)
    }

}