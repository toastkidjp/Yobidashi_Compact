package jp.toastkid.yobidashi.compact.calendar.view

import java.time.LocalDate
import java.time.Month
import javax.swing.JButton
import javax.swing.JPanel
import javax.swing.JSpinner
import javax.swing.JTextField
import javax.swing.SpinnerListModel
import javax.swing.SpinnerNumberModel
import javax.swing.event.ChangeListener

class ChooserPanelFactory(private val refreshDayLabels: (Int, Month) -> Unit) {

    operator fun invoke(): JPanel {
        val monthChooser = makeMonthChooser()
        val yearChooser = makeYearChooser()

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

        return chooserPanel
    }

    private fun makeMonthChooser(): JSpinner {
        val monthChooser = JSpinner()
        monthChooser.model = SpinnerListModel(Month.values())
        monthChooser.font = monthChooser.font.deriveFont(14f)
        val tm = (monthChooser.editor as? JSpinner.DefaultEditor)?.textField
        tm?.horizontalAlignment = JTextField.CENTER

        val dm = monthChooser.preferredSize
        dm.width += 10
        monthChooser.preferredSize = dm
        return monthChooser
    }

    private fun makeYearChooser(): JSpinner {
        val yearChooser = JSpinner()
        yearChooser.model = SpinnerNumberModel(2012, 0, 5000, 1)
        val editor = JSpinner.NumberEditor(yearChooser, "#")
        yearChooser.editor = editor
        yearChooser.font = yearChooser.font.deriveFont(14f)
        val dy = yearChooser.preferredSize
        dy.width += 10 //dy.height += 1;
        yearChooser.preferredSize = dy
        return yearChooser
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