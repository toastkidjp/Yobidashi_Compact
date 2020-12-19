package jp.toastkid.yobidashi.compact.editor

class TableFormConverter {

    operator fun invoke(text: String) =
            "| ${text.trim().replace(" ", " | ").replace("\n", "\n| ")}\n"

}