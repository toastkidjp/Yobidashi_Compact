package jp.toastkid.yobidashi.compact.calendar.service

class EquinoxDayCalculator {

    fun calculateVernalEquinoxDay(year: Int): Int {
        return (20.8431 + 0.242194 * (year - 1980)).toInt() - ((year - 1980) / 4).toInt()
    }

    fun calculateAutumnalEquinoxDay(year: Int): Int {
        return (23.2488 + 0.242194 * (year - 1980)).toInt() - ((year - 1980) / 4).toInt()
    }

}
