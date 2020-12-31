package jp.toastkid.yobidashi.compact.view

import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import javax.swing.JMenuBar
import javax.swing.JMenuItem

internal class MenuBarViewTest {

    private lateinit var menuBarView: MenuBarView

    @BeforeEach
    fun setUp() {
        menuBarView = MenuBarView()

        mockkConstructor(JMenuBar::class)
        every { anyConstructed<JMenuBar>().add(any<JMenuItem>()) }.answers { mockk() }
    }

    @Test
    fun test() {
        menuBarView.invoke(mockk())

        verify (atLeast = 1) { anyConstructed<JMenuBar>().add(any<JMenuItem>()) }
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

}