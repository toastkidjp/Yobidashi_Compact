package jp.toastkid.yobidashi.compact.service

import jp.toastkid.yobidashi.compact.model.OutgoAggregationResult
import java.awt.Dimension
import java.awt.Font
import javax.swing.JComponent
import javax.swing.JScrollPane
import javax.swing.JTable
import javax.swing.table.DefaultTableModel

class OutgoAggregationResultTableContentFactoryService {

    operator fun invoke(aggregationResult: OutgoAggregationResult): JComponent {
        val tableModel = object : DefaultTableModel(arrayOf("Date", "Item", "Price"), 0) {
            override fun getColumnClass(columnIndex: Int): Class<*> {
                return when (columnIndex) {
                    2 -> Integer::class.java
                    else -> String::class.java
                }
            }
        }
        val table = JTable(tableModel)
        setAppropriateTextAppearance(table)

        table.autoCreateRowSorter = true

        aggregationResult.makeItemArrays().forEach { tableModel.addRow(it) }

        return JScrollPane(table).also {
            it.preferredSize = Dimension(600, 400)
        }
    }

    private fun setAppropriateTextAppearance(table: JTable) {
        val font = Font(Font.SANS_SERIF, Font.PLAIN, 16)
        table.tableHeader?.font = font
        table.font = font
        table.rowHeight = 36
    }

}