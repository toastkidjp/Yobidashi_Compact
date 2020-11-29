package jp.toastkid.yobidashi.compact.calendar.model

import java.util.Calendar

enum class Month {
    JAN, FEB, MAR, APR, MAY, JUNE, JULY, AUG, SEP, OCT, NOV, DEC;

    fun isSameMonth(calendar: Calendar): Boolean {
        return calendar.get(Calendar.MONTH) == this.ordinal
    }

    fun previous(): Month {
        return values()[this.ordinal - 1]
    }

    fun next(): Month {
        return values()[this.ordinal + 1]
    }

    companion object {

        fun from(calendar: Calendar): Month {
            return values()[calendar.get(Calendar.MONTH)]
        }

        fun fromName(name: String): Month {
            return values().firstOrNull { it.name == name } ?: JAN
        }

    }
}