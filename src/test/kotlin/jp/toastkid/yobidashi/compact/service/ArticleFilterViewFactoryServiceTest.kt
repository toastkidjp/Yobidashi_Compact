package jp.toastkid.yobidashi.compact.service

import io.mockk.every
import io.mockk.mockkConstructor
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import javax.swing.JTextField

internal class ArticleFilterViewFactoryServiceTest {

    private lateinit var articleFilterViewFactoryService: ArticleFilterViewFactoryService

    @BeforeEach
    fun setUp() {
        articleFilterViewFactoryService = ArticleFilterViewFactoryService()

        mockkConstructor(JTextField::class)
        every { anyConstructed<JTextField>().addKeyListener(any()) }.answers { Unit }
        every { anyConstructed<JTextField>().setPreferredSize(any()) }.answers { Unit }
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun invoke() {
        articleFilterViewFactoryService.invoke {  }

        verify(exactly = 1) { anyConstructed<JTextField>().addKeyListener(any()) }
        verify(exactly = 1) { anyConstructed<JTextField>().setPreferredSize(any()) }
    }

}