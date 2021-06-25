package jp.toastkid.yobidashi.compact.editor.finder

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.swing.Swing
import javax.swing.JLabel

class MessageReceiverService(
    private val messageChannel: Channel<String>,
    private val message: JLabel
) {

    operator fun invoke() {
        CoroutineScope(Dispatchers.Swing).launch {
            messageChannel.receiveAsFlow().collect {
                message.text = it
            }
        }
    }

}