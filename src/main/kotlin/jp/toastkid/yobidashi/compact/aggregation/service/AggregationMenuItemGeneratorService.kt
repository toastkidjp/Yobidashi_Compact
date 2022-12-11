package jp.toastkid.yobidashi.compact.aggregation.service

import jp.toastkid.yobidashi.compact.SubjectPool
import jp.toastkid.yobidashi.compact.aggregation.model.AggregationResult
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
            accelerator: KeyStroke? = null
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

                    if (result.isEmpty()) {
                        JOptionPane.showConfirmDialog(null, "Result is empty.")
                        return@launch
                    }

                    val table = AggregationResultTableFactoryService().invoke(result)
                    SubjectPool.addNewTab(table, "$keyword ${result.resultTitleSuffix()}")
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