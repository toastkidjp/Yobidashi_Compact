package jp.toastkid.yobidashi.compact.calendar.service

class SpecialCaseOffDayCalculator {

    operator fun invoke(year: Int, month: Int, date: Int): Boolean {
        if (year == 2019 && month == 5 && date == 1) {
            return true
        }

        if (year == 2020) {
            if (month == 7) {
                return when (date) {
                    23, 24 -> return true
                    else -> false
                }
            }
            if (month == 8) {
                return date == 10
            }
            if (month == 10) {
                return false
            }
        }

        if (year == 2021) {
            if (month == 7) {
                return when (date) {
                    22, 23 -> return true
                    else -> false
                }
            }
            if (month == 8) {
                return date == 9
            }
            if (month == 10) {
                return false
            }
        }
        return false
    }

}