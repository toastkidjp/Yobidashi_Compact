package jp.toastkid.yobidashi.compact.editor.text

class TableFormConverter {

    operator fun invoke(text: String) =
            "| ${text.trim().replace(" ", " | ").replace("\n", "\n| ")}\n"

}