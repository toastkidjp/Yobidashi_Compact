package jp.toastkid.yobidashi.compact.service

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import javax.swing.JComponent
import javax.swing.JTabbedPane

internal class TabAdderServiceTest {

    @InjectMockKs
    private lateinit var tabAdderService: TabAdderService

    @MockK
    private lateinit var tabPane: JTabbedPane

    @MockK
    private lateinit var closerTabComponentFactoryService: CloserTabComponentFactoryService

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)

        every { tabPane.add(any<JComponent>()) }.answers { mockk() }
        every { tabPane.indexOfComponent(any<JComponent>()) }.answers { 0 }
        every { tabPane.setTabComponentAt(any(), any<JComponent>()) }.answers { mockk() }
        every { tabPane.setSelectedIndex(any()) }.answers { mockk() }
        every { closerTabComponentFactoryService.invoke(any(), any()) }.answers { mockk() }
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun invoke() {
        tabAdderService.invoke(mockk(), "tab1")

        verify(exactly = 1) { tabPane.add(any<JComponent>()) }
        verify(exactly = 1) { tabPane.indexOfComponent(any<JComponent>()) }
        verify(exactly = 1) { tabPane.setTabComponentAt(any(), any<JComponent>()) }
        verify(exactly = 1) { tabPane.setSelectedIndex(any()) }
        verify(exactly = 1) { closerTabComponentFactoryService.invoke(any(), any()) }
    }

}