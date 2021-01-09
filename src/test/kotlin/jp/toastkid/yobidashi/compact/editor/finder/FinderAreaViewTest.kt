package jp.toastkid.yobidashi.compact.editor.finder

import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import kotlinx.coroutines.channels.Channel
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class FinderAreaViewTest {

    private lateinit var finderAreaView: FinderAreaView

    @MockK
    private lateinit var orderChannel: Channel<FindOrder>

    @MockK
    private lateinit var messageChannel: Channel<String>

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun test() {
        finderAreaView = FinderAreaView(orderChannel, messageChannel)
        assertNotNull(finderAreaView.view())
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

}