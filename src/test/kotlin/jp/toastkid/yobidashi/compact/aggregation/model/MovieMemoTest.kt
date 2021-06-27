package jp.toastkid.yobidashi.compact.aggregation.model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class MovieMemoTest {

    @Test
    fun testHeader() {
        assertEquals(2, MovieMemo.header().size)
    }

}