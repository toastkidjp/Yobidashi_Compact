package jp.toastkid.yobidashi.compact.service

class CloseActionService(
        private val tabCount: () -> Int,
        private val closeTab: () -> Unit,
        private val closeWindow: () -> Unit
) {

    operator fun invoke() {
        if (tabCount() <= 1) {
            closeWindow()
            return
        }

        closeTab()
    }

}