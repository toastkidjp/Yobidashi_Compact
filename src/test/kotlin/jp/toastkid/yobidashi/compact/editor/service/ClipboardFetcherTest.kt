package jp.toastkid.yobidashi.compact.editor.service

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.awt.datatransfer.Clipboard
import java.awt.datatransfer.Transferable

internal class ClipboardFetcherTest {

    private lateinit var clipboardFetcher: ClipboardFetcher

    @MockK
    private lateinit var clipboard: Clipboard

    @MockK
    private lateinit var transferable: Transferable

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)

        every { clipboard.getContents(any()) }.returns(transferable)

        clipboardFetcher = ClipboardFetcher(clipboard)
        /*
        n if (transferData?.isDataFlavorSupported(DataFlavor.stringFlavor) == false) null
        else transferData.getTransferData(DataFlavor.stringFlavor).toString()
         */
    }

    @Test
    fun testReturnNull() {
        every { transferable.isDataFlavorSupported(any()) }.returns(false)

        assertNull(clipboardFetcher.invoke())
    }

    @Test
    fun testContentIsNull() {
        every { clipboard.getContents(any()) }.returns(null)
        every { transferable.isDataFlavorSupported(any()) }.returns(false)

        assertNull(clipboardFetcher.invoke())
    }

    @Test
    fun test() {
        every { transferable.isDataFlavorSupported(any()) }.returns(true)
        every { transferable.getTransferData(any()) }.returns("test")

        assertEquals("test", clipboardFetcher.invoke())
    }

}