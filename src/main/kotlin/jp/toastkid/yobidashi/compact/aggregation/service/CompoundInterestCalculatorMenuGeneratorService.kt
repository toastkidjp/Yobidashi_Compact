package jp.toastkid.yobidashi.compact.aggregation.service

import jp.toastkid.yobidashi.compact.SubjectPool
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.swing.Swing
import kotlinx.coroutines.withContext
import java.awt.GridLayout
import java.text.NumberFormat
import javax.swing.JFormattedTextField
import javax.swing.JLabel
import javax.swing.JMenuItem
import javax.swing.JOptionPane
import javax.swing.JPanel
import javax.swing.text.NumberFormatter

class CompoundInterestCalculatorMenuGeneratorService {

    operator fun invoke(): JMenuItem {
        val item = JMenuItem("Compound interest calculation")
        item.hideActionText = true
        item.addActionListener {
            //installment: Int, annualInterest: Double, year: Int
            val intFormatter = NumberFormatter(NumberFormat.getInstance())
            intFormatter.valueClass = Integer::class.java
            intFormatter.minimum = 0
            intFormatter.allowsInvalid = false

            val installmentInput = JFormattedTextField(IntFormatterFactoryService()())
            val annualInterestInput = JFormattedTextField(IntFormatterFactoryService()())
            val yearInput = JFormattedTextField(IntFormatterFactoryService()())
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
                return@addActionListener
            }

            val installment = installmentInput.text.replace(",", "").toInt()
            val annualInterest = annualInterestInput.text.replace(",", "").toDouble()
            val year = yearInput.text.replace(",", "").toInt()

            CoroutineScope(Dispatchers.Swing).launch {
                try {
                    val result = withContext(Dispatchers.IO) {
                        CompoundInterestCalculatorService()(installment, (annualInterest * 0.01), year)
                    }

                    if (result.isEmpty()) {
                        JOptionPane.showConfirmDialog(null, "Result is empty.")
                        return@launch
                    }

                    val table = AggregationResultTableFactoryService().invoke(result)
                    // "Compound interest calculation. Installment = $installment, Annual interest = $annualInterest, Year = $year"
                    SubjectPool.addNewTab(table, "$installment, $annualInterest, $year")
                } catch (e: Exception) {
                    e.printStackTrace()
                    JOptionPane.showConfirmDialog(null, e)
                }
            }
        }
        return item
    }

}