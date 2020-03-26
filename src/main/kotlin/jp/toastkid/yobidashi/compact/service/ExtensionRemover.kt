package jp.toastkid.yobidashi.compact.service

class ExtensionRemover {

    operator fun invoke(fileName: String?): String? {
        if (fileName.isNullOrBlank() || !fileName.contains(".")) {
            return fileName
        }
        return fileName.substring(0, fileName.lastIndexOf("."))
    }

}