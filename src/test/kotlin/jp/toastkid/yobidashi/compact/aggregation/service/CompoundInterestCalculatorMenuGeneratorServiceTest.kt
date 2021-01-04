package jp.toastkid.yobidashi.compact.aggregation.service

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockkConstructor
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import javax.swing.JMenuItem

internal class CompoundInterestCalculatorMenuGeneratorServiceTest {

    private lateinit var compoundInterestCalculatorMenuGeneratorService: CompoundInterestCalculatorMenuGeneratorService

    @MockK
    private lateinit var inputService: CompoundInterestCalculationInputService

    @MockK
    private lateinit var calculatorService: CompoundInterestCalculatorService

    @MockK
    private lateinit var aggregationResultTableFactoryService: AggregationResultTableFactoryService

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)

        compoundInterestCalculatorMenuGeneratorService = CompoundInterestCalculatorMenuGeneratorService(
                inputService, calculatorService, aggregationResultTableFactoryService
        )

        mockkConstructor(JMenuItem::class)
        every { anyConstructed<JMenuItem>().addActionListener(any()) }.answers { Unit }
    }

    @Test
    fun test() {
        compoundInterestCalculatorMenuGeneratorService.invoke()

        verify(exactly = 1) { anyConstructed<JMenuItem>().addActionListener(any()) }
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

}