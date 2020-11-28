package jp.toastkid.yobidashi.compact.service

import jp.toastkid.yobidashi.compact.model.OutgoAggregationResult
import java.awt.Dimension
import java.awt.Font
import java.awt.ScrollPane
import javax.swing.BoxLayout
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTable
import javax.swing.table.DefaultTableModel


class OutgoAggregationResultTableContentFactoryService {

    operator fun invoke(aggregationResult: OutgoAggregationResult): ScrollPane {
        val tableModel = object : DefaultTableModel(arrayOf("Date", "Item", "Price"), 0) {
            override fun getColumnClass(columnIndex: Int): Class<*> {
                return when (columnIndex) {
                    2 -> Integer::class.java
                    else -> String::class.java
                }
            }
        }
        val table = JTable(tableModel)
        val font = Font(Font.SANS_SERIF, Font.PLAIN, 16)
        table.tableHeader?.font = font
        table.font = font
        table.rowHeight = 36

        table.setAutoCreateRowSorter(true)

        aggregationResult.makeItemArrays().forEach { tableModel.addRow(it) }
        val scrollPane = ScrollPane().also {
            it.add(
                    JPanel().also {
                        it.layout = BoxLayout(it, BoxLayout.PAGE_AXIS)
                        it.add(table.tableHeader)
                        it.add(table)
                    }
            )
            it.preferredSize = Dimension(600, 400)
        }
        val contentPanel = JPanel()
        contentPanel.layout = BoxLayout(contentPanel, BoxLayout.PAGE_AXIS)
        contentPanel.add(JLabel())
        contentPanel.add(scrollPane)

        return scrollPane
    }
}