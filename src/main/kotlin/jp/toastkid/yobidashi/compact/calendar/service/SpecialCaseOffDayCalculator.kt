package jp.toastkid.yobidashi.compact.calendar.service

class SpecialCaseOffDayCalculator {

    operator fun invoke(year: Int, month: Int, date: Int): Pair<Boolean, Boolean> {
        if (year == 2019 && month == 5 && date == 1) {
            return true to false
        }

        if (year == 2020) {
            if (month == 7) {
                return when (date) {
                    23, 24 -> return true to true
                    else -> false to true
                }
            }
            if (month == 8) {
                return (date == 10) to true
            }
            if (month == 10) {
                return false to true
            }
        }

        if (year == 2021) {
            if (month == 7) {
                return when (date) {
                    22, 23 -> return true to true
                    else -> false to true
                }
            }
            if (month == 8) {
                return (date == 9) to true
            }
            if (month == 10) {
                return false to true
            }
        }
        return false to false
    }

}