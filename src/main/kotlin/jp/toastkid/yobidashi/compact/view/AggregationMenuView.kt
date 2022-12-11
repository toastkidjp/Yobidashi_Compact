package jp.toastkid.yobidashi.compact.view

import jp.toastkid.yobidashi.compact.aggregation.service.AggregationMenuItemGeneratorService
import jp.toastkid.yobidashi.compact.aggregation.service.ArticleLengthAggregatorService
import jp.toastkid.yobidashi.compact.aggregation.service.CompoundInterestCalculatorMenuGeneratorService
import jp.toastkid.yobidashi.compact.aggregation.service.EatingOutCounterService
import jp.toastkid.yobidashi.compact.aggregation.service.MovieMemoSubtitleExtractor
import jp.toastkid.yobidashi.compact.aggregation.service.Nikkei225AggregatorService
import jp.toastkid.yobidashi.compact.aggregation.service.OutgoAggregatorService
import jp.toastkid.yobidashi.compact.aggregation.service.StepsAggregatorService
import jp.toastkid.yobidashi.compact.aggregation.service.StocksAggregatorService
import java.awt.Event
import java.awt.event.InputEvent
import java.awt.event.KeyEvent
import javax.swing.JMenu
import javax.swing.KeyStroke

class AggregationMenuView(
        private val aggregationMenuItemGeneratorService: AggregationMenuItemGeneratorService = AggregationMenuItemGeneratorService(),
        private val compoundInterestCalculatorMenuGeneratorService: CompoundInterestCalculatorMenuGeneratorService = CompoundInterestCalculatorMenuGeneratorService()
) {

    operator fun invoke(): JMenu {
        val menu = JMenu("Aggregate")

        menu.add(
                aggregationMenuItemGeneratorService.invoke(
                        "OutGo",
                        "Please input year and month you want aggregate outgo?",
                        { OutgoAggregatorService().invoke(it) },
                        KeyStroke.getKeyStroke(KeyEvent.VK_G, Event.CTRL_MASK)
                )
        )
        menu.add(
            aggregationMenuItemGeneratorService.invoke(
                "Eat out count",
                "Please input year and month you want count eat-out times?",
                { EatingOutCounterService().invoke(it) }
            )
        )
        menu.add(
                aggregationMenuItemGeneratorService.invoke(
                        "Article length",
                        "Please input year and month you want aggregate article length?",
                        { ArticleLengthAggregatorService().invoke(it) },
                        KeyStroke.getKeyStroke(KeyEvent.VK_L, Event.CTRL_MASK)
                )
        )
        menu.add(
                aggregationMenuItemGeneratorService.invoke(
                    "Movie memo",
                    "Please input year and month you want aggregate movies? ex)",
                    { MovieMemoSubtitleExtractor().invoke(it) },
                    KeyStroke.getKeyStroke(KeyEvent.VK_M, Event.CTRL_MASK)
                )
        )
        menu.add(
            aggregationMenuItemGeneratorService.invoke(
                "Steps",
                "Please input year and month you want aggregate count of steps? ex)",
                { StepsAggregatorService().invoke(it) },
                KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.SHIFT_MASK)
            )
        )
        menu.add(
            aggregationMenuItemGeneratorService.invoke(
                "Stock",
                "Please input year and month you want aggregate stocks? ex)",
                { StocksAggregatorService().invoke(it) },
                KeyStroke.getKeyStroke(KeyEvent.VK_3, Event.CTRL_MASK)
            )
        )
        menu.add(
                aggregationMenuItemGeneratorService.invoke(
                        "Nikkei 225",
                        "Please input year and month you want aggregate Nikkei 225? ex)",
                        { Nikkei225AggregatorService().invoke(it) },
                        KeyStroke.getKeyStroke(KeyEvent.VK_2, Event.CTRL_MASK)
                )
        )
        menu.add(compoundInterestCalculatorMenuGeneratorService.invoke())

        return menu
    }

}