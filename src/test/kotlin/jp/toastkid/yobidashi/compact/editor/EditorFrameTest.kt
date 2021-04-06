package jp.toastkid.yobidashi.compact.editor

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.unmockkAll
import jp.toastkid.yobidashi.compact.editor.model.Editing
import jp.toastkid.yobidashi.compact.editor.view.EditorAreaView
import jp.toastkid.yobidashi.compact.editor.view.MenuBarView
import jp.toastkid.yobidashi.compact.service.UiUpdaterService
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.awt.Font
import javax.swing.JComponent
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JPanel

internal class EditorFrameTest {

    private lateinit var editorFrame: EditorFrame

    @MockK
    private lateinit var frame: JFrame

    @MockK
    private lateinit var editing: Editing

    @MockK
    private lateinit var statusLabel: JLabel

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        every { frame.setIconImage(any()) }.answers { Unit }
        every { frame.getContentPane() }.answers { JPanel() }
        every { frame.setJMenuBar(any()) }.answers { Unit }
        every { frame.setBounds(any(), any(), any(), any()) }.answers { Unit }
        every { frame.setDefaultCloseOperation(any()) }.answers { Unit }
        every { frame.setVisible(any()) }.answers { Unit }

        val font = mockk<Font>()
        every { font.deriveFont(any<Float>()) }.returns(font)
        every { statusLabel.getFont() }.returns(font)
        every { statusLabel.setFont(any()) }.answers { Unit }

        every { editing.setCurrentSize(any()) }.answers { Unit }

        mockkConstructor(MenuBarView::class)
        every { anyConstructed<MenuBarView>().invoke(any()) }.returns(mockk())

        mockkConstructor(EditorAreaView::class)
        every { anyConstructed<EditorAreaView>().find(any()) }.answers { Unit }
        every { anyConstructed<EditorAreaView>().receiveStatus(any()) }.answers { Unit }

        mockkConstructor(UiUpdaterService::class)
        every { anyConstructed<UiUpdaterService>().invoke(any(), any()) }.answers { Unit }

        mockkConstructor(JPanel::class)
        every { anyConstructed<JPanel>().add(any<JComponent>(), any<String>()) }.returns(mockk())

        editorFrame = EditorFrame(frame, editing, statusLabel)
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun load() {
        // TODO
    }

    @Test
    fun show() {
        editorFrame.show()
    }

}