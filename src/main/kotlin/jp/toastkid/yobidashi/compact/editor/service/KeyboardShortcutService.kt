package jp.toastkid.yobidashi.compact.editor.service

import jp.toastkid.yobidashi.compact.editor.MenuCommand
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import java.awt.event.KeyEvent

class KeyboardShortcutService(private val channel: Channel<MenuCommand>) {

    operator fun invoke(e: KeyEvent) {
        if (e.isControlDown.not()) {
            return
        }

        CoroutineScope(Dispatchers.Default).launch {
            val command = when (e.keyCode) {
                KeyEvent.VK_T -> MenuCommand.TO_TABLE
                KeyEvent.VK_I -> MenuCommand.ITALIC
                KeyEvent.VK_B -> MenuCommand.BOLD
                KeyEvent.VK_PERIOD -> MenuCommand.BLOCKQUOTE
                KeyEvent.VK_CIRCUMFLEX -> MenuCommand.STRIKETHROUGH
                KeyEvent.VK_1 -> MenuCommand.ORDERED_LIST
                KeyEvent.VK_2 -> MenuCommand.TASK_LIST
                KeyEvent.VK_MINUS -> MenuCommand.UNORDERED_LIST
                KeyEvent.VK_AT -> MenuCommand.CODE_BLOCK
                else -> null
            } ?: return@launch
            channel.send(command)
        }
    }

}