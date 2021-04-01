package jp.toastkid.yobidashi.compact.service

import java.awt.Dimension
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import javax.swing.JTextField

class ArticleFilterViewFactoryService {

    operator fun invoke(action: (String) -> Unit): JTextField {
        val searchInput = JTextField()
        searchInput.addKeyListener(object : KeyListener {
            override fun keyTyped(e: KeyEvent?) = Unit

            override fun keyPressed(e: KeyEvent?) = Unit

            override fun keyReleased(e: KeyEvent?) {
                if (e?.keyCode == KeyEvent.VK_ENTER) {
                    action(searchInput.text)
                }
            }
        })
        searchInput.preferredSize = Dimension(600, 40)
        return searchInput
    }

}