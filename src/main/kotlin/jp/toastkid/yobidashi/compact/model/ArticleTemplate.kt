package jp.toastkid.yobidashi.compact.model

import java.util.Calendar

class ArticleTemplate {
    operator fun invoke(header: String) = """# $header
時分起床。起きた時の室温は度だった。

## 今日のプログラミング
追記

## 朝食
追記
${ if (isNotOffDay()) {
"""

## 午前の仕事
に打刻する。

追記
""".trimIndent()
} else ""}

## 昼食

追記
${ if (isNotOffDay()) {
        """

## 午後の仕事

追記

## 勤務終了

追記
""".trimIndent()
    } else ""}

## 夕食

追記
${ if (isNotOffDay()) {
        """

## 今晩のWBS

追記
""".trimIndent()
    } else ""}

## 室温

| 時刻 | 室温
|:---|:---

## 消灯
寝る前の室温は度だった。時分に消灯し、寝る。
${ if (isNotOffDay()) {
        """

## 今日の日経平均株価終値
円(円高安)
""".trimIndent()
    } else ""}

## 家計簿
| 品目 | 金額 |
|:---|:---|
| | 円
| | 円
| | 円
| | 円

"""

    private fun isNotOffDay(): Boolean {
        val dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
        return dayOfWeek != Calendar.SATURDAY && dayOfWeek != Calendar.SUNDAY
    }

}
