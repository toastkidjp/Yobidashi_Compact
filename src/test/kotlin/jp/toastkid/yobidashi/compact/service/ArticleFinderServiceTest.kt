package jp.toastkid.yobidashi.compact.service

import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import javax.swing.JComponent
import javax.swing.JOptionPane
import javax.swing.JPanel
import javax.swing.JTextField

internal class ArticleFinderServiceTest {

    private lateinit var articleFinderService: ArticleFinderService

    @BeforeEach
    fun setUp() {
        articleFinderService = ArticleFinderService()

        mockkConstructor(JPanel::class)
        every { anyConstructed<JPanel>().setLayout(any()) }.answers { mockk() }
        every { anyConstructed<JPanel>().add(any<JComponent>()) }.answers { mockk() }

        mockkConstructor(JTextField::class)
        every { anyConstructed<JTextField>().setPreferredSize(any()) }.answers { mockk() }

        /*
        val keyword = JOptionPane.showInputDialog(null, panel)
        if (keyword.isNullOrBlank() && fileFilter.text.isNullOrBlank()) {
            return
        }
         */
    }

    @Test
    fun testNullInputCase() {
        mockkStatic(JOptionPane::class)
        every { JOptionPane.showInputDialog(null, any()) }.answers { null }

        articleFinderService.invoke()

        verify (exactly = 1) { anyConstructed<JPanel>().setLayout(any()) }
        verify (atLeast = 1) { anyConstructed<JPanel>().add(any<JComponent>()) }
        verify (exactly = 1) { anyConstructed<JTextField>().setPreferredSize(any()) }
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

}