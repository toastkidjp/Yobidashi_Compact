package jp.toastkid.yobidashi.compact.calendar.view

import javax.swing.JSpinner
import javax.swing.SpinnerNumberModel

class YearChooserFactory {

    operator fun invoke(): JSpinner {
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

}