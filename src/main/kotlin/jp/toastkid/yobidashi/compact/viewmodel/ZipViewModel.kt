package jp.toastkid.yobidashi.compact.viewmodel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.swing.Swing

object ZipViewModel {

    private val zip = Channel<Unit>()

    fun zip() {
        CoroutineScope(Dispatchers.Default).launch { zip.send(Unit) }
    }

    fun observe(onNext: () -> Unit) {
        CoroutineScope(Dispatchers.Swing).launch { zip.receiveAsFlow().collect { onNext() } }
    }

}