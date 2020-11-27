package jp.toastkid.yobidashi.compact.view

import jp.toastkid.yobidashi.compact.model.OutgoAggregationResult
import jp.toastkid.yobidashi.compact.service.OutgoAggregatorService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.swing.Swing
import kotlinx.coroutines.withContext
import java.awt.ScrollPane
import java.awt.event.ActionEvent
import java.awt.event.InputEvent
import java.awt.event.KeyEvent
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.swing.AbstractAction
import javax.swing.BoxLayout
import javax.swing.JMenu
import javax.swing.JMenuItem
import javax.swing.JOptionPane
import javax.swing.JPanel
import javax.swing.JTable
import javax.swing.KeyStroke
import javax.swing.table.DefaultTableModel

/**
 * TODO:
 * Code clean up.
 * Make scrollable
 */
class AggregationMenuView {

    operator fun invoke(): JMenu {
        val menu = JMenu("Aggregate")
        menu.add(makeMenuItem())
        return menu
    }
    private fun makeMenuItem(): JMenuItem {
        val item = JMenuItem()
        item.hideActionText = true
        item.action = object : AbstractAction() {
            override fun actionPerformed(e: ActionEvent) {
                onAction()
            }
        }
        item.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_G, InputEvent.CTRL_MASK)
        item.text = "OutGo"
        return item
    }

    private fun onAction() {
        val defaultInput = LocalDate.now().format(DATE_FORMATTER)

        val keyword = JOptionPane.showInputDialog(
                null,
                "Please input year and month you want aggregate outgo? ex) $defaultInput",
                defaultInput
        )

        if (keyword.isNullOrBlank()) {
            return
        }

        CoroutineScope(Dispatchers.Swing).launch {
            try {
                val result = withContext(Dispatchers.IO) { OutgoAggregatorService().invoke(keyword) }
                onSuccess(result)
            } catch (e: Exception) {
                e.printStackTrace()
                JOptionPane.showConfirmDialog(null, e)
            }
        }
    }

    private fun onSuccess(aggregationResult: OutgoAggregationResult) {
        if (aggregationResult.isEmpty()) {
            JOptionPane.showConfirmDialog(null, "Result is empty.")
            return
        }

        val tableModel = DefaultTableModel(arrayOf("Date", "Item", "Price"), 0)
        val table = JTable(tableModel)
        aggregationResult.makeItemArrays().forEach { tableModel.addRow(it) }
        JOptionPane.showConfirmDialog(
                null,
                ScrollPane().also {
                    it.add(
                            JPanel().also {
                                it.layout = BoxLayout(it, BoxLayout.PAGE_AXIS)
                                it.add(table.tableHeader)
                                it.add(table)
                            }
                    )
                }
        )
    }

    companion object {
        private val DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM")
    }
}