package jp.toastkid.yobidashi.compact.service

import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.mockkConstructor
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import io.mockk.verify
import jp.toastkid.yobidashi.compact.editor.EditorFrame
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

internal class CommandLineArgumentServiceTest {

    @InjectMockKs
    private lateinit var commandLineArgumentService: CommandLineArgumentService

    @MockK
    private lateinit var path: Path

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)

        mockkStatic(Paths::class)
        every { Paths.get(any<String>()) }.returns(path)

        mockkStatic(Files::class)
        every { Files.isReadable(any()) }.returns(true)

        mockkConstructor(EditorFrame::class)
        every { anyConstructed<EditorFrame>().load(any<Path>()) }.just(Runs)
        every { anyConstructed<EditorFrame>().show() }.just(Runs)
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun invoke() {
        commandLineArgumentService.invoke(arrayOf("file1", "file2"))

        verify { anyConstructed<EditorFrame>().load(any<Path>()) }
        verify { anyConstructed<EditorFrame>().show() }
    }
}