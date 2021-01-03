package jp.toastkid.yobidashi.compact.aggregation.service

import java.awt.GridLayout
import javax.swing.JFormattedTextField
import javax.swing.JLabel
import javax.swing.JOptionPane
import javax.swing.JPanel

class CompoundInterestCalculationInputService {

    operator fun invoke(): Triple<Int, Double, Int>? {
        val intFormatter = IntFormatterFactoryService().invoke()
        val installmentInput = JFormattedTextField(intFormatter)
        val annualInterestInput = JFormattedTextField(intFormatter)
        val yearInput = JFormattedTextField(intFormatter)
        val content = JPanel().also {
            it.layout = GridLayout(3, 2)
            it.add(JLabel("Installment"))
            it.add(installmentInput)
            it.add(JLabel("Annual interest"))
            it.add(annualInterestInput)
            it.add(JLabel("Year"))
            it.add(yearInput)
        }

        val option = JOptionPane.showConfirmDialog(
                null,
                content
        )

        if (option != JOptionPane.OK_OPTION
                || installmentInput.text.isNullOrBlank()
                || annualInterestInput.text.isNullOrBlank()
                || yearInput.text.isNullOrBlank()
        ) {
            return null
        }

        val installment = installmentInput.text.replace(",", "").toInt()
        val annualInterest = annualInterestInput.text.replace(",", "").toDouble()
        val year = yearInput.text.replace(",", "").toInt()

        return Triple(installment, annualInterest, year)
    }

}