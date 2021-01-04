package jp.toastkid.yobidashi.compact.aggregation.service

import jp.toastkid.yobidashi.compact.SubjectPool
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.swing.Swing
import kotlinx.coroutines.withContext
import javax.swing.JMenuItem
import javax.swing.JOptionPane

class CompoundInterestCalculatorMenuGeneratorService(
        private val inputService: CompoundInterestCalculationInputService = CompoundInterestCalculationInputService(),
        private val calculatorService: CompoundInterestCalculatorService = CompoundInterestCalculatorService(),
        private val aggregationResultTableFactoryService: AggregationResultTableFactoryService = AggregationResultTableFactoryService()
) {

    operator fun invoke(): JMenuItem {
        val item = JMenuItem("Compound interest calculation")
        item.addActionListener {
            onAction()
        }
        return item
    }

    private fun onAction() {
        val (installment, annualInterest, year) = inputService.invoke() ?: return

        CoroutineScope(Dispatchers.Swing).launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    calculatorService(installment, (annualInterest * 0.01), year)
                }

                if (result.isEmpty()) {
                    JOptionPane.showConfirmDialog(null, "Result is empty.")
                    return@launch
                }

                val table = aggregationResultTableFactoryService.invoke(result)
                // "Compound interest calculation. Installment = $installment, Annual interest = $annualInterest, Year = $year"
                SubjectPool.addNewTab(table, "$installment, $annualInterest, $year")
            } catch (e: Exception) {
                e.printStackTrace()
                JOptionPane.showConfirmDialog(null, e)
            }
        }
    }

}