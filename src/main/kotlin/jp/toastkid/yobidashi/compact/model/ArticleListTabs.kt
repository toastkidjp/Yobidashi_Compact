package jp.toastkid.yobidashi.compact.model

import jp.toastkid.yobidashi.compact.view.ArticleListView

class ArticleListTabs {

    private val tabs = mutableListOf<ArticleListView>()

    fun get(index: Int) = tabs[index]

    fun add(articleListView: ArticleListView) = tabs.add(articleListView)

}