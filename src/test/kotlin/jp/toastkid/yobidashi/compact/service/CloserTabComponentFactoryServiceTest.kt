package jp.toastkid.yobidashi.compact.service

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.JTabbedPane

internal class CloserTabComponentFactoryServiceTest {

    private lateinit var closerTabComponentFactoryService: CloserTabComponentFactoryService

    @MockK
    private lateinit var tabPane: JTabbedPane

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)

        closerTabComponentFactoryService = CloserTabComponentFactoryService(tabPane)

        mockkConstructor(JPanel::class)
        every { anyConstructed<JPanel>().setLayout(any()) }.answers { Unit }
        every { anyConstructed<JPanel>().add(any<JComponent>()) }.answers { mockk() }
    }

    @Test
    fun test() {
        closerTabComponentFactoryService.invoke(mockk(), "test")

        verify(atLeast = 1) { anyConstructed<JPanel>().setLayout(any()) }
        verify(atLeast = 1) { anyConstructed<JPanel>().add(any<JComponent>()) }
    }

    @AfterEach
    fun tearDown() {
    }

}