package jp.toastkid.yobidashi.compact.calendar.view

import jp.toastkid.yobidashi.compact.calendar.model.Month
import jp.toastkid.yobidashi.compact.calendar.service.OffDayFinderUseCase
import java.awt.Color
import java.awt.Dimension
import java.awt.GridLayout
import java.util.Calendar
import javax.swing.BoxLayout
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JSpinner
import javax.swing.JSpinner.DefaultEditor
import javax.swing.JTextField
import javax.swing.SpinnerListModel
import javax.swing.SpinnerNumberModel
import javax.swing.border.LineBorder
import javax.swing.event.ChangeListener

class CalendarPanel : JPanel() {

    private val monthChooser = JSpinner()

    private val yearChooser = JSpinner()

    private val dayLabels = Array(6) { arrayOfNulls<JLabel>(7) }

    init {
        layout = BoxLayout(this, BoxLayout.Y_AXIS)
        add(makeChoosersPanel())
        add(makeDayPanel())

        val cal = Calendar.getInstance()
        monthChooser.value = Month.from(cal)
        yearChooser.value = cal[Calendar.YEAR]

        refreshDayLabels(cal, true)
    }

    private fun makeChoosersPanel(): JPanel {
        makeMonthChooser()
        val monthYearListener: ChangeListener = makeMonthChangeListener()
        monthChooser.addChangeListener(monthYearListener)
        makeYearChooser(monthYearListener)

        val chooserPanel = JPanel()
        chooserPanel.add(JButton("<").also {
            it.addActionListener {
                addMonth(-1)
            }
        })
        chooserPanel.add(yearChooser)
        chooserPanel.add(monthChooser)
        chooserPanel.add(JButton(">").also {
            it.addActionListener {
                addMonth(1)
            }
        })
        return chooserPanel
    }

    private fun addMonth(additional: Int) {
        val currentMonth = monthChooser.value as? Month ?: return
        val next = currentMonth.ordinal + additional
        val nextMonth = when {
            next < 0 -> {
                yearChooser.value = yearChooser.value as Int - 1
                Month.DEC
            }
            next > 11 -> {
                yearChooser.value = yearChooser.value as Int + 1
                Month.JAN
            }
            else -> {
                Month.values()[next]
            }
        }
        monthChooser.value = nextMonth
    }

    private fun makeDayPanel(): JPanel {
        val dayPanel = JPanel()
        val layout = GridLayout(7, 7)
        layout.hgap = 3
        layout.vgap = 3
        dayPanel.layout = layout

        val dayOfWeekLabelFactory = DayOfWeekLabelFactory(DAY_LABEL_SIZE)
        for (i in 0..6) {
            dayPanel.add(dayOfWeekLabelFactory.invoke(i))
        }

        val dayLabelFactory = DayLabelFactory(DAY_LABEL_SIZE)

        for (i in 0..5) {
            for (j in 0..6) {
                dayLabels[i][j] = dayLabelFactory.invoke(j)
                dayPanel.add(dayLabels[i][j])
            }
        }
        return dayPanel
    }

    private fun makeYearChooser(monthYearListener: ChangeListener) {
        yearChooser.model = SpinnerNumberModel(2012, 0, 5000, 1)
        val editor = JSpinner.NumberEditor(yearChooser, "#")
        yearChooser.editor = editor
        yearChooser.font = yearChooser.font.deriveFont(14f)
        yearChooser.addChangeListener(monthYearListener)
        val dy = yearChooser.preferredSize
        dy.width += 10 //dy.height += 1;
        yearChooser.preferredSize = dy
    }

    private fun makeMonthChangeListener(): ChangeListener {
        return ChangeListener {
            refreshDayLabels(yearChooser.value as Int,  Month.fromName(monthChooser.value.toString()))
        }
    }

    private fun makeMonthChooser() {
        monthChooser.model = SpinnerListModel(Month.values())
        monthChooser.font = monthChooser.font.deriveFont(14f)
        val tm = (monthChooser.editor as? DefaultEditor)?.textField
        tm?.horizontalAlignment = JTextField.CENTER

        val dm = monthChooser.preferredSize
        dm.width += 10
        monthChooser.preferredSize = dm
    }

    private fun refreshDayLabels(year: Int, month: Month) {
        val cal = Calendar.getInstance()
        if (month.isSameMonth(cal) && year == cal[Calendar.YEAR]) {
            refreshDayLabels(cal, true)
        } else {
            cal[year, month.ordinal] = 1
            refreshDayLabels(cal, false)
        }
    }

    private fun refreshDayLabels(calendar: Calendar, currentMonth: Boolean) {
        val maxDate = calendar.getActualMaximum(Calendar.DATE)
        val today = calendar[Calendar.DATE]
        calendar[Calendar.DATE] = 1
        val firstDayOfWeek = calendar[Calendar.DAY_OF_WEEK]
        for (ll in dayLabels) {
            for (l in ll) {
                l?.text = ""
            }
        }

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val offDayFinder = OffDayFinderUseCase()
        for (day in 1..maxDate) {
            getDayLabel(day, firstDayOfWeek)?.also {
                it.text = day.toString()
                val color = offDayFinder(year, month, day, (day + firstDayOfWeek - 2) % 7)
                when (it.foreground) {
                    Color.BLACK -> it.foreground = color
                    Color.RED -> it.foreground = color
                }
            }
        }

        for (i in dayLabels.indices) {
            for (element in dayLabels[i]) {
                element?.also {
                    it.background = DAY_BG
                    it.border = BORDER
                    it.isVisible = it.text.isNotEmpty()
                }
            }
        }
        if (currentMonth) {
            getDayLabel(today, firstDayOfWeek)?.also {
                it.background = TODAY_BG
                it.border = TODAY_BORDER
            }
        }
    }

    private fun getDayLabel(day: Int, firstDayOfWeek: Int): JLabel? {
        return dayLabels[(day + firstDayOfWeek - 2) / 7][(day + firstDayOfWeek - 2) % 7]
    }

    companion object {
        private val DAY_BG: Color = Color(250, 250, 255)
        private val BORDER = LineBorder(Color(220, 220, 220, 220), 2, false)

        private val TODAY_BG: Color = Color(220, 220, 255)
        private val TODAY_FG: Color = Color(105, 50, 50)
        private val TODAY_BORDER = LineBorder(Color(50, 50, 175), 2, false)

        private val DAY_LABEL_SIZE = Dimension(50, 50)
    }
}

fun main() {
    val frame = JFrame("Calender")
    //      frame.setSize(400,600);
    frame.add(CalendarPanel())
    frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    frame.pack()
    frame.isVisible = true
}