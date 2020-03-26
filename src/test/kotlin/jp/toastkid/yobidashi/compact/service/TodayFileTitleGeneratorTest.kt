package jp.toastkid.yobidashi.compact.service

import jp.toastkid.yobidashi.compact.service.TodayFileTitleGenerator
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TodayFileTitleGeneratorTest {

    @Test
    fun test() {
        assertEquals(
                "日記2020-03-24(火)",
                TodayFileTitleGenerator().invoke(1584982800366L)
        )
    }
}