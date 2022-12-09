package jp.toastkid.yobidashi.compact.editor.service

import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.nio.file.Path

internal class ArticleContentLoaderServiceTest {

    @MockK
    private lateinit var article: Path

    @BeforeEach
    fun setUp() {
        mockkStatic(Files::class)
        every { Files.readAllLines(any()) }.answers { listOf("line1", "line2") }
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