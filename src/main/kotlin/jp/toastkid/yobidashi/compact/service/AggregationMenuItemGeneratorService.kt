package jp.toastkid.yobidashi.compact.service

import jp.toastkid.yobidashi.compact.model.AggregationResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.swing.Swing
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.swing.JMenuItem
import javax.swing.JOptionPane
import javax.swing.KeyStroke

class AggregationMenuItemGeneratorService {

    operator fun invoke(
            menuTitle: String,
            message: String,
            aggregator: (String) -> AggregationResult,
            accelerator: KeyStroke?
    ): JMenuItem {
        val item = JMenuItem(menuTitle)
        item.hideActionText = true
        item.addActionListener {
            val defaultInput = LocalDate.now().format(DATE_FORMATTER)
            val keyword = JOptionPane.showInputDialog(
                    null,
                    "$message ex)$defaultInput",
                    defaultInput
            )

            if (keyword.isNullOrBlank()) {
                return@addActionListener
            }

            CoroutineScope(Dispatchers.Swing).launch {
                try {
                    val result = withContext(Dispatchers.IO) { aggregator(keyword) }
                    val table = AggregationResultTableFactoryService().invoke(result)
                    JOptionPane.showMessageDialog(
                            null,
                            table,
                            "$keyword ${result.resultTitleSuffix()}",
                            JOptionPane.PLAIN_MESSAGE
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                    JOptionPane.showConfirmDialog(null, e)
                }
            }
        }
        accelerator?.let { item.accelerator = it }
        return item
    }

    companion object {

        private val DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM")

    }

}