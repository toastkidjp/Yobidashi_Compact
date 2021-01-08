package jp.toastkid.yobidashi.compact.view

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import javax.swing.JMenu
import javax.swing.JMenuItem

internal class SortMenuViewTest {

    private lateinit var menuView: SortMenuView

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)

        menuView = SortMenuView()

        mockkConstructor(JMenu::class)
        every { anyConstructed<JMenu>().add(any<JMenuItem>()) }.answers { mockk() }
    }

    @Test
    fun test() {
        menuView.invoke()

        verify(atLeast = 1) { anyConstructed<JMenu>().add(any<JMenuItem>()) }
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

}