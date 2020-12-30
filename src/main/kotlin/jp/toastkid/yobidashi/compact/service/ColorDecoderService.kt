package jp.toastkid.yobidashi.compact.service

import java.awt.Color

class ColorDecoderService {

    operator fun invoke(argbColorCode: String?): Color? {
        if (argbColorCode.isNullOrBlank()) {
            return null
        }

        val code = if (argbColorCode.startsWith("#")) argbColorCode else "#$argbColorCode"
        return Color(java.lang.Long.decode(code).toInt(), code.length > 7)
    }

}