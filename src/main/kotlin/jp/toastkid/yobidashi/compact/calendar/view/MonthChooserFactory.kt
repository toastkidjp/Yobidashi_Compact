package jp.toastkid.yobidashi.compact.calendar.view

import java.time.Month
import javax.swing.JSpinner
import javax.swing.JTextField
import javax.swing.SpinnerListModel

class MonthChooserFactory {

    operator fun invoke(): JSpinner {
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

}