package jp.toastkid.yobidashi.compact.view

import jp.toastkid.yobidashi.compact.model.Article
import java.awt.Component
import javax.swing.JLabel
import javax.swing.JList
import javax.swing.ListCellRenderer

class ArticleCellRenderer :  ListCellRenderer<Article> {

    override fun getListCellRendererComponent(
            list: JList<out Article>?,
            value: Article?,
            index: Int,
            isSelected: Boolean,
            cellHasFocus: Boolean
    ): Component {
        val label = JLabel()
        label.text = value?.getTitle()
        return label
    }

}