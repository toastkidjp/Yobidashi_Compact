package jp.toastkid.yobidashi.compact.view

import jp.toastkid.yobidashi.compact.model.OutgoAggregationResult
import jp.toastkid.yobidashi.compact.service.ArticleLengthAggregatorService
import jp.toastkid.yobidashi.compact.service.OutgoAggregationResultTableContentFactoryService
import jp.toastkid.yobidashi.compact.service.OutgoAggregatorService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.swing.Swing
import kotlinx.coroutines.withContext
import java.awt.event.ActionEvent
import java.awt.event.InputEvent
import java.awt.event.KeyEvent
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.swing.AbstractAction
import javax.swing.JMenu
import javax.swing.JMenuItem
import javax.swing.JOptionPane
import javax.swing.JScrollPane
import javax.swing.JTextArea
import javax.swing.KeyStroke

/**
 * TODO:
 * Code clean up.
 */
class AggregationMenuView {

    operator fun invoke(): JMenu {
        val menu = JMenu("Aggregate")
        menu.add(makeMenuItem())

        val item = JMenuItem("Article length")
        item.hideActionText = true
        item.addActionListener {
            val defaultInput = LocalDate.now().format(DATE_FORMATTER)
            val keyword = JOptionPane.showInputDialog(
                    null,
                    "Please input year and month you want aggregate article length? ex) $defaultInput",
                    defaultInput
            )

            if (keyword.isNullOrBlank()) {
                return@addActionListener
            }

            CoroutineScope(Dispatchers.Swing).launch {
                try {
                    val result = withContext(Dispatchers.IO) {  ArticleLengthAggregatorService().invoke(keyword) }
                    val area = JTextArea(result.map { "${it.key}\t${it.value}" }.reduce { base, item -> "$base\n$item" })
                    JOptionPane.showMessageDialog(
                            null,
                            JScrollPane(area),
                            "$keyword ${result.values.sum()}",
                            JOptionPane.PLAIN_MESSAGE
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                    JOptionPane.showConfirmDialog(null, e)
                }
            }
        }
        item.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.CTRL_MASK)
        menu.add(item)

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

        JOptionPane.showMessageDialog(
                null,
                OutgoAggregationResultTableContentFactoryService().invoke(
                        arrayOf("Date", "Item", "Price"),
                        aggregationResult.makeItemArrays()
                ),
                "${aggregationResult.target} Total: ${aggregationResult.sum()}",
                JOptionPane.INFORMATION_MESSAGE
        )
    }

    companion object {
        private val DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM")
    }
}