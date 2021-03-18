package jp.toastkid.yobidashi.compact.editor.text

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class ListHeadAdderTest {

    private val target = """1st line
2nd line""".trimIndent()

    private val expected = """- 1st line
- 2nd line

""".trimIndent()

    @Test
    fun test() {
        assertEquals(expected, ListHeadAdder().invoke(target, "-"))
    }

}