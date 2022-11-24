package jp.toastkid.yobidashi.compact.model

import jp.toastkid.yobidashi.compact.calendar.service.OffDayFinderService
import java.time.DayOfWeek
import java.time.LocalDate

class ArticleTemplate {
    operator fun invoke(header: String) = """# $header
時分起床。起きた時の室温は度だった。

${ if (isStockDay()) {
"""
## 今日の資産運用
評価額は円、評価損益は円(%)だった。
追記

### 指数
S&P 500は
[VIX](https://www.bloomberg.co.jp/quote/VIX:IND) は 

""".trimIndent()    
} else "" }

## 朝食
追記からおかめ納豆極小粒1パックとふんわり食パン2枚を食べる。食後にインスタントコーヒーを飲む。

## 今日のプログラミング
追記

${ if (isNotOffDay()) {
"""

## 午前の仕事
に打刻する。

追記
""".trimIndent()
} else ""}

## 昼食
食後に一口チョコレート2個を食べ、冷やしたインスタントコーヒーを飲む。
追記
${ if (isNotOffDay()) {
        """

## 『じゅん散歩』「」
追記

## 午後の仕事

追記

## 勤務終了
に業務を終了する。
追記
""".trimIndent()
    } else ""}

## 夕食
からおかめ納豆極小粒1パックとふんわり食パン2枚を食べる。
追記
${ if (isNotOffDay()) {
        """

## 今晩のWBS
がスタジオ出演していた。

- アメリカ合衆国の株価指数は
- ドル円は
- 長期国債は%
- 金はドル
- NY原油先物はドル
- トレンドたまごは

### クロージングトーク

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

    private fun isStockDay(): Boolean {
        val now = LocalDate.now()
        val dayOfWeek = now.dayOfWeek
        return dayOfWeek != DayOfWeek.MONDAY && dayOfWeek != DayOfWeek.SUNDAY
                && OffDayFinderService().invoke(now.year, now.monthValue, now.dayOfMonth, dayOfWeek.value).not()
    }

}
