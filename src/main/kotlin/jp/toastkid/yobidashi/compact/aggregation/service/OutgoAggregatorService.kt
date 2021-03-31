package jp.toastkid.yobidashi.compact.aggregation.service

import jp.toastkid.yobidashi.compact.aggregation.model.OutgoAggregationResult
import jp.toastkid.yobidashi.compact.service.ArticlesReaderService
import java.nio.file.Files
import java.util.stream.Collectors

class OutgoAggregatorService(private val articlesReaderService: ArticlesReaderService = ArticlesReaderService()) {

    operator fun invoke(keyword: String): OutgoAggregationResult {
        val aggregationResult = OutgoAggregationResult(keyword)
        articlesReaderService.invoke()
                .parallel()
                .map { it.toFile().nameWithoutExtension to Files.readAllLines(it) }
                .filter { it.first.startsWith(keyword) }
                .map {
                    var isOutGoLine = false
                    for (line in it.second) {
                        if (line.startsWith("#") && line.endsWith("家計簿")) {
                            isOutGoLine = true
                        }

                        if (!isOutGoLine || !line.startsWith("|")) {
                            continue
                        }

                        val items = line.split("|")
                        val target = items.get(2)
                        var price = 0
                        if (target.endsWith(YEN_UNIT)) {
                            val priceStr = target.substring(0, target.indexOf(YEN_UNIT)).trim().replace(",", "")
                            if (priceStr.isNotBlank()) {
                                price = Integer.parseInt(priceStr)
                            }
                            aggregationResult.add(it.first, items.get(0) + items.get(1).trim(), price)
                        }
                    }
                }
                .collect(Collectors.toList())
        return aggregationResult
    }

    companion object {
        private const val YEN_UNIT = "円"
    }

}