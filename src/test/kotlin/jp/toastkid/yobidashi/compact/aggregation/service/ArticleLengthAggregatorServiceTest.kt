package jp.toastkid.yobidashi.compact.aggregation.service

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import io.mockk.verify
import jp.toastkid.yobidashi.compact.service.ArticlesReaderService
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.util.stream.Stream

class ArticleLengthAggregatorServiceTest {

    @InjectMockKs
    private lateinit var articleLengthAggregatorService: ArticleLengthAggregatorService

    @MockK
    private lateinit var articlesReaderService: ArticlesReaderService

    @BeforeEach
    fun setUp() {
        val file = mockk<File>()
        every { file.getName() }.returns("file.md")

        val path = mockk<Path>()
        every { path.toFile() }.returns(file)

        mockkStatic(Files::class)
        every { Files.readAllBytes(any()) }.returns("test content".toByteArray())

        MockKAnnotations.init(this)
        every { articlesReaderService.invoke() }.returns(Stream.of(path))
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun invoke() {
        articleLengthAggregatorService.invoke("file")

        verify(exactly = 1) { Files.readAllBytes(any()) }
        verify(exactly = 1) { articlesReaderService.invoke() }
    }

}