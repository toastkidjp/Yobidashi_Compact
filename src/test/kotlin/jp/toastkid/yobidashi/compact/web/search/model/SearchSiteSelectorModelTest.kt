package jp.toastkid.yobidashi.compact.web.search.model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class SearchSiteSelectorModelTest {

    private lateinit var searchSiteSelectorModel: SearchSiteSelectorModel

    @BeforeEach
    fun setUp() {
        searchSiteSelectorModel = SearchSiteSelectorModel()
    }

    @Test
    fun testGetSize() {
        assertEquals(SearchSite.values().size, searchSiteSelectorModel.size)
    }

    @Test
    fun testGetElementAt() {
        assertSame(SearchSite.values()[0], searchSiteSelectorModel.getElementAt(0))
    }

    @Test
    fun testAddListDataListener() {
        searchSiteSelectorModel.addListDataListener(null)
    }

    @Test
    fun testRemoveListDataListener() {
        searchSiteSelectorModel.removeListDataListener(null)
    }

    @Test
    fun testSetSelectedItem() {
        searchSiteSelectorModel.selectedItem = SearchSite.AMAZON

        assertSame(SearchSite.AMAZON, searchSiteSelectorModel.selectedItem)
    }

}