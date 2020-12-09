package jp.toastkid.yobidashi.compact.service

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class TodayFileTitleGeneratorTest {

    @Test
    fun test() {
        assertEquals(
                "2020-03-24(Tue)",
                TodayFileTitleGenerator().invoke(1584982800366L)
        )
    }
}