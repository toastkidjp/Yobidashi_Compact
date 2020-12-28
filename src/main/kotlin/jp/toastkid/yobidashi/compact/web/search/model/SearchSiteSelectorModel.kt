package jp.toastkid.yobidashi.compact.web.search.model

import javax.swing.ComboBoxModel
import javax.swing.event.ListDataListener

class SearchSiteSelectorModel : ComboBoxModel<SearchSite> {

    private val searchSites = SearchSite.values()

    private var lastSelected = SearchSite.getDefault()

    override fun getSize(): Int {
        return searchSites.size
    }

    override fun getElementAt(index: Int): SearchSite {
        return searchSites[index]
    }

    override fun addListDataListener(l: ListDataListener?) = Unit

    override fun removeListDataListener(l: ListDataListener?) = Unit

    override fun setSelectedItem(anItem: Any?) {
        lastSelected = (anItem as? SearchSite) ?: SearchSite.YAHOO_JAPAN
    }

    override fun getSelectedItem(): Any {
        return lastSelected
    }

}