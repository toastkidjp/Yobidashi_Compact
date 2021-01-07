package jp.toastkid.yobidashi.compact.model

import jp.toastkid.yobidashi.compact.calendar.service.OffDayFinderService
import java.time.DayOfWeek
import java.time.LocalDate

class ArticleTemplate {
    operator fun invoke(header: String) = """# $header
時分起床。起きた時の室温は度だった。

## 今日のプログラミング
追記

## 朝食
追記

## 今日の資産運用
評価額は円、評価損益は円(%)だった。
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
        val now = LocalDate.now()
        val dayOfWeek = now.dayOfWeek
        return dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY
                && OffDayFinderService().invoke(now.year, now.monthValue, now.dayOfMonth, dayOfWeek.value).not()
    }

}
