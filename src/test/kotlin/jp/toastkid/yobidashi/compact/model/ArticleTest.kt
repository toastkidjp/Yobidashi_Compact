package jp.toastkid.yobidashi.compact.model

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File
import java.nio.file.Path
import java.nio.file.Paths

internal class ArticleTest {

    @MockK
    private lateinit var path: Path

    @MockK
    private lateinit var file: File

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        every { path.toFile() }.returns(file)
        every { file.getName() }.returns("test.md")

        mockkStatic(Paths::class)
        every { Paths.get(any(), any()) }.returns(path)
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun testMakeWith() {
        Article.withTitle("test.md")

        verify(exactly = 1) { Paths.get(any(), any()) }
    }

}