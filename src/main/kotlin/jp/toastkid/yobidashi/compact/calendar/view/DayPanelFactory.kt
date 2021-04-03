package jp.toastkid.yobidashi.compact.calendar.view

import java.awt.Dimension
import java.awt.GridLayout
import java.time.DayOfWeek
import javax.swing.JLabel
import javax.swing.JPanel

class DayPanelFactory(
    private val dayOfWeekLabelFactory: DayOfWeekLabelFactory = DayOfWeekLabelFactory(DAY_LABEL_SIZE),
    private val dayLabelFactory: DayLabelFactory = DayLabelFactory(DAY_LABEL_SIZE)
) {

    operator fun invoke(): Pair<JPanel, Array<Array<JLabel?>>> {
        val dayPanel = JPanel()
        val dayLabels =  Array(6) { arrayOfNulls<JLabel>(7) }

        val layout = GridLayout(7, 7)
        layout.hgap = 3
        layout.vgap = 3
        dayPanel.layout = layout

        dayPanel.add(dayOfWeekLabelFactory(DayOfWeek.SUNDAY))
        dayPanel.add(dayOfWeekLabelFactory(DayOfWeek.MONDAY))
        dayPanel.add(dayOfWeekLabelFactory(DayOfWeek.TUESDAY))
        dayPanel.add(dayOfWeekLabelFactory(DayOfWeek.WEDNESDAY))
        dayPanel.add(dayOfWeekLabelFactory(DayOfWeek.THURSDAY))
        dayPanel.add(dayOfWeekLabelFactory(DayOfWeek.FRIDAY))
        dayPanel.add(dayOfWeekLabelFactory(DayOfWeek.SATURDAY))

        for (i in 0..5) {
            for (j in 0..6) {
                dayLabels[i][j] = dayLabelFactory.invoke(j)
                dayPanel.add(dayLabels[i][j])
            }
        }
        return dayPanel to dayLabels
    }

    companion object {

        private val DAY_LABEL_SIZE = Dimension(50, 50)

    }

}