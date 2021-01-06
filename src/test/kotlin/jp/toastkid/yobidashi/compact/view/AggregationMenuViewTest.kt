package jp.toastkid.yobidashi.compact.view

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.unmockkAll
import io.mockk.verify
import jp.toastkid.yobidashi.compact.aggregation.service.AggregationMenuItemGeneratorService
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import javax.swing.JMenu
import javax.swing.JMenuItem

internal class AggregationMenuViewTest {

    private lateinit var aggregationMenuView: AggregationMenuView

    @MockK
    private lateinit var aggregationMenuItemGeneratorService: AggregationMenuItemGeneratorService

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)

        aggregationMenuView = AggregationMenuView(aggregationMenuItemGeneratorService)

        every { aggregationMenuItemGeneratorService.invoke(any(), any(), any(), any()) }.answers { mockk() }

        mockkConstructor(JMenu::class)
        every { anyConstructed<JMenu>().add(any<JMenuItem>()) }.answers { mockk() }
    }

    @Test
    fun test() {
        aggregationMenuView.invoke()

        verify(atLeast = 1) { anyConstructed<JMenu>().add(any<JMenuItem>()) }
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

}