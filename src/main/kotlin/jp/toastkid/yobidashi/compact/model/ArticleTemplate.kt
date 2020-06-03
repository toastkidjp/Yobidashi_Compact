package jp.toastkid.yobidashi.compact.model

class ArticleTemplate {
    operator fun invoke(header: String) = """# $header

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

## 消灯
時分に消灯し、寝る。

## 今日の日経平均株価終値
円(円高安)

## 家計簿
| 品目 | 金額 |
|:---|:---|
| | 円
| | 円

"""
}