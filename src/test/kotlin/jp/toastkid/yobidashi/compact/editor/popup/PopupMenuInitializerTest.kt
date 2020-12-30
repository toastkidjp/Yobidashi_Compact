package jp.toastkid.yobidashi.compact.editor.popup

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.unmockkAll
import io.mockk.verify
import jp.toastkid.yobidashi.compact.editor.MenuCommand
import kotlinx.coroutines.channels.Channel
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import javax.swing.JMenuItem
import javax.swing.JPopupMenu

internal class PopupMenuInitializerTest {

    private lateinit var popupMenuInitializer: PopupMenuInitializer

    @MockK
    private lateinit var popupMenu: JPopupMenu

    @MockK
    private lateinit var channel: Channel<MenuCommand>

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)

        popupMenuInitializer = PopupMenuInitializer(popupMenu, channel)

        every { popupMenu.add(any<JMenuItem>()) }.answers { mockk() }
    }

    @Test
    fun test() {
        popupMenuInitializer()

        verify(atLeast = 1) { popupMenu.add(any<JMenuItem>()) }
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }
}