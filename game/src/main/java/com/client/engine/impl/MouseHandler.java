package com.client.engine.impl;

import com.client.Client;
import com.client.graphics.interfaces.impl.Slider;
import com.client.util.MonotonicClock;
import net.runelite.rs.api.RSMouseHandler;

import javax.swing.*;
import java.awt.event.*;

public class MouseHandler implements MouseListener, MouseMotionListener, FocusListener, RSMouseHandler {

    private int isInEvent;

    public static MouseHandler instance;

    public static volatile int idleCycles;

    public static volatile int currentButton;

    public static volatile int MouseHandler_xVolatile;

    public static volatile int MouseHandler_yVolatile;

    public static volatile long MouseHandler_lastMovedVolatile;

    public static int MouseHandler_currentButton;

    public static int mouseX;

    public static int mouseY;

    public static long MouseHandler_millis;

    public static volatile int MouseHandler_lastButtonVolatile;

    public static volatile int MouseHandler_lastPressedXVolatile;

    public static volatile int MouseHandler_lastPressedYVolatile;

    public static volatile long MouseHandler_lastPressedTimeMillisVolatile;

    public static int clickMode3;

    public static int saveClickX;

    public static int saveClickY;

    public static long lastPressed;


    public static int clickMode2;

    static {
        instance = new MouseHandler();
        idleCycles = 0;
        currentButton = 0;
        MouseHandler_xVolatile = -1;
        MouseHandler_yVolatile = -1;
        MouseHandler_lastMovedVolatile = -1L;
        MouseHandler_currentButton = 0;
        mouseX = 0;
        mouseY = 0;
        MouseHandler_millis = 0L;
        MouseHandler_lastButtonVolatile = 0;
        MouseHandler_lastPressedXVolatile = 0;
        MouseHandler_lastPressedYVolatile = 0;
        MouseHandler_lastPressedTimeMillisVolatile = 0L;
        clickMode3 = 0;
        saveClickX = 0;
        saveClickY = 0;
        lastPressed = 0L;
    }

    MouseHandler() {
    }

    final int getButton(MouseEvent var1) {
        int var2 = var1.getButton();
        if (!var1.isAltDown() && var2 != 2) {
            return !var1.isMetaDown() && var2 != 3 ? 1 : 2;
        } else {
            return 4;
        }
    }

    public final synchronized void mouseMoved(MouseEvent mouseEvent) {
        if (isInEvent == 0)
        {
            mouseEvent = Client.instance.getCallbacks().mouseMoved(mouseEvent);
        }
        if (!mouseEvent.isConsumed())
        {
            isInEvent++;
            try
            {
                if (instance != null) {
                    idleCycles = 0;
                    MouseHandler_xVolatile = mouseEvent.getX();
                    MouseHandler_yVolatile = mouseEvent.getY();
                    MouseHandler_lastMovedVolatile = mouseEvent.getWhen();
                }

            }
            finally
            {
                isInEvent--;
            }
        }

    }

    public final synchronized void mousePressed(MouseEvent mouseEvent) {
        if (isInEvent == 0)
        {
            mouseEvent = Client.instance.getCallbacks().mousePressed(mouseEvent);
        }
        if (!mouseEvent.isConsumed())
        {
            isInEvent++;
            try
            {
                if (instance != null) {
                    idleCycles = 0;
                    MouseHandler_lastPressedXVolatile = mouseEvent.getX();
                    MouseHandler_lastPressedYVolatile = mouseEvent.getY();
                    MouseHandler_lastPressedTimeMillisVolatile = MonotonicClock.currentTimeMillis();
                    MouseHandler_lastButtonVolatile = this.getButton(mouseEvent);
                    if (MouseHandler_lastButtonVolatile != 0) {
                        currentButton = MouseHandler_lastButtonVolatile;
                    }
                    clickMode2 = SwingUtilities.isRightMouseButton(mouseEvent) ? 2 : 1;
                }

                Slider.handleSlider(mouseX, mouseY);
                if (mouseEvent.isPopupTrigger()) {
                    mouseEvent.consume();
                }
            }
            finally
            {
                isInEvent--;
            }
        }
    }

    public final synchronized void mouseReleased(MouseEvent mouseEvent) {
        if (isInEvent == 0)
        {
            mouseEvent = Client.instance.getCallbacks().mouseReleased(mouseEvent);
        }
        if (!mouseEvent.isConsumed())
        {
            isInEvent++;
            try
            {
                if (instance != null) {
                    idleCycles = 0;
                    currentButton = 0;
                    clickMode2 = 0;
                }

                if (mouseEvent.isPopupTrigger()) {
                    mouseEvent.consume();
                }
            }
            finally
            {
                isInEvent--;
            }
        }
    }

    public final void mouseClicked(MouseEvent var1) {
        var1 = Client.instance.getCallbacks().mouseClicked(var1);
        if (!var1.isConsumed())
        {
            if (var1.isPopupTrigger()) {
                var1.consume();
            }
        }

    }

    public final synchronized void mouseEntered(MouseEvent mouseEvent) {
        if (isInEvent == 0)
        {
            mouseEvent = Client.instance.getCallbacks().mouseEntered(mouseEvent);
        }
        if (!mouseEvent.isConsumed())
        {
            isInEvent++;
            try
            {
                this.mouseMoved(mouseEvent);
            }
            finally
            {
                isInEvent--;
            }
        }
    }

    public final synchronized void mouseDragged(MouseEvent mouseEvent) {
        if (isInEvent == 0)
        {
            mouseEvent = Client.instance.getCallbacks().mouseDragged(mouseEvent);
        }
        if (!mouseEvent.isConsumed())
        {
            isInEvent++;
            try
            {
                this.mouseMoved(mouseEvent);
            }
            finally
            {
                isInEvent--;
            }
        }
    }

    public final void focusGained(FocusEvent var1) {
    }

    public final synchronized void mouseExited(MouseEvent mouseEvent) {
        if (isInEvent == 0)
        {
            mouseEvent = Client.instance.getCallbacks().mouseExited(mouseEvent);
        }
        if (!mouseEvent.isConsumed())
        {
            isInEvent++;
            try
            {
                if (instance != null) {
                    idleCycles = 0;
                    MouseHandler_xVolatile = -1;
                    MouseHandler_yVolatile = -1;
                    MouseHandler_lastMovedVolatile = mouseEvent.getWhen();
                }

            }
            finally
            {
                isInEvent--;
            }
        }


    }

    public final synchronized void focusLost(FocusEvent var1) {
        if (instance != null) {
            currentButton = 0;
        }

    }


}
