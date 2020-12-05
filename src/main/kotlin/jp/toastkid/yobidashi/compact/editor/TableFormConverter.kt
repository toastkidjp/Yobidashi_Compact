package jp.toastkid.yobidashi.compact.editor

class TableFormConverter {

    operator fun invoke(text: String) =
            "| " + text.trimStart().replace(" ", " | ").replace("\n", "\n| ")

}