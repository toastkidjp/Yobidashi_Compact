package jp.toastkid.yobidashi.compact.service

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class CloseActionServiceTest {

    private lateinit var closeActionService: CloseActionService

    @MockK
    private lateinit var tabCount: () -> Int

    @MockK
    private lateinit var closeTab: () -> Unit

    @MockK
    private lateinit var closeWindow: () -> Unit

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)

        closeActionService = CloseActionService(tabCount, closeTab, closeWindow)

        every { closeTab.invoke() }.answers { Unit }
        every { closeWindow.invoke() }.answers { Unit }
    }

    @Test
    fun testCloseTab() {
        every { tabCount.invoke() }.answers { 2 }

        closeActionService.invoke()

        verify(exactly = 1) { tabCount.invoke() }
        verify(exactly = 1) { closeTab.invoke() }
        verify(exactly = 0) { closeWindow.invoke() }
    }

    @Test
    fun testCloseWindow() {
        every { tabCount.invoke() }.answers { 1 }

        closeActionService.invoke()

        verify(exactly = 1) { tabCount.invoke() }
        verify(exactly = 0) { closeTab.invoke() }
        verify(exactly = 1) { closeWindow.invoke() }
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

}