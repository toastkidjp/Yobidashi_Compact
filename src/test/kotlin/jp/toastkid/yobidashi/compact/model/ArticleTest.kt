package jp.toastkid.yobidashi.compact.model

import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import io.mockk.verify
import jp.toastkid.yobidashi.compact.editor.service.OpenEditorService
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.attribute.FileTime
import kotlin.io.path.nameWithoutExtension

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

        mockkStatic("kotlin.io.path.PathsKt")
        every { path.nameWithoutExtension }.returns("test")
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

    @Test
    fun testGetTitle() {
        val article = Article.withTitle("test.md")
        assertEquals("test", article.getTitle())

        verify(exactly = 1) { Paths.get(any(), any()) }
    }

    @Test
    fun testOpen() {
        mockkConstructor(OpenEditorService::class)
        every { anyConstructed<OpenEditorService>().invoke(any()) }.just(Runs)

        val article = Article.withTitle("test.md")
        article.open()

        verify(exactly = 1) { Paths.get(any(), any()) }
        verify(exactly = 1) { anyConstructed<OpenEditorService>().invoke(any()) }
    }

    @Test
    fun testOpenErrorCase() {
        mockkConstructor(OpenEditorService::class)
        every { anyConstructed<OpenEditorService>().invoke(any()) }.throws(IOException())

        val article = Article.withTitle("test.md")
        article.open()

        verify(exactly = 1) { Paths.get(any(), any()) }
        verify(exactly = 1) { anyConstructed<OpenEditorService>().invoke(any()) }
    }

    @Test
    fun testCount() {
        mockkStatic(Files::class)
        every { Files.readAllLines(any()) }.returns(listOf("test content", "is containing dummy text です"))

        val article = Article.withTitle("test.md")
        assertEquals(39, article.count())
    }

    @Test
    fun testMakeFile() {
        mockkStatic(Files::class)
        every { Files.createFile(any()) }.returns(path)
        every { Files.write(any(), any<ByteArray>()) }.returns(path)

        Article.withTitle("test.md").makeFile { "test" }

        verify { Files.createFile(any()) }
        verify { Files.write(any(), any<ByteArray>()) }
    }

    @Test
    fun testPath() {
        assertSame(path, Article.withTitle("test.md").path())
    }

    @Test
    fun testLastModified() {
        val fileTime = mockk<FileTime>()
        every { fileTime.toMillis() }.returns(42L)
        mockkStatic(Files::class)
        every { Files.getLastModifiedTime(any()) }.returns(fileTime)

        val article = Article.withTitle("test.md")
        assertEquals(42L, article.lastModified())
    }

    @Test
    fun testLastModifiedThrowingIoException() {
        val fileTime = mockk<FileTime>()
        every { fileTime.toMillis() }.throws(IOException())
        mockkStatic(Files::class)
        every { Files.getLastModifiedTime(any()) }.returns(fileTime)

        val article = Article.withTitle("test.md")
        assertEquals(0L, article.lastModified())
    }

    @Test
    fun testLastModifiedThrowingIoExceptionFromFiles() {
        val fileTime = mockk<FileTime>()
        every { fileTime.toMillis() }.returns(42L)
        mockkStatic(Files::class)
        every { Files.getLastModifiedTime(any()) }.throws(IOException())

        val article = Article.withTitle("test.md")
        assertEquals(0L, article.lastModified())
    }

}