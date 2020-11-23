package jp.toastkid.yobidashi.compact.editor.model

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class EditingTest {

    @Test
    fun test() {
        val editing = Editing()
        assertFalse(editing.shouldShowIndicator())

        editing.setCurrentSize(1)
        assertFalse(editing.shouldShowIndicator())

        editing.setCurrentSize(1)
        assertFalse(editing.shouldShowIndicator())

        editing.setCurrentSize(2)
        assertTrue(editing.shouldShowIndicator())

        editing.setCurrentSize(2)
        assertTrue(editing.shouldShowIndicator())

        editing.clear()
        assertFalse(editing.shouldShowIndicator())
    }

}