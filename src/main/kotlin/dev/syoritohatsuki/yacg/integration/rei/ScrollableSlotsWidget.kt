package dev.syoritohatsuki.yacg.integration.rei

import me.shedaniel.clothconfig2.ClothConfigInitializer
import me.shedaniel.clothconfig2.api.scroll.ScrollingContainer
import me.shedaniel.math.Point
import me.shedaniel.math.Rectangle
import me.shedaniel.rei.api.client.REIRuntime
import me.shedaniel.rei.api.client.gui.widgets.Slot
import me.shedaniel.rei.api.client.gui.widgets.WidgetWithBounds
import me.shedaniel.rei.api.client.gui.widgets.Widgets
import me.shedaniel.rei.api.common.entry.EntryIngredient
import net.minecraft.client.gui.DrawContext
import kotlin.math.ceil
import kotlin.math.roundToInt

//copy-paste from RoughlyEnoughLootTables who copy-paste from Beacon Payment and updated for 1.20+
class ScrollableSlotsWidget(private val bounds: Rectangle, ingredients: Collection<EntryIngredient>) :
    WidgetWithBounds() {
    private val widgets: List<Slot> = ingredients.map {
        Widgets.createSlot(Point(0, 0)).entries(it).disableBackground()
    }
    private val scrolling: ScrollingContainer

    init {
        scrolling = object : ScrollingContainer() {
            override fun getBounds(): Rectangle = Rectangle(this@ScrollableSlotsWidget.getBounds())
            override fun getMaxScrollHeight(): Int = (ceil(widgets.size / 8f) * 18).roundToInt()
        }
    }

    override fun getBounds(): Rectangle = bounds

    override fun children() = widgets

    override fun mouseScrolled(
        mouseX: Double,
        mouseY: Double,
        horizontalAmount: Double,
        verticalAmount: Double
    ): Boolean = if (containsMouse(mouseX, mouseY)) {
        scrolling.offset(ClothConfigInitializer.getScrollStep() * -horizontalAmount, true)
        true
    } else false


    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean =
        scrolling.updateDraggingState(mouseX, mouseY, button) || super.mouseClicked(mouseX, mouseY, button)

    override fun mouseDragged(mouseX: Double, mouseY: Double, button: Int, deltaX: Double, deltaY: Double): Boolean =
        scrolling.mouseDragged(mouseX, mouseY, button, deltaX, deltaY) || super.mouseDragged(
            mouseX, mouseY, button, deltaX, deltaY
        )


    @Suppress("UnstableApiUsage")
    override fun render(graphics: DrawContext, mouseX: Int, mouseY: Int, delta: Float) {
        scrolling.updatePosition(delta)
        scissor(graphics, scrolling.scissorBounds).use {
            for (y in 0 until ceil(widgets.size / 8f).roundToInt()) {
                for (x in 0..7) {
                    val index = y * 8 + x
                    if (widgets.size <= index) return@use
                    val widget = widgets[index]
                    widget.bounds.setLocation(
                        bounds.x + 1 + x * 18, bounds.y + 1 + y * 18 - scrolling.scrollAmountInt()
                    )
                    widget.render(graphics, mouseX, mouseY, delta)
                }
            }
        }
        scissor(graphics, scrolling.bounds).use {
            scrolling.renderScrollBar(
                graphics, -0x1000000, 1f, if (REIRuntime.getInstance().isDarkThemeEnabled) 0.8f else 1f
            )
        }
    }
}