package jp.toastkid.yobidashi.compact.editor.service.factory

import jp.toastkid.yobidashi.compact.model.Setting
import javax.swing.JComboBox

class FontSizeSpinnerFactory {

    operator fun invoke(applyColorSetting: () -> Unit): JComboBox<Int> {
        val sizeSpinner = JComboBox<Int>()
        (9..20).forEach {
            sizeSpinner.addItem(it)
        }
        sizeSpinner.addItemListener {
            Setting.setEditorFontSize(Integer.parseInt(it.item?.toString()))
            applyColorSetting()
        }
        sizeSpinner.selectedItem = Setting.editorFontSize()
        return sizeSpinner
    }

}