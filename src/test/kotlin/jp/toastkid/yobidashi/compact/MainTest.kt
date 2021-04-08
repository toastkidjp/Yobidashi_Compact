package jp.toastkid.yobidashi.compact

import io.mockk.every
import io.mockk.mockkConstructor
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.spyk
import io.mockk.unmockkAll
import io.mockk.verify
import jp.toastkid.yobidashi.compact.model.Setting
import jp.toastkid.yobidashi.compact.view.MainFrame
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class MainTest {

    @BeforeEach
    fun setUp() {
        mockkConstructor(MainFrame::class)
        every { anyConstructed<MainFrame>().show() }.answers { Unit }

        val runtime = spyk(Runtime.getRuntime())
        every { runtime.addShutdownHook(any()) }.answers { Unit }

        mockkStatic(Runtime::class)
        every { Runtime.getRuntime() }.returns(runtime)

        mockkObject(Setting)
        every { Setting.save() }.answers { Unit }
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun test() {
        main()

        verify { anyConstructed<MainFrame>().show() }
    }

}