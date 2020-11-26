package jp.toastkid.yobidashi.compact.model

class ArticleTemplate {
    operator fun invoke(header: String) = """# $header
時分起床。起きた時の室温は度だった。

追記

## 朝食


## 午前の仕事
に打刻する。

追記

## 昼食

追記

## 午後の仕事

追記

## 勤務終了

追記

## 夕食

追記

## 室温

| 時刻 | 室温
|:---|:---

## 消灯
寝る前の室温は度だった。時分に消灯し、寝る。

## 今日の日経平均株価終値
円(円高安)

## 家計簿
| 品目 | 金額 |
|:---|:---|
| | 円
| | 円

"""
}