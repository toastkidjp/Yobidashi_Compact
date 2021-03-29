package jp.toastkid.yobidashi.compact.service

import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import io.mockk.verify
import jp.toastkid.yobidashi.compact.model.Setting
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.nio.file.Paths

class ArticlesReaderServiceTest {

    private lateinit var articlesReaderService: ArticlesReaderService

    @BeforeEach
    fun setUp() {
        mockkObject(Setting)
        every { Setting.articleFolder() }.returns("test")

        mockkStatic(Paths::class)
        every { Paths.get(any<String>()) }.returns(mockk())

        mockkStatic(Files::class)
        every { Files.list(any()) }.returns(mockk())

        articlesReaderService = ArticlesReaderService()
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun invoke() {
        articlesReaderService.invoke()

        verify(exactly = 1) { Setting.articleFolder() }
        verify(exactly = 1) { Paths.get(any<String>()) }
        verify(exactly = 1) { Files.list(any()) }
    }

}