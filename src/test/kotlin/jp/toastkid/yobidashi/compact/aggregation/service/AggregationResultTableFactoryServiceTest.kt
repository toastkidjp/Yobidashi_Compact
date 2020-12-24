package jp.toastkid.yobidashi.compact.aggregation.service

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import io.mockk.verify
import jp.toastkid.yobidashi.compact.aggregation.model.AggregationResult
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class AggregationResultTableFactoryServiceTest {

    private lateinit var aggregationResultTableFactoryService: AggregationResultTableFactoryService

    @MockK
    private lateinit var result: AggregationResult

    @BeforeEach
    fun setUp() {
        aggregationResultTableFactoryService = AggregationResultTableFactoryService()

        MockKAnnotations.init(this)
        every { result.header() }.answers { arrayOf("1", "2") }
        every { result.columnClass(any()) }.answers { Integer::class.java }
        every { result.itemArrays() }.answers { listOf(arrayOf<Any>("first", "second")) }
    }

    @Test
    fun test() {
        aggregationResultTableFactoryService(result)

        verify (exactly = 1) { result.header() }
        verify (exactly = 0) { result.columnClass(any()) }
        verify (exactly = 1) { result.itemArrays() }
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

}