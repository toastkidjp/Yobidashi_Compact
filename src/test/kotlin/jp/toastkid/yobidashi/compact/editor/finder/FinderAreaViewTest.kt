package jp.toastkid.yobidashi.compact.editor.finder

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.channels.Channel
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import javax.swing.JPanel
import javax.swing.JTextField

internal class FinderAreaViewTest {

    private lateinit var finderAreaView: FinderAreaView

    @MockK
    private lateinit var orderChannel: Channel<FindOrder>

    @MockK
    private lateinit var messageChannel: Channel<String>

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun test() {
        finderAreaView = FinderAreaView(orderChannel, messageChannel)
        assertNotNull(finderAreaView.view())
    }

    @Test
    fun testSwitchVisibility() {
        val textField = mockk<JTextField>()
        every { textField.requestFocus() }.returns(Unit)
        every { textField.setCaretPosition(any()) }.returns(Unit)

        mockkConstructor(JPanel::class)
        every { anyConstructed<JPanel>().getComponent(any()) }.returns(textField)

        finderAreaView = FinderAreaView(orderChannel, messageChannel)
        finderAreaView.switchVisibility()

        verify(exactly = 1) { textField.requestFocus() }
        verify(exactly = 1) { textField.setCaretPosition(any()) }
        verify(exactly = 1) { anyConstructed<JPanel>().getComponent(any()) }
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

}