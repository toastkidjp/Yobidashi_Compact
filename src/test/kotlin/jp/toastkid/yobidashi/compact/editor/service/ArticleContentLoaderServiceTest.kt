package jp.toastkid.yobidashi.compact.editor.service

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import jp.toastkid.yobidashi.compact.model.Article
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.nio.file.Files

internal class ArticleContentLoaderServiceTest {

    @MockK
    private lateinit var article: Article

    @BeforeEach
    fun setUp() {
        mockkStatic(Files::class)
        every { Files.readAllLines(any()) }.answers { listOf("line1", "line2") }

        MockKAnnotations.init(this)
        every { article.path() }.answers { mockk() }
    }

    @Test
    fun test() {
        assertEquals(
                "line1-line2-",
                ArticleContentLoaderService("-").invoke(article)
        )
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

}