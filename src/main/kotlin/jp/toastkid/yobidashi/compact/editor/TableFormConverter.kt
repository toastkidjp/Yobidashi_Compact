package jp.toastkid.yobidashi.compact.editor

class TableFormConverter {

    operator fun invoke(text: String) =
            "| " + text.replace(" ", " | ").replace("\n", "\n| ")
}