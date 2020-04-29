package jp.toastkid.yobidashi.compact.view

import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import jp.toastkid.yobidashi.compact.model.Setting
import java.awt.event.ActionEvent
import java.awt.event.InputEvent
import java.awt.event.KeyEvent
import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.stream.Collectors
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
                val defaultInput = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"))

                val keyword = JOptionPane.showInputDialog(
                        null,
                        "Please input year and month you want aggregate outgo? ex) $defaultInput",
                        defaultInput
                )

                if (keyword.isNullOrBlank()) {
                    return
                }

                val map = mutableMapOf<String, Int>()
                Single.fromCallable {
                    Files.list(Paths.get(Setting.articleFolder()))
                            .parallel()
                            .map { it.fileName.toString() to Files.readAllLines(it) }
                            .filter { it.first.startsWith(keyword) }
                            .map {
                                var isOutGoLine = false
                                for (line in it.second) {
                                    if ((line.startsWith("#")) && line.endsWith("家計簿")) {
                                        isOutGoLine = true
                                    }

                                    if (!isOutGoLine || !line.startsWith("|")) {
                                        continue
                                    }

                                    val items = line.split("|")
                                    val target = items.get(2)
                                    var price = 0
                                    if (target.endsWith("円")) {
                                        val priceStr = target.substring(0, target.indexOf("円")).trim().replace(",", "")
                                        if (priceStr.isNotBlank()) {
                                            price = Integer.parseInt(priceStr)
                                        }
                                        map.put(items.get(0) +  items.get(1).trim(), price)
                                    }
                                }
                            }
                            .collect(Collectors.toList())
                }.subscribeOn(Schedulers.io())
                        .subscribe(
                                {
                                    if (map.isEmpty()) {
                                        JOptionPane.showConfirmDialog(null, "Result is empty.")
                                        return@subscribe
                                    }

                                    val sum = map.values.sum()
                                    val detail = map.entries.map { "${it.key}: ${it.value}" }.reduce { base, item -> "$base$LINE_SEPARATOR$item" }
                                    JOptionPane.showConfirmDialog(null, "\\$sum$LINE_SEPARATOR$detail")
                                },
                                {
                                    it.printStackTrace()
                                    JOptionPane.showConfirmDialog(null, it)
                                }
                        )
            }
        }
        item.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_G, InputEvent.CTRL_MASK)
        item.text = "OutGo"
        return item
    }

    companion object {
        private val LINE_SEPARATOR = System.lineSeparator()
    }
}