package jp.toastkid.yobidashi.compact.view

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.spyk
import io.mockk.unmockkAll
import io.mockk.verify
import jp.toastkid.yobidashi.compact.model.ArticleListTabs
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import javax.swing.JFrame

internal class MainFrameTest {

    private lateinit var mainFrame: MainFrame

    private lateinit var frame: JFrame

    @MockK
    private lateinit var tabs: ArticleListTabs

    @MockK
    private lateinit var articleListView: ArticleListView

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        every { tabs.add(any()) }.returns(true)
        every { tabs.get(any()) }.returns(articleListView)
        every { articleListView.sortBy(any()) }.answers { Unit }

        frame = spyk(JFrame("test"))
        every { frame.setVisible(any()) }.answers { Unit }
        mainFrame = MainFrame("test", tabs, frame)
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun testShow() {
        mainFrame.show()

        verify(exactly = 1) { tabs.add(any()) }
        verify(exactly = 1) { articleListView.sortBy(any()) }
        verify(exactly = 1) { frame.setVisible(any()) }
    }

}