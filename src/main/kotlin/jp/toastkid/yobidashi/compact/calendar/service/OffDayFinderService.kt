package jp.toastkid.yobidashi.compact.calendar.service

import jp.toastkid.yobidashi.compact.calendar.model.EquinoxDayCalculator
import jp.toastkid.yobidashi.compact.calendar.model.FixedJapaneseHoliday

class OffDayFinderService(
        private val equinoxDayCalculator: EquinoxDayCalculator = EquinoxDayCalculator(),
        private val userOffDayService: UserOffDayService = UserOffDayService(),
        private val moveableHolidayCalculatorService: MoveableHolidayCalculatorService = MoveableHolidayCalculatorService(),
        private val specialCaseOffDayCalculator: SpecialCaseOffDayCalculatorService = SpecialCaseOffDayCalculatorService()
) {

    operator fun invoke(year: Int, month: Int, date: Int, dayOfWeek: Int): Boolean {
        if (month == 6) {
            return false
        }

        if (month == 3 && date == equinoxDayCalculator.calculateVernalEquinoxDay(year)) {
            return true
        }

        if (month == 9 && date == equinoxDayCalculator.calculateAutumnalEquinoxDay(year)) {
            return true
        }

        val isSpecialCase = specialCaseOffDayCalculator(year, month, date)
        if (isSpecialCase.first) {
            return true
        }
        if (isSpecialCase.second) {
            return false
        }

        if (moveableHolidayCalculatorService(year, month, date)) {
            return true
        }

        if (userOffDayService(month, date)) {
            return true
        }

        var firstOrNull = FixedJapaneseHoliday.values()
                .firstOrNull { month == it.month && date == it.date }
        if (firstOrNull == null) {
            if (month == 5 && date == 6 && dayOfWeek <= 3) {
                return true
            }
            if (dayOfWeek == 1) {
                firstOrNull = FixedJapaneseHoliday.values()
                        .firstOrNull { month == it.month && (date - 1) == it.date }
            }
        }
        return if (firstOrNull != null) {
            true
        } else false
    }

}