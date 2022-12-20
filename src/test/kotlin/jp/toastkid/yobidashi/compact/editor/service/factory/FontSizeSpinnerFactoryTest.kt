package jp.toastkid.yobidashi.compact.editor.service.factory

import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.mockkObject
import io.mockk.unmockkAll
import io.mockk.verify
import jp.toastkid.yobidashi.compact.model.Setting
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import javax.swing.JComboBox

internal class FontSizeSpinnerFactoryTest {

    @InjectMockKs
    private lateinit var fontSizeSpinnerFactory: FontSizeSpinnerFactory

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)

        mockkConstructor(JComboBox::class)
        every { anyConstructed<JComboBox<Int>>().addItem(any()) }.just(Runs)
        every { anyConstructed<JComboBox<Int>>().addItemListener(any()) }.just(Runs)
        every { anyConstructed<JComboBox<Int>>().selectedItem = any() }.just(Runs)

        mockkObject(Setting)
        every { Setting.editorFontSize() }.returns(10)
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun testInvoke() {
        fontSizeSpinnerFactory.invoke(mockk())

        verify(atLeast = 1) { anyConstructed<JComboBox<Int>>().addItem(any()) }
        verify(exactly = 1) { anyConstructed<JComboBox<Int>>().addItemListener(any()) }
        verify(exactly = 1) { anyConstructed<JComboBox<Int>>().selectedItem = any() }
    }

}