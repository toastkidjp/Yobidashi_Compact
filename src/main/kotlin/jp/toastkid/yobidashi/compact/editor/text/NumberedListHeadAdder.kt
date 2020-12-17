package jp.toastkid.yobidashi.compact.editor.text

class NumberedListHeadAdder {

    /**
     * @param str Nullable [CharSequence]
     */
    operator fun invoke(text: String?): String? {
        if (text.isNullOrEmpty()) {
            return text
        }

        return text.split("\n")
                .mapIndexed { index, s -> "${index + 1}. $s" }
                .reduceRight { s, acc -> "$s\n$acc" }
    }

}