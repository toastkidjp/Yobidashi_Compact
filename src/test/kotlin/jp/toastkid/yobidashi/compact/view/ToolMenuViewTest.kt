package jp.toastkid.yobidashi.compact.view

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.unmockkAll
import io.mockk.verify
import jp.toastkid.yobidashi.compact.service.FileRenameService
import jp.toastkid.yobidashi.compact.service.UnixTimeConverterService
import jp.toastkid.yobidashi.compact.service.UrlEncoderService
import jp.toastkid.yobidashi.compact.service.UrlOpenerService
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import javax.swing.JMenu
import javax.swing.JMenuItem

internal class ToolMenuViewTest {

    @InjectMockKs
    private lateinit var toolMenuView: ToolMenuView

    @MockK
    private lateinit var urlOpenerService: UrlOpenerService

    @MockK
    private lateinit var unixTimeConverterService: UnixTimeConverterService

    @MockK
    private lateinit var urlEncoderService: UrlEncoderService

    @MockK
    private lateinit var fileRenameService: FileRenameService

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)

        mockkConstructor(JMenu::class)
        every { anyConstructed<JMenu>().add(any<JMenuItem>()) }.answers { mockk() }
    }

    @Test
    fun test() {
        toolMenuView.invoke()

        verify(atLeast = 1) { anyConstructed<JMenu>().add(any<JMenuItem>()) }
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }
}