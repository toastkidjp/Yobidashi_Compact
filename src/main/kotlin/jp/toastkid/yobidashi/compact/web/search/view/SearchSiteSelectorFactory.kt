package jp.toastkid.yobidashi.compact.web.search.view

import jp.toastkid.yobidashi.compact.web.search.model.SearchSite
import jp.toastkid.yobidashi.compact.web.search.model.SearchSiteSelectorModel
import java.awt.Dimension
import javax.swing.JComboBox

class SearchSiteSelectorFactory {

    operator fun invoke(): JComboBox<SearchSite> {
        return JComboBox(SearchSite.values()).also {
            it.model = SearchSiteSelectorModel()
            it.renderer = SearchSiteCellRenderer()
            it.preferredSize = Dimension(50, 24)
        }
    }

}