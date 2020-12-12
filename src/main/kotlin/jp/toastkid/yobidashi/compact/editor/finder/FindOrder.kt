package jp.toastkid.yobidashi.compact.editor.finder

data class FindOrder(
        val target: String,
        val replace: String,
        val upper: Boolean = false,
        val invokeReplace: Boolean = false,
        val caseSensitive: Boolean = true
)