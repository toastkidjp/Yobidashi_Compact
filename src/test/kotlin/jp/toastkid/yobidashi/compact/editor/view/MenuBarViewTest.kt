package jp.toastkid.yobidashi.compact.editor.view

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.unmockkAll
import io.mockk.verify
import jp.toastkid.yobidashi.compact.editor.MenuCommand
import kotlinx.coroutines.channels.Channel
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import javax.swing.JMenuBar
import javax.swing.JMenuItem

internal class MenuBarViewTest {

    private lateinit var menuBarView: MenuBarView

    @MockK
    private lateinit var channel: Channel<MenuCommand>

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)

        menuBarView = MenuBarView(channel)

        mockkConstructor(JMenuBar::class)
        every { anyConstructed<JMenuBar>().add(any<JMenuItem>()) }.answers { mockk() }
    }

    @Test
    fun test() {
        menuBarView.invoke(mockk())

        verify(atLeast = 1) { anyConstructed<JMenuBar>().add(any<JMenuItem>()) }
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

}