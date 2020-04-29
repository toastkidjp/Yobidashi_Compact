package jp.toastkid.yobidashi.compact.service

import jp.toastkid.yobidashi.compact.model.Setting
import javax.swing.JFrame
import javax.swing.SwingUtilities
import javax.swing.UIManager

class UiUpdaterService {

    operator fun invoke(frame: JFrame, lookAndFeel: String) {
        try {
            UIManager.setLookAndFeel(lookAndFeel)
            Setting.setLookAndFeel(lookAndFeel)
            SwingUtilities.updateComponentTreeUI(frame)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}