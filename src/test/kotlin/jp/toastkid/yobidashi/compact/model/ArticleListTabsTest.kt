package jp.toastkid.yobidashi.compact.model

import io.mockk.mockk
import jp.toastkid.yobidashi.compact.view.ArticleListView
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class ArticleListTabsTest {

    private lateinit var articleListTabs: ArticleListTabs

    @BeforeEach
    fun setUp() {
        articleListTabs = ArticleListTabs()
    }

    @Test
    fun test() {
        assertThrows<IndexOutOfBoundsException> { articleListTabs.get(0) }

        val mockTab = mockk<ArticleListView>()
        articleListTabs.add(mockTab)
        assertEquals(mockTab, articleListTabs.get(0))
    }

}