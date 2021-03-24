package jp.toastkid.yobidashi.compact.view

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import javax.swing.JFrame
import javax.swing.JMenu
import javax.swing.JMenuItem

internal class LookAndFeelMenuViewTest {

    private lateinit var lookAndFeelMenuView: LookAndFeelMenuView

    @MockK
    private lateinit var frameSupplier: () -> JFrame

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)

        lookAndFeelMenuView = LookAndFeelMenuView(frameSupplier)

        every { frameSupplier.invoke() }.answers { mockk() }

        mockkConstructor(JMenu::class)
        every { anyConstructed<JMenu>().setMnemonic(any<Char>()) }.answers { Unit }
        every { anyConstructed<JMenu>().add(any<JMenuItem>()) }.answers { mockk() }
    }

    @Test
    fun test() {
        lookAndFeelMenuView.invoke()

        verify(exactly = 1) { anyConstructed<JMenu>().setMnemonic(any<Char>()) }
        verify(atLeast = 1) { anyConstructed<JMenu>().add(any<JMenuItem>()) }
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

}