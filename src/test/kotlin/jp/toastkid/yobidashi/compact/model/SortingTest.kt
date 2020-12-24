package jp.toastkid.yobidashi.compact.model

import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Test

internal class SortingTest {

    @Test
    fun test() {
        assertSame(Sorting.LAST_MODIFIED, Sorting.findByName(null))
        assertSame(Sorting.LAST_MODIFIED, Sorting.findByName(""))
        assertSame(Sorting.LAST_MODIFIED, Sorting.findByName("test"))
        assertSame(Sorting.LAST_MODIFIED, Sorting.findByName("title"))
        assertSame(Sorting.TITLE, Sorting.findByName("TITLE"))
        assertSame(Sorting.LAST_MODIFIED, Sorting.findByName("LAST_MODIFIED"))
    }

}