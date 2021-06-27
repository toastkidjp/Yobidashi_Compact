package jp.toastkid.yobidashi.compact.aggregation.model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class MovieMemoTest {

    @Test
    fun testHeader() {
        assertEquals(2, MovieMemo.header().size)
    }

    @Test
    fun test() {
        assertEquals(2, MovieMemo("2021-06-27", "test").toArray().size)
    }

}