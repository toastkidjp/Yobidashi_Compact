package jp.toastkid.yobidashi.compact.view

import jp.toastkid.yobidashi.compact.model.OutgoAggregationResult
import jp.toastkid.yobidashi.compact.service.AggregationMenuItemGeneratorService
import jp.toastkid.yobidashi.compact.service.AggregationResultTableFactoryService
import jp.toastkid.yobidashi.compact.service.ArticleLengthAggregatorService
import jp.toastkid.yobidashi.compact.service.MovieMemoSubtitleExtractor
import jp.toastkid.yobidashi.compact.service.Nikkei225AggregatorService
import jp.toastkid.yobidashi.compact.service.OutgoAggregatorService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.swing.Swing
import kotlinx.coroutines.withContext
import java.awt.event.InputEvent
import java.awt.event.KeyEvent
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.swing.JMenu
import javax.swing.JOptionPane
import javax.swing.KeyStroke

/**
 * TODO:
 * Code clean up.
 */
class AggregationMenuView {

    private val aggregationMenuItemGeneratorService = AggregationMenuItemGeneratorService()

    operator fun invoke(): JMenu {
        val menu = JMenu("Aggregate")

        menu.add(
                aggregationMenuItemGeneratorService.invoke(
                        "OutGo",
                        "Please input year and month you want aggregate outgo?",
                        { OutgoAggregatorService().invoke(it) },
                        KeyStroke.getKeyStroke(KeyEvent.VK_G, InputEvent.CTRL_MASK)
                )
        )
        menu.add(
                aggregationMenuItemGeneratorService.invoke(
                        "Article length",
                        "Please input year and month you want aggregate article length?",
                        { ArticleLengthAggregatorService().invoke(it) },
                        KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.CTRL_MASK)
                )
        )
        menu.add(
                aggregationMenuItemGeneratorService.invoke(
                    "Movie memo",
                    "Please input year and month you want aggregate article length? ex)",
                    { MovieMemoSubtitleExtractor().invoke(it) },
                    KeyStroke.getKeyStroke(KeyEvent.VK_M, InputEvent.CTRL_MASK)
                )
        )
        menu.add(
                aggregationMenuItemGeneratorService.invoke(
                        "Nikkei 225",
                        "Please input year and month you want aggregate Nikkei 225? ex)",
                        { Nikkei225AggregatorService().invoke(it) },
                        KeyStroke.getKeyStroke(KeyEvent.VK_2, InputEvent.CTRL_MASK)
                )
        )

        return menu
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
                AggregationResultTableFactoryService().invoke(aggregationResult),
                "${aggregationResult.target} Total: ${aggregationResult.sum()}",
                JOptionPane.INFORMATION_MESSAGE
        )
    }

    companion object {
        private val DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM")
    }
}