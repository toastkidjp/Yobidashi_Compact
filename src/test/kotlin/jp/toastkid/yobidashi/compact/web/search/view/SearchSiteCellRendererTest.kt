package jp.toastkid.yobidashi.compact.web.search.view

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.spyk
import io.mockk.unmockkAll
import io.mockk.verify
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
        val spy = spyk(SearchSite.GITHUB)
        every { spy.siteName }.returns("test")

        searchSiteCellRenderer.getListCellRendererComponent(
                list,
                spy,
                0,
                true,
                true
        )

        verify (atLeast = 1) { spy.siteName }
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

}