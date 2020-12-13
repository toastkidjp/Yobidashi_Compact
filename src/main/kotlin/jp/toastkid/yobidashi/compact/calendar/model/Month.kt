package jp.toastkid.yobidashi.compact.calendar.model

import java.time.LocalDate

enum class Month {
    JAN, FEB, MAR, APR, MAY, JUNE, JULY, AUG, SEP, OCT, NOV, DEC;

    fun isSameMonth(date: LocalDate): Boolean {
        return date.monthValue - 1 == this.ordinal
    }

    companion object {

        fun from(date: LocalDate): Month {
            return values()[date.monthValue - 1]
        }

        fun fromName(name: String): Month {
            return values().firstOrNull { it.name == name } ?: JAN
        }

    }
}