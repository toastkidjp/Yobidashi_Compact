package jp.toastkid.yobidashi.compact.service

import java.awt.Dimension
import java.awt.Font
import javax.swing.JComponent
import javax.swing.JScrollPane
import javax.swing.JTable
import javax.swing.table.DefaultTableModel

class AggregationResultTableFactoryService {

    operator fun invoke(
            tableHeader: Array<String>,
            rows: Collection<Array<Any>>
    ): JComponent {
        val tableModel = object : DefaultTableModel(tableHeader, 0) {
            override fun getColumnClass(columnIndex: Int): Class<*> {
                return when (columnIndex) {
                    tableHeader.size - 1 -> Integer::class.java
                    else -> String::class.java
                }
            }
        }
        val table = JTable(tableModel)
        setAppropriateTextAppearance(table)

        table.autoCreateRowSorter = true

        rows.forEach { tableModel.addRow(it) }

        return JScrollPane(table).also {
            it.preferredSize = PREFERRED_SIZE
        }
    }

    private fun setAppropriateTextAppearance(table: JTable) {
        val font = Font(Font.SANS_SERIF, Font.PLAIN, 16)
        table.tableHeader?.font = font
        table.font = font
        table.rowHeight = 36
    }

    companion object {
        private val PREFERRED_SIZE = Dimension(600, 400)
    }
}