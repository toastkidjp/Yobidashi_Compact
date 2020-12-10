package jp.toastkid.yobidashi.compact.model

enum class Sorting(val text: String) {
    LAST_MODIFIED("Last modified"), TITLE("Title");

    companion object {
        fun findByName(name: String?) = values().find { it.name == name } ?: LAST_MODIFIED
    }
}