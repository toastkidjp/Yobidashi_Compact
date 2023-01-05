package jp.toastkid.yobidashi.compact.editor.service

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import jp.toastkid.yobidashi.compact.editor.MenuCommand
import jp.toastkid.yobidashi.compact.editor.model.Editing
import jp.toastkid.yobidashi.compact.editor.view.EditorAreaView
import jp.toastkid.yobidashi.compact.service.UrlOpenerService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.nio.file.Path

internal class CommandReceiverServiceTest {

    @InjectMockKs
    private lateinit var commandReceiverService: CommandReceiverService

    @Suppress("unused")
    @MockK
    private lateinit var channel: Channel<MenuCommand>

    @Suppress("unused")
    @MockK
    private lateinit var editorAreaView: EditorAreaView

    @Suppress("unused")
    @MockK
    private lateinit var currentArticle: () -> Path

    @Suppress("unused")
    @MockK
    private lateinit var editing: Editing

    @Suppress("unused")
    @MockK
    private lateinit var resetFrameTitle: () -> Unit

    @Suppress("unused")
    @MockK
    private lateinit var switchFinder: () -> Unit

    @MockK
    private lateinit var close: () -> Unit

    @MockK
    private lateinit var urlOpenerService: UrlOpenerService

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        every { urlOpenerService.invoke(any<String>()) }.answers { Unit }
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun invoke() {
        CoroutineScope(Dispatchers.Unconfined).launch {
            commandReceiverService.invoke()
        }
        Unit
    }

}