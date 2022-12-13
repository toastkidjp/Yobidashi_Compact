package jp.toastkid.yobidashi.compact

import jp.toastkid.yobidashi.compact.model.Setting
import jp.toastkid.yobidashi.compact.service.CommandLineArgumentService
import jp.toastkid.yobidashi.compact.view.MainFrame

/**
 * Created by toastkidjp on 2020/03/23.
 * @author toastkidjp
 */
fun main(array: Array<String>?) {
    if (array?.isNotEmpty() == true) {
        CommandLineArgumentService().invoke(array)
        return
    }
    val frame = MainFrame("Yobidashi Compact")
    frame.show()
    Runtime.getRuntime().addShutdownHook(Thread {
        Setting.save()
    })
}
