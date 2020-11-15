package jp.toastkid.yobidashi.compact.view

import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import jp.toastkid.yobidashi.compact.model.OutgoAggregationResult
import jp.toastkid.yobidashi.compact.service.OutgoAggregatorService
import java.awt.event.ActionEvent
import java.awt.event.InputEvent
import java.awt.event.KeyEvent
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.swing.*

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

        Single.fromCallable { OutgoAggregatorService().invoke(keyword) }
                .subscribeOn(Schedulers.io())
                .subscribe(
                        { onSuccess(it) },
                        {
                            it.printStackTrace()
                            JOptionPane.showConfirmDialog(null, it)
                        }
                )
    }

    private fun onSuccess(aggregationResult: OutgoAggregationResult) {
        if (aggregationResult.isEmpty()) {
            JOptionPane.showConfirmDialog(null, "Result is empty.")
            return
        }

        JOptionPane.showConfirmDialog(null, JScrollPane(JTextArea(aggregationResult.makeMessage())))
    }

    companion object {
        private val DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM")
    }
}