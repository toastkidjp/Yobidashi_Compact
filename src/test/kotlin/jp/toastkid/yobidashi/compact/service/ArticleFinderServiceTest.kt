package jp.toastkid.yobidashi.compact.service

import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import io.mockk.verify
import jp.toastkid.yobidashi.compact.SubjectPool
import jp.toastkid.yobidashi.compact.model.Article
import jp.toastkid.yobidashi.compact.view.ArticleListView
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import javax.swing.JComponent
import javax.swing.JOptionPane
import javax.swing.JPanel
import javax.swing.JTextField

internal class ArticleFinderServiceTest {

    private lateinit var articleFinderService: ArticleFinderService

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)

        articleFinderService = ArticleFinderService()

        mockkConstructor(JPanel::class)
        every { anyConstructed<JPanel>().setLayout(any()) }.answers { mockk() }
        every { anyConstructed<JPanel>().add(any<JComponent>()) }.answers { mockk() }

        mockkConstructor(JTextField::class)
        every { anyConstructed<JTextField>().setPreferredSize(any()) }.answers { mockk() }

        mockkConstructor(ArticleListView::class)
        coEvery { anyConstructed<ArticleListView>().add(any<Article>()) }.answers { mockk() }

        mockkConstructor(KeywordSearch::class)
        coEvery { anyConstructed<KeywordSearch>().invoke(any(), any(), any()) }.returns(mutableListOf("test"))

        mockkObject(Article)
        coEvery { Article.withTitle(any()) }.returns(mockk())

        mockkObject(SubjectPool)
        coEvery { SubjectPool.sendSearchResult(any(), any()) }.answers { Unit }
    }

    @Test
    fun testNoneInputCase() {
        mockkStatic(JOptionPane::class)
        every { JOptionPane.showInputDialog(null, any()) }.answers { null }
        every { anyConstructed<JTextField>().getText() }.answers { null }

        articleFinderService.invoke()

        verify (exactly = 1) { anyConstructed<JPanel>().setLayout(any()) }
        verify (atLeast = 1) { anyConstructed<JPanel>().add(any<JComponent>()) }
        verify (exactly = 1) { anyConstructed<JTextField>().setPreferredSize(any()) }

        verify (exactly = 0) { anyConstructed<ArticleListView>().add(any<Article>()) }
        verify (exactly = 0) { anyConstructed<KeywordSearch>().invoke(any(), any(), any()) }
        verify (exactly = 0) { Article.withTitle(any()) }
        verify (exactly = 0) { SubjectPool.sendSearchResult(any(), any()) }
    }

    @Test
    fun testNormalInputCase() {
        mockkStatic(JOptionPane::class)
        every { JOptionPane.showInputDialog(null, any()) }.answers { "test" }
        every { anyConstructed<JTextField>().getText() }.answers { "any" }
        coEvery { anyConstructed<ArticleListView>().isEmpty() }.returns(false)

        articleFinderService.invoke()

        verify (exactly = 1) { anyConstructed<JPanel>().setLayout(any()) }
        verify (atLeast = 1) { anyConstructed<JPanel>().add(any<JComponent>()) }
        verify (exactly = 1) { anyConstructed<JTextField>().setPreferredSize(any()) }

        coVerify (exactly = 1) { anyConstructed<ArticleListView>().add(any<Article>()) }
        coVerify (exactly = 1) { anyConstructed<KeywordSearch>().invoke(any(), any(), any()) }
        coVerify (exactly = 1) { Article.withTitle(any()) }
        coVerify (exactly = 1) { SubjectPool.sendSearchResult(any(), any()) }
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

}