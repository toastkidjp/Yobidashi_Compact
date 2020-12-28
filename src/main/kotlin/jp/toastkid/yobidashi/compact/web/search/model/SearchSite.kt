package jp.toastkid.yobidashi.compact.web.search.model

import java.net.URI
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

enum class SearchSite(val siteName: String, private val searchUrlBase: String) {
    YAHOO_JAPAN("Yahoo! JAPAN", "https://search.yahoo.co.jp/search?p="),
    WIKIPEDIA("Wikipedia", "https://ja.wikipedia.org/w/index.php?search="),
    AMAZON("Amazon", "https://www.amazon.co.jp/s?k="),
    IMAGE_YAHOO_JAPAN("Image (Yahoo! JAPAN)", "https://search.yahoo.co.jp/image/search?p="),
    REALTIME_YAHOO_JAPAN("Realtime (Yahoo! JAPAN)", "https://search.yahoo.co.jp/realtime/search?p=")
    ;

    fun make(rawQuery: String): URI {
        return URI("$searchUrlBase${URLEncoder.encode(rawQuery, StandardCharsets.UTF_8.name())}")
    }

    companion object {
        fun getDefault() = YAHOO_JAPAN
    }

}