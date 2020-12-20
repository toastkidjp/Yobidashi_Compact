package jp.toastkid.yobidashi.compact.web.search.model

import javax.swing.ComboBoxModel
import javax.swing.event.ListDataListener

class SearchSiteSelectorModel : ComboBoxModel<SearchSite> {

    private val searchSites = SearchSite.values()

    // TODO impl getDefault()
    private var lastSelected = SearchSite.YAHOO_JAPAN

    override fun getSize(): Int {
        return searchSites.size
    }

    override fun getElementAt(index: Int): SearchSite {
        return searchSites[index]
    }

    override fun addListDataListener(l: ListDataListener?) {
        // NOOP
    }

    override fun removeListDataListener(l: ListDataListener?) {
        // NOOP
    }

    override fun setSelectedItem(anItem: Any?) {
        lastSelected = (anItem as? SearchSite) ?: SearchSite.YAHOO_JAPAN
    }

    override fun getSelectedItem(): Any {
        return lastSelected
    }

}