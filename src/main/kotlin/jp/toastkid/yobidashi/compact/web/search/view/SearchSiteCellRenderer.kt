package jp.toastkid.yobidashi.compact.web.search.view

import jp.toastkid.yobidashi.compact.web.search.model.SearchSite
import java.awt.Component
import javax.swing.JLabel
import javax.swing.JList
import javax.swing.ListCellRenderer

class SearchSiteCellRenderer : ListCellRenderer<SearchSite> {

    override fun getListCellRendererComponent(
            list: JList<out SearchSite>?,
            value: SearchSite?,
            index: Int,
            isSelected: Boolean,
            cellHasFocus: Boolean
    ): Component {
        return JLabel(value?.siteName)
    }

}