package jp.toastkid.yobidashi.compact.calendar.view

import java.awt.event.ActionEvent
import java.awt.event.KeyEvent
import java.time.LocalDate
import java.time.Month
import javax.swing.AbstractAction
import javax.swing.JButton
import javax.swing.JPanel
import javax.swing.JSpinner
import javax.swing.KeyStroke
import javax.swing.event.ChangeListener

class ChooserPanelFactory(
    private val refreshDayLabels: (Int, Month) -> Unit,
    private val monthChooserFactory: MonthChooserFactory = MonthChooserFactory(),
    private val yearChooserFactory: YearChooserFactory = YearChooserFactory()
) {

    operator fun invoke(): JPanel {
        val monthChooser = monthChooserFactory.invoke()
        val yearChooser = yearChooserFactory.invoke()

        val monthYearListener: ChangeListener = makeMonthChangeListener(yearChooser, monthChooser)
        monthChooser.addChangeListener(monthYearListener)
        yearChooser.addChangeListener(monthYearListener)

        val chooserPanel = JPanel()
        chooserPanel.add(JButton("<").also {
            it.addActionListener {
                addMonth(monthChooser, yearChooser, -1)
            }
        })
        chooserPanel.add(yearChooser)
        chooserPanel.add(monthChooser)
        chooserPanel.add(JButton(">").also {
            it.addActionListener {
                addMonth(monthChooser, yearChooser, 1)
            }
        })

        val date = LocalDate.now()
        monthChooser.value = date.month
        yearChooser.value = date.year

        setKeyboardAction(chooserPanel, monthChooser, yearChooser)

        return chooserPanel
    }

    private fun setKeyboardAction(
        chooserPanel: JPanel,
        monthChooser: JSpinner,
        yearChooser: JSpinner
    ) {
        chooserPanel.inputMap.also {
            it.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "prev")
            it.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "next")
        }

        chooserPanel.actionMap.also {
            it.put("prev", object : AbstractAction() {
                override fun actionPerformed(e: ActionEvent?) {
                    addMonth(monthChooser, yearChooser, -1)
                }
            })
            it.put("next", object : AbstractAction() {
                override fun actionPerformed(e: ActionEvent?) {
                    addMonth(monthChooser, yearChooser, 1)
                }
            })
        }
    }

    private fun makeMonthChangeListener(yearChooser: JSpinner, monthChooser: JSpinner): ChangeListener {
        return ChangeListener {
            refreshDayLabels(yearChooser.value as Int,  Month.valueOf(monthChooser.value.toString()))
        }
    }

    private fun addMonth(monthChooser: JSpinner, yearChooser: JSpinner, additional: Int) {
        val currentMonth = monthChooser.value as? Month ?: return
        val next = currentMonth.ordinal + additional
        val nextMonth = when {
            next < 0 -> {
                yearChooser.value = yearChooser.value as Int - 1
                Month.DECEMBER
            }
            next > 11 -> {
                yearChooser.value = yearChooser.value as Int + 1
                Month.JANUARY
            }
            else -> {
                Month.values()[next]
            }
        }
        monthChooser.value = nextMonth
    }

}