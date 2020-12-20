package jp.toastkid.yobidashi.compact.web.search

import jp.toastkid.yobidashi.compact.service.UrlOpenerService
import jp.toastkid.yobidashi.compact.web.search.model.SearchSite
import jp.toastkid.yobidashi.compact.web.search.view.SearchSiteSelectorFactory
import javax.swing.BoxLayout
import javax.swing.JLabel
import javax.swing.JOptionPane
import javax.swing.JPanel

class WebSearchService(private val urlOpenerService: UrlOpenerService = UrlOpenerService()) {

    operator fun invoke() {
        val searchSiteSelector = SearchSiteSelectorFactory().invoke()
        val content = JPanel().also { panel ->
            panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)
            panel.add(JLabel("Please would you input search query?"))
            panel.add(searchSiteSelector)
        }
        val input = JOptionPane.showInputDialog(content)
        if (input.isNullOrBlank()) {
            return
        }
        (searchSiteSelector.selectedItem as? SearchSite)?.make(input)?.let { uri -> urlOpenerService(uri) }
    }

}