package jp.toastkid.yobidashi.compact.calendar.service

import jp.toastkid.yobidashi.compact.calendar.model.EquinoxDayCalculator
import jp.toastkid.yobidashi.compact.calendar.model.FixedJapaneseHoliday
import java.awt.Color

class OffDayFinderUseCase {

    private val equinoxDayCalculator = EquinoxDayCalculator()

    private val userOffDayService = UserOffDayService()

    private val moveableHolidayCalculatorService = MoveableHolidayCalculatorService()

    private val specialCaseOffDayCalculator = SpecialCaseOffDayCalculator()

    operator fun invoke(year: Int, month: Int, date: Int, dayOfWeek: Int): Color {
        if (month == 6) {
            return COLOR_NORMAL_DAY
        }

        if (month == 3 && date == equinoxDayCalculator.calculateVernalEquinoxDay(year)) {
            return COLOR_OFF_DAY
        }

        if (month == 9 && date == equinoxDayCalculator.calculateAutumnalEquinoxDay(year)) {
            return COLOR_OFF_DAY
        }

        val isSpecialCase = specialCaseOffDayCalculator(year, month, date)
        if (isSpecialCase.first) {
            return COLOR_OFF_DAY
        }
        if (isSpecialCase.second) {
            return COLOR_NORMAL_DAY
        }

        if (moveableHolidayCalculatorService(year, month, date)) {
            return COLOR_OFF_DAY
        }

        if (userOffDayService(month, date)) {
            return COLOR_OFF_DAY
        }

        var firstOrNull = FixedJapaneseHoliday.values()
                .firstOrNull { month == it.month && date == it.date }
        if (firstOrNull == null) {
            if (month == 5 && date == 6 && dayOfWeek <= 3) {
                return COLOR_OFF_DAY
            }
            if (dayOfWeek == 1) {
                firstOrNull = FixedJapaneseHoliday.values()
                        .firstOrNull { month == it.month && (date - 1) == it.date }
            }
        }
        return if (firstOrNull != null) {
            COLOR_OFF_DAY
        } else COLOR_NORMAL_DAY
    }

    companion object {

        private val COLOR_NORMAL_DAY = Color.BLACK

        private val COLOR_OFF_DAY = Color.RED

    }

}