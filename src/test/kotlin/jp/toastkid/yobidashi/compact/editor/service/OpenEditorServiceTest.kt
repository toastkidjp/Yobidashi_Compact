package jp.toastkid.yobidashi.compact.editor.service

import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkAll
import io.mockk.verify
import jp.toastkid.yobidashi.compact.editor.EditorFrame
import jp.toastkid.yobidashi.compact.model.Article
import jp.toastkid.yobidashi.compact.model.Setting
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.awt.Desktop
import java.nio.file.Path

internal class OpenEditorServiceTest {

    private lateinit var openEditorService: OpenEditorService

    @MockK
    private lateinit var editorFrame: EditorFrame

    @MockK
    private lateinit var desktop: Desktop

    @MockK
    private lateinit var article: Article

    @MockK
    private lateinit var path: Path

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        openEditorService = OpenEditorService(editorFrame, desktop)

        every { editorFrame.load(any<Article>()) }.just(Runs)
        every { editorFrame.show() }.answers { Unit }
        every { desktop.open(any()) }.answers { Unit }
        every { path.toFile() }.answers { mockk() }
        every { article.path() }.answers { path }
    }

    @Test
    fun testUseInternalEditor() {
        mockkObject(Setting)
        every { Setting.useInternalEditor() }.returns(true)

        openEditorService.invoke(article)

        verify (exactly = 1) { editorFrame.load(any<Article>()) }
        verify (exactly = 1) { editorFrame.show() }
        verify (exactly = 0)  { desktop.open(any()) }
    }

    @Test
    fun testNotUseInternalEditor() {
        mockkObject(Setting)
        every { Setting.useInternalEditor() }.returns(false)

        openEditorService.invoke(article)

        verify (exactly = 0) { editorFrame.load(any<Article>()) }
        verify (exactly = 0) { editorFrame.show() }
        verify (exactly = 1)  { desktop.open(any()) }
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

}