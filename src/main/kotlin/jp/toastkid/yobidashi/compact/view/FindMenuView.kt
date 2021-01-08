package jp.toastkid.yobidashi.compact.view

import jp.toastkid.yobidashi.compact.service.ArticleFinderService
import java.awt.event.InputEvent
import java.awt.event.KeyEvent
import javax.swing.JMenu
import javax.swing.JMenuItem
import javax.swing.KeyStroke

/**
 * TODO Divide ArticleFinderService.
 */
class FindMenuView {

    operator fun invoke(): JMenu {
        return JMenu("Find").also { it.add(makeMenuItem()) }
    }

    private fun makeMenuItem(): JMenuItem {
        val item = JMenuItem()
        item.hideActionText = true
        item.addActionListener {
            ArticleFinderService().invoke()
        }
        item.accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_MASK)
        item.text = "Find article"
        return item
    }

}