package jp.toastkid.yobidashi.compact.editor.text

class ListHeadAdder {

    /**
     * @param str Nullable [CharSequence]
     * @param head
     */
    operator fun invoke(text: String?, head: String): String? {
        if (text.isNullOrEmpty()) {
            return text
        }
        return head + text.replace("\n", "\n $head")
    }

}