package jp.toastkid.yobidashi.compact.aggregation.service

import jp.toastkid.yobidashi.compact.aggregation.model.CompoundInterestCalculationResult
import kotlin.math.pow
import kotlin.math.roundToInt

class CompoundInterestCalculatorService {

    /**
     *
     * @param annualInterest If you want to calculate 5%, you should pass 1.05
     */
    operator fun invoke(installment: Int, annualInterest: Double, year: Int): CompoundInterestCalculationResult {
        val result = CompoundInterestCalculationResult()
        var single = 0.0
        (1 .. year)
                .map { installment to installment }
                .forEachIndexed() { index, item ->
                    single += (installment * (1 + annualInterest))
                    val coefficient = ((1 + annualInterest).pow(index + 1) - 1) / annualInterest
                    val compound =  (installment.toDouble()) * coefficient
                    result.put(index + 1, single.toInt(), compound.roundToInt())
                }
        return result
    }

}