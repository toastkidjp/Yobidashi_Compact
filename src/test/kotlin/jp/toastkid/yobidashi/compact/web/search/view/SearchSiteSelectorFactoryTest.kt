package jp.toastkid.yobidashi.compact.web.search.view

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.unmockkAll
import io.mockk.verify
import jp.toastkid.yobidashi.compact.web.search.model.SearchSite
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import javax.swing.ComboBoxModel
import javax.swing.JComboBox
import javax.swing.ListCellRenderer

internal class SearchSiteSelectorFactoryTest {

    private lateinit var searchSiteSelectorFactory: SearchSiteSelectorFactory

    @MockK
    private lateinit var valuesFactory: () -> Array<SearchSite>

    @MockK
    private lateinit var modelFactory: () -> ComboBoxModel<SearchSite>

    @MockK
    private lateinit var rendererFactory: () -> ListCellRenderer<SearchSite>

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)

        searchSiteSelectorFactory = SearchSiteSelectorFactory(valuesFactory, modelFactory, rendererFactory)

        mockkConstructor(JComboBox::class)
        every { anyConstructed<JComboBox<Any>>().setModel(any()) }.answers { Unit }
        every { anyConstructed<JComboBox<Any>>().setRenderer(any()) }.answers { Unit }
        every { anyConstructed<JComboBox<Any>>().setPreferredSize(any()) }.answers { Unit }

        every { valuesFactory.invoke() }.answers { arrayOf(SearchSite.YAHOO_JAPAN) }
        every { modelFactory.invoke() }.answers { mockk() }
        every { rendererFactory.invoke() }.answers { mockk() }
    }

    @Test
    fun test() {
        searchSiteSelectorFactory.invoke()

        verify(exactly = 1) { anyConstructed<JComboBox<Any>>().setModel(any()) }
        verify(exactly = 1) { anyConstructed<JComboBox<Any>>().setRenderer(any()) }
        verify(exactly = 1) { anyConstructed<JComboBox<Any>>().setPreferredSize(any()) }
        verify(exactly = 1) { valuesFactory.invoke() }
        verify(exactly = 1) { modelFactory.invoke() }
        verify(exactly = 1) { rendererFactory.invoke() }
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

}