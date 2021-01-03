package jp.toastkid.yobidashi.compact.web.search

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import io.mockk.verify
import jp.toastkid.yobidashi.compact.service.UrlOpenerService
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.net.URI
import javax.swing.JOptionPane

internal class WebSearchServiceTest {

    private lateinit var webSearchService: WebSearchService

    @MockK
    private lateinit var urlOpenerService: UrlOpenerService

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)

        webSearchService = WebSearchService(urlOpenerService)

        every { urlOpenerService.invoke(any<URI>()) }.answers { Unit }
    }

    @Test
    fun test() {
        mockkStatic(JOptionPane::class)
        every { JOptionPane.showInputDialog(any()) }.answers { "test" }

        webSearchService()

        verify(exactly = 1) { JOptionPane.showInputDialog(any()) }
        verify(exactly = 1) { urlOpenerService.invoke(any<URI>()) }
    }

    @Test
    fun testEmptyInputCase() {
        mockkStatic(JOptionPane::class)
        every { JOptionPane.showInputDialog(any()) }.answers { "" }

        webSearchService()

        verify(exactly = 1) { JOptionPane.showInputDialog(any()) }
        verify(exactly = 0) { urlOpenerService.invoke(any<URI>()) }
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

}