package jp.toastkid.yobidashi.compact.service

import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import io.mockk.verify
import jp.toastkid.yobidashi.compact.model.Setting
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import javax.swing.JFrame
import javax.swing.SwingUtilities
import javax.swing.UIManager

internal class UiUpdaterServiceTest {

    private lateinit var uiUpdaterService: UiUpdaterService

    @BeforeEach
    fun setUp() {
        mockkStatic(UIManager::class)
        every { UIManager.setLookAndFeel(any<String>()) }.just(Runs)
        mockkObject(Setting)
        every { Setting.setLookAndFeel(any()) }.just(Runs)
        mockkStatic(SwingUtilities::class)
        every { SwingUtilities.updateComponentTreeUI(any()) }.just(Runs)

        uiUpdaterService = UiUpdaterService()
    }

    @Test
    fun test() {
        val frame = mockk<JFrame>()

        uiUpdaterService.invoke(frame, "test")

        verify (exactly = 1) { UIManager.setLookAndFeel("test") }
        verify (exactly = 1) { Setting.setLookAndFeel("test") }
        verify (exactly = 1) { SwingUtilities.updateComponentTreeUI(any()) }
    }

    @Test
    fun testErrorCase() {
        every { SwingUtilities.updateComponentTreeUI(any()) }.throws(RuntimeException())

        val frame = mockk<JFrame>()

        uiUpdaterService.invoke(frame, "test")

        verify (exactly = 1) { UIManager.setLookAndFeel("test") }
        verify (exactly = 1) { Setting.setLookAndFeel("test") }
        verify (exactly = 1) { SwingUtilities.updateComponentTreeUI(any()) }
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

}