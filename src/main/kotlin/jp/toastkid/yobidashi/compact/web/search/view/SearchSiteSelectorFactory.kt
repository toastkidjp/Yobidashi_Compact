package jp.toastkid.yobidashi.compact.web.search.view

import jp.toastkid.yobidashi.compact.web.search.model.SearchSite
import jp.toastkid.yobidashi.compact.web.search.model.SearchSiteSelectorModel
import java.awt.Dimension
import javax.swing.ComboBoxModel
import javax.swing.JComboBox
import javax.swing.ListCellRenderer

class SearchSiteSelectorFactory(
        private val valuesFactory: () -> Array<SearchSite> = { SearchSite.values() },
        private val modelFactory: () -> ComboBoxModel<SearchSite> = { SearchSiteSelectorModel() },
        private val rendererFactory: () -> ListCellRenderer<SearchSite> = { SearchSiteCellRenderer() }
) {

    operator fun invoke(): JComboBox<SearchSite> {
        return JComboBox(valuesFactory()).also {
            it.model = modelFactory()
            it.renderer = rendererFactory()
            it.preferredSize = Dimension(50, 24)
        }
    }

}