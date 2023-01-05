package jp.toastkid.yobidashi.compact.editor.view

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
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
import javax.swing.JComponent
import javax.swing.JMenu
import javax.swing.JMenuItem

internal class EditMenuViewTest {

    @InjectMockKs
    private lateinit var editMenuView: EditMenuView

    @MockK
    private lateinit var channel: Channel<MenuCommand>

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)

        mockkConstructor(JMenu::class)
        every { anyConstructed<JMenu>().add(any<JComponent>()) }.returns(mockk())
        every { anyConstructed<JMenu>().addSeparator() }.answers {  }

        mockkConstructor(JMenuItem::class)
        every { anyConstructed<JMenuItem>().setAccelerator(any()) }.returns(mockk())
        every { anyConstructed<JMenuItem>().addActionListener(any()) }.returns(mockk())
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun testInvoke() {
        editMenuView.invoke()

        verify(atLeast = 1) { anyConstructed<JMenu>().addSeparator() }
        verify(atLeast = 1) { anyConstructed<JMenuItem>().accelerator = any() }
        verify(atLeast = 1) { anyConstructed<JMenuItem>().addActionListener(any()) }
    }

}