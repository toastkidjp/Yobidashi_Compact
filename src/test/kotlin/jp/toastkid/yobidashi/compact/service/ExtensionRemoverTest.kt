package jp.toastkid.yobidashi.compact.service

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ExtensionRemoverTest {

    private lateinit var extensionRemover: ExtensionRemover

    @BeforeEach
    fun setUp() {
        extensionRemover = ExtensionRemover()
    }

    @Test
    fun test() {
        assertNull(extensionRemover.invoke(null))
        assertEquals("", extensionRemover.invoke(""))
        assertEquals("tomato", extensionRemover.invoke("tomato"))
        assertEquals("tomato", extensionRemover.invoke("tomato.txt"))
        assertEquals("tomato.exe", extensionRemover.invoke("tomato.exe.txt"))
    }
}