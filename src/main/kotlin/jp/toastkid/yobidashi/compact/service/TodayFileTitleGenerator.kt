package jp.toastkid.yobidashi.compact.service

import java.text.SimpleDateFormat
import java.util.*

class TodayFileTitleGenerator {

    private val dateFormat = ThreadLocal.withInitial {
        return@withInitial SimpleDateFormat("yyyy-MM-dd(E)", Locale.ENGLISH)
    }

    operator fun invoke(ms: Long) =
            dateFormat.get().format(Date().also { it.time = ms })
}