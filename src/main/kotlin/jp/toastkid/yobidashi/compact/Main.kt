package jp.toastkid.yobidashi.compact

import jp.toastkid.yobidashi.compact.model.Setting
import jp.toastkid.yobidashi.compact.view.MainFrame

/**
 * Created by toastkidjp on 2020/03/23.
 */
object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        val frame = MainFrame("Yobidashi Compact")

        frame.setBounds(200, 200, 400, 800)
        frame.isVisible = true
        Runtime.getRuntime().addShutdownHook(Thread {
            Setting.save()
        })
    }
}
