package com.client.definitions.healthbar;

import com.client.Client;
import com.client.Rasterizer2D;
import com.client.Sprite;
import com.client.collection.iterable.IterableNodeDeque;
import com.client.collection.node.Node;

import static com.client.Client.viewportOffsetX;
import static com.client.Client.viewportOffsetY;

public class HealthBar extends Node {

    public HealthBarDefinition definition;

    private IterableNodeDeque updates;

    public HealthBar(HealthBarDefinition definition) {
        this.updates = new IterableNodeDeque();
        this.definition = definition;
    }

    public void put(int cycle, int endCycle, int cycleOffset, int healthValue) {
        HealthBarUpdate update = null;
        int updateCount = 0;

        for (HealthBarUpdate barUpdate = (HealthBarUpdate)this.updates.last(); barUpdate != null; barUpdate = (HealthBarUpdate)this.updates.previous()) {
            ++updateCount;
            if (barUpdate.cycle == cycle) {
                barUpdate.set(cycle, endCycle, cycleOffset, healthValue);
                return;
            }

            if (barUpdate.cycle <= cycle) {
                update = barUpdate;
            }
        }

        if (update == null) {
            if (updateCount < 4) {
                this.updates.addLast(new HealthBarUpdate(cycle, endCycle, cycleOffset, healthValue));
            }

        } else {
            IterableNodeDeque.IterableNodeDeque_addBefore(new HealthBarUpdate(cycle, endCycle, cycleOffset, healthValue), update);
            if (updateCount >= 4) {
                this.updates.last().remove();
            }

        }
    }

    public HealthBarUpdate get(int id) {
        HealthBarUpdate barUpdate = (HealthBarUpdate)this.updates.last();
        if (barUpdate != null && barUpdate.cycle <= id) {
            for (HealthBarUpdate update = (HealthBarUpdate)this.updates.previous(); update != null && update.cycle <= id; update = (HealthBarUpdate)this.updates.previous()) {
                barUpdate.remove();
                barUpdate = update;
            }

            if (this.definition.int5 + barUpdate.cycle + barUpdate.cycleOffset > id) {
                return barUpdate;
            } else {
                barUpdate.remove();
                return null;
            }
        } else {
            return null;
        }
    }

    public boolean isEmpty() {
        return this.updates.hasActiveHealthBars();
    }

    public int calculateOpacity(HealthBarUpdate healthUpdate, HealthBarDefinition definition, int tick) {
        int opacity = 255;
        int elapsedTicks = tick - healthUpdate.cycle;
        if (healthUpdate.cycleOffset + definition.int5 - elapsedTicks <= 0 && definition.int3 >= 0) {
            opacity = ((healthUpdate.cycleOffset + definition.int5 - elapsedTicks) << 8) / (definition.int5 - definition.int3);
        }
        return opacity;
    }

    public int calculateRenderedWidth(HealthBarUpdate healthUpdate, HealthBarDefinition definition, int elapsedTicks, int barWidth) {
        int healthWidth = barWidth * healthUpdate.health2 / definition.width;
        if (healthUpdate.cycleOffset > elapsedTicks) {
            int partialProgress = (definition.field1998 == 0) ? 0 : definition.field1998 * (elapsedTicks / definition.field1998);
            int initialWidth = barWidth * healthUpdate.health / definition.width;
            return partialProgress * (healthWidth - initialWidth) / healthUpdate.cycleOffset + initialWidth;
        } else {
            return healthUpdate.health2 > 0 && healthWidth < 1 ? 1 : healthWidth;
        }
    }

    public int drawHealthBar(Sprite backSprite, Sprite frontSprite, int barWidth, int padding, int renderedWidth, int opacity, int currentOffset) {
        if (backSprite != null && frontSprite != null) {
            if (barWidth == renderedWidth) {
                renderedWidth += padding * 2;
            } else {
                renderedWidth += padding;
            }

            int spriteHeight = backSprite.myHeight;
            currentOffset += spriteHeight;
            int offsetX = viewportOffsetX + Client.instance.viewportTempX - (barWidth >> 1) - padding;
            int offsetY = viewportOffsetY + Client.instance.viewportTempY - currentOffset;

            drawSpriteWithOpacity(backSprite, offsetX, offsetY, opacity);
            expandAndDrawSpriteWithOpacity(frontSprite, renderedWidth, offsetX, offsetY, spriteHeight, opacity);

            Rasterizer2D.setClip(viewportOffsetX, viewportOffsetY, viewportOffsetX + Client.viewportWidth, viewportOffsetY + Client.viewportHeight);
            return currentOffset + 2;
        } else {
            return drawRectangleBar(barWidth, renderedWidth, currentOffset);
        }
    }

    private void drawSpriteWithOpacity(Sprite sprite, int x, int y, int opacity) {
        if (opacity >= 0 && opacity < 255) {
            sprite.drawAdvancedSprite(x, y, opacity);
        } else {
            sprite.drawSprite(x, y);
        }
    }

    private void expandAndDrawSpriteWithOpacity(Sprite sprite, int width, int x, int y, int height, int opacity) {
        Rasterizer2D.expandClip(x, y, x + width, y + height);
        drawSpriteWithOpacity(sprite, x, y, opacity);
    }

    private int drawRectangleBar(int barWidth, int renderedWidth, int currentOffset) {
        currentOffset += 5;
        if (Client.instance.viewportTempX > -1) {
            int offsetX = viewportOffsetX + Client.instance.viewportTempX - (barWidth >> 1);
            int offsetY = viewportOffsetY + Client.instance.viewportTempY - currentOffset;
            Rasterizer2D.fillRectangle(offsetX, offsetY, renderedWidth, 5, 65280);
            Rasterizer2D.fillRectangle(renderedWidth + offsetX, offsetY, barWidth - renderedWidth, 5, 16711680);
        }
        return currentOffset + 2;
    }

}