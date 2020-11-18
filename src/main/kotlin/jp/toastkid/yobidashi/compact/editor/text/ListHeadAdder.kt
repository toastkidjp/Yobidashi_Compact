package jp.toastkid.yobidashi.compact.editor.text

class ListHeadAdder {

    /**
     * Line separator.
     */
    private val lineSeparator = System.getProperty("line.separator") ?: "\n"

    /**
     * Invoke quotation function.
     *
     * @param str Nullable [CharSequence]
     */
    operator fun invoke(text: String?, head: String): String? {
        if (text.isNullOrEmpty()) {
            return text
        }
        return head + text.replace("\n", "\n $head")
    }
}