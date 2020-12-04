package jp.toastkid.yobidashi.compact.view

import jp.toastkid.yobidashi.compact.model.OutgoAggregationResult
import jp.toastkid.yobidashi.compact.service.AggregationResultTableFactoryService
import jp.toastkid.yobidashi.compact.service.ArticleLengthAggregatorService
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
import javax.swing.KeyStroke

/**
 * TODO:
 * Code clean up.
 */
class AggregationMenuView {

    operator fun invoke(): JMenu {
        val menu = JMenu("Aggregate")

        menu.add(makeMenuItem())
        menu.add(makeArticleLengthAggregationMenuItem())

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
                AggregationResultTableFactoryService().invoke(
                        arrayOf("Date", "Item", "Price"),
                        aggregationResult.makeItemArrays()
                ),
                "${aggregationResult.target} Total: ${aggregationResult.sum()}",
                JOptionPane.INFORMATION_MESSAGE
        )
    }

    private fun makeArticleLengthAggregationMenuItem(): JMenuItem {
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
                    val result = withContext(Dispatchers.IO) { ArticleLengthAggregatorService().invoke(keyword) }
                    JOptionPane.showMessageDialog(
                            null,
                            AggregationResultTableFactoryService().invoke(
                                    arrayOf("Title", "Length"),
                                    result.entries.map { arrayOf(it.key, it.value) }
                            ),
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
        return item
    }

    companion object {
        private val DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM")
    }
}