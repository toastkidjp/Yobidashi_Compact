package jp.toastkid.yobidashi.compact.editor.service

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class LinkDecoratorServiceTest {

    @Test
    fun test() {
        assertEquals("www.yahoo.co.jp", LinkDecoratorService().invoke("www.yahoo.co.jp"))
        assertEquals("[Yahoo! JAPAN](https://www.yahoo.co.jp)", LinkDecoratorService().invoke("https://www.yahoo.co.jp"))
    }

}