package jp.toastkid.yobidashi.compact.view

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.unmockkAll
import io.mockk.verify
import jp.toastkid.yobidashi.compact.model.Article
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.awt.Color
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JList
import javax.swing.JPanel

internal class ArticleCellRendererTest {

    private lateinit var articleCellRenderer: ArticleCellRenderer

    @MockK
    private lateinit var list: JList<out Article>

    @MockK
    private lateinit var value: Article

    @BeforeEach
    fun setUp() {
        articleCellRenderer = ArticleCellRenderer()

        MockKAnnotations.init(this)

        every { list.getSelectionBackground() }.answers { Color.BLUE }
        every { list.getSelectionForeground() }.answers { Color.WHITE }

        every { list.getBackground() }.answers { Color.WHITE }
        every { list.getForeground() }.answers { Color.BLACK }

        every { value.getTitle() }.returns("Test")
        every { value.lastModified() }.returns(1610059992L)

        mockkConstructor(JPanel::class)
        every { anyConstructed<JPanel>().setLayout(any()) }.answers { mockk() }
        every { anyConstructed<JPanel>().add(any<JComponent>(), any()) }.answers { mockk() }
        every { anyConstructed<JPanel>().setBackground(any()) }.answers { mockk() }
        every { anyConstructed<JPanel>().setForeground(any()) }.answers { mockk() }
        every { anyConstructed<JPanel>().setBorder(any()) }.answers { mockk() }

        mockkConstructor(JLabel::class)
        every { anyConstructed<JLabel>().setText(any()) }.answers { mockk() }
        every { anyConstructed<JLabel>().setForeground(any()) }.answers { mockk() }
    }

    @Test
    fun getListCellRendererComponent() {
        articleCellRenderer.getListCellRendererComponent(
                list, value, 1, true, true
        )

        verify(atLeast = 1) { list.getSelectionBackground() }
        verify(atLeast = 1) { list.getSelectionForeground() }
        verify(exactly = 0) { list.getBackground() }
        verify(exactly = 0) { list.getForeground() }
        verify(atLeast = 1) { value.getTitle() }
        verify(atLeast = 1) { value.lastModified() }
        verify(atLeast = 1) { anyConstructed<JPanel>().setLayout(any()) }
        verify(atLeast = 1) { anyConstructed<JPanel>().setBackground(Color.BLUE) }
        verify(atLeast = 1) { anyConstructed<JPanel>().setForeground(Color.WHITE) }
        verify(atLeast = 1) { anyConstructed<JPanel>().setBorder(any()) }
        verify(atLeast = 1) { anyConstructed<JLabel>().setText(any()) }
        verify(atLeast = 1) { anyConstructed<JLabel>().setForeground(any()) }
    }


    @Test
    fun getListCellRendererComponentIsNotSelected() {
        articleCellRenderer.getListCellRendererComponent(
            list, value, 1, false, true
        )

        verify(exactly = 0) { list.getSelectionBackground() }
        verify(exactly = 0) { list.getSelectionForeground() }
        verify(atLeast = 1) { list.getBackground() }
        verify(atLeast = 1) { list.getForeground() }
        verify(atLeast = 1) { value.getTitle() }
        verify(atLeast = 1) { value.lastModified() }
        verify(atLeast = 1) { anyConstructed<JPanel>().setLayout(any()) }
        verify(atLeast = 1) { anyConstructed<JPanel>().setBackground(any()) }
        verify(atLeast = 1) { anyConstructed<JPanel>().setForeground(any()) }
        verify(atLeast = 1) { anyConstructed<JPanel>().setBorder(any()) }
        verify(atLeast = 1) { anyConstructed<JLabel>().setText(any()) }
        verify(atLeast = 1) { anyConstructed<JLabel>().setForeground(any()) }
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

}