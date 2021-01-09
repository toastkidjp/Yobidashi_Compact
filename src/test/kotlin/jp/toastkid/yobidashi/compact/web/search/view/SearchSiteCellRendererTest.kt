package jp.toastkid.yobidashi.compact.web.search.view

import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import jp.toastkid.yobidashi.compact.web.search.model.SearchSite
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import javax.swing.JList

internal class SearchSiteCellRendererTest {

    private lateinit var searchSiteCellRenderer: SearchSiteCellRenderer

    @MockK
    private lateinit var list: JList<out SearchSite>

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        searchSiteCellRenderer = SearchSiteCellRenderer()
    }

    @Test
    fun test() {
        searchSiteCellRenderer.getListCellRendererComponent(
                list,
                SearchSite.GITHUB,
                0,
                true,
                true
        )
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

}