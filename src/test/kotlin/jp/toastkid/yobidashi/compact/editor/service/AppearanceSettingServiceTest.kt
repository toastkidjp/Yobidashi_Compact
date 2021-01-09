package jp.toastkid.yobidashi.compact.editor.service

import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import io.mockk.verify
import jp.toastkid.yobidashi.compact.editor.MenuCommand
import kotlinx.coroutines.channels.Channel
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.awt.GridBagConstraints
import javax.swing.JComponent
import javax.swing.JOptionPane
import javax.swing.JPanel

internal class AppearanceSettingServiceTest {

    private lateinit var appearanceSettingService: AppearanceSettingService

    @MockK
    private lateinit var channel: Channel<MenuCommand>

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        coEvery { channel.send(any()) }.answers { Unit }

        appearanceSettingService = AppearanceSettingService(channel)

        mockkConstructor(JPanel::class)
        every { anyConstructed<JPanel>().setLayout(any()) }.returns(mockk())
        every { anyConstructed<JPanel>().add(any<JComponent>(), any<GridBagConstraints>()) }.returns(mockk())

        mockkStatic(JOptionPane::class)
        every { JOptionPane.showMessageDialog(any(), any()) }.answers { Unit }
    }

    @Test
    fun test() {
        appearanceSettingService.invoke()

        coVerify (atLeast = 1) { channel.send(any()) }
        verify (atLeast = 1) { anyConstructed<JPanel>().setLayout(any()) }
        verify (atLeast = 1) { anyConstructed<JPanel>().add(any<JComponent>(), any<GridBagConstraints>()) }
        verify (atLeast = 1) { JOptionPane.showMessageDialog(any(), any()) }
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

}