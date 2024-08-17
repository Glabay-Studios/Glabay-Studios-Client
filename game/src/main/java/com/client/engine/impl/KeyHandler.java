package com.client.engine.impl;

import com.client.Client;
import com.client.engine.keys.KeyEventProcessor;
import com.client.engine.keys.KeyEventWrapper;
import com.client.features.KeyboardAction;
import com.client.graphics.interfaces.impl.Keybinding;
import net.runelite.api.KeyCode;
import net.runelite.rs.api.RSKeyHandler;

import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Collection;

import static com.client.engine.GameEngine.keyCodes;

public class KeyHandler implements KeyListener, FocusListener, RSKeyHandler {

    public static boolean hasFocus;

    Collection<KeyEventWrapper> keyEventQueue;
    Collection<KeyEventWrapper> keyEventProcessingQueue;

    KeyEventProcessor[] keyEventProcessors;

    public boolean[] pressedKeys;

    public static volatile int idleCycles;


    public void setupComponent(Component component) {
        component.setFocusTraversalKeysEnabled(false);
        component.addKeyListener(this);
        component.addFocusListener(this);
    }

    synchronized void method362(Component component) {
        component.removeKeyListener(this);
        component.removeFocusListener(this);
        synchronized(this) {
            this.keyEventQueue.add(new KeyEventWrapper(4, 0));
        }
    }

    public KeyHandler() {
        this.keyEventProcessors = new KeyEventProcessor[3];
        this.pressedKeys = new boolean[112];
        idleCycles = 0;
        this.keyEventQueue = new ArrayList<>(100);
        this.keyEventProcessingQueue = new ArrayList<>(100);
    }


    public void processKeyEvents() {
        ++idleCycles;
        swapKeyEventQueues();
        for (KeyEventWrapper eventWrapper : keyEventProcessingQueue) {
            for (int i = 0; i < this.keyEventProcessors.length; i++) {
                if (eventWrapper.processWith(this.keyEventProcessors[i])) {
                    break;
                }
            }
        }
        this.keyEventProcessingQueue.clear();
    }

    synchronized void swapKeyEventQueues() {
        Collection<KeyEventWrapper> temp = this.keyEventProcessingQueue;
        this.keyEventProcessingQueue = this.keyEventQueue;
        this.keyEventQueue = temp;
    }

    public final synchronized void keyTyped(KeyEvent keyEvent) {
        Client.instance.getCallbacks().keyTyped(keyEvent);
        if (!keyEvent.isConsumed())
        {

            char var2 = keyEvent.getKeyChar();
            if (var2 != 0 && var2 != '\uffff' && method4577(var2)) {
                this.keyEventQueue.add(new KeyEventWrapper(3, var2));
            }

            keyEvent.consume();
        }
    }


    public final synchronized void keyReleased(KeyEvent keyEvent) {
        Client.instance.getCallbacks().keyReleased(keyEvent);
        if (!keyEvent.isConsumed())
        {
            int keyCode = keyEvent.getKeyCode();

            if (keyCode == KeyEvent.VK_SHIFT) {
                Client.shiftDown = false;
            }
            if (keyCode == KeyEvent.VK_CONTROL) {
                Client.controlIsDown = false;
            }

            if (keyCode >= 0 && keyCode < keyCodes.length) {
                int keyHandlerKeyCode = keyCodes[keyCode];
                keyCode = keyHandlerKeyCode & -129;
            } else {
                keyCode = -1;
            }

            if (keyCode >= 0) {
                this.pressedKeys[keyCode] = false;
                this.keyEventQueue.add(new KeyEventWrapper(2, keyCode));
            }

            keyEvent.consume();
        }

    }

    public void assignProcessor(KeyEventProcessor processor, int index) {
        this.keyEventProcessors[index] = processor;
    }

    public final synchronized void focusGained(FocusEvent var1) {
        this.keyEventQueue.add(new KeyEventWrapper(4, 1));
    }

    public final synchronized void focusLost(FocusEvent var1) {
        Client.shiftDown = false;
        Client.isCtrlPressed = false;

        for (int var2 = 0; var2 < 112; ++var2) {
            if (this.pressedKeys[var2]) {
                this.pressedKeys[var2] = false;
                this.keyEventQueue.add(new KeyEventWrapper(2, var2));
            }
        }

        this.keyEventQueue.add(new KeyEventWrapper(4, 0));
    }

    public final synchronized void keyPressed(KeyEvent keyEvent) {
        Client.instance.getCallbacks().keyPressed(keyEvent);
        if (!keyEvent.isConsumed())
        {

            int keyCode = keyEvent.getKeyCode();

            if (keyEvent.isShiftDown()) {
                Client.shiftDown = true;
            }

            if (keyCode == KeyEvent.VK_SPACE) {
                if (Client.backDialogID == 979 || Client.backDialogID == 968 || Client.backDialogID == 973
                        || Client.backDialogID == 986 || Client.backDialogID == 306 || Client.backDialogID == 4887
                        || Client.backDialogID == 4893 || Client.backDialogID == 356 || Client.backDialogID == 359
                        || Client.backDialogID == 310 || Client.backDialogID == 4882 || Client.backDialogID == 4900) {
                    Client.stream.createFrame(40);
                    Client.stream.writeWord(4892);
                }
            }


            if (keyCode == 192 && Client.localPlayer != null && Client.localPlayer.isAdminRights()) {
                Client.instance.devConsole.console_open = !Client.instance.devConsole.console_open;
            }

            if (keyEvent.isControlDown()) {
                if (keyCode == KeyEvent.VK_SPACE) {
                    Client.continueDialogue();
                }
                if (keyCode == KeyEvent.VK_1 || keyCode == KeyEvent.VK_NUMPAD1) {
                    Client.dialogueOptions("one");
                }
                if (keyCode == KeyEvent.VK_2 || keyCode == KeyEvent.VK_NUMPAD2) {
                    Client.dialogueOptions("two");
                }
                if (keyCode == KeyEvent.VK_3 || keyCode == KeyEvent.VK_NUMPAD3) {
                    Client.dialogueOptions("three");
                }
                if (keyCode == KeyEvent.VK_4 || keyCode == KeyEvent.VK_NUMPAD4) {
                    Client.dialogueOptions("four");
                }
                if (keyCode == KeyEvent.VK_5 || keyCode == KeyEvent.VK_NUMPAD5) {
                    Client.dialogueOptions("five");
                }
                switch (keyCode) {
                    case KeyEvent.VK_V:
                        Client.inputString += Client.getClipboardContents();
                        Client.inputTaken = true;
                        break;

                    case KeyEvent.VK_T:
                        Client.teleportInterface();
                        break;

                }
            }

            if (keyCode == KeyEvent.VK_F1) {
                Keybinding.isBound(KeyEvent.VK_F1);
            } else if (keyCode == KeyEvent.VK_F2) {
                Keybinding.isBound(KeyEvent.VK_F2);
            } else if (keyCode == KeyEvent.VK_F3) {
                Keybinding.isBound(KeyEvent.VK_F3);
            } else if (keyCode == KeyEvent.VK_F4) {
                Keybinding.isBound(KeyEvent.VK_F4);
            } else if (keyCode == KeyEvent.VK_F5) {
                Keybinding.isBound(KeyEvent.VK_F5);
            } else if (keyCode == KeyEvent.VK_F6) {
                Keybinding.isBound(KeyEvent.VK_F6);
            } else if (keyCode == KeyEvent.VK_F7) {
                Keybinding.isBound(KeyEvent.VK_F7);
            } else if (keyCode == KeyEvent.VK_F8) {
                Keybinding.isBound(KeyEvent.VK_F8);
            } else if (keyCode == KeyEvent.VK_F9) {
                Keybinding.isBound(KeyEvent.VK_F9);
            } else if (keyCode == KeyEvent.VK_F10) {
                Keybinding.isBound(KeyEvent.VK_F10);
            } else if (keyCode == KeyEvent.VK_F11) {
                Keybinding.isBound(KeyEvent.VK_F11);
            } else if (keyCode == KeyEvent.VK_F12) {
                Keybinding.isBound(KeyEvent.VK_F12);
            }

            if (keyCode == KeyEvent.VK_ESCAPE) {
                Keybinding.isBound(KeyEvent.VK_ESCAPE);
                Client.closeInterfacee();
            }

            if (keyEvent.isControlDown()) {
                Client.controlIsDown = true;
            }

            for(KeyboardAction action : KeyboardAction.values()) {
                if (Client.loggedIn) {
                    if (action.canActivate(keyEvent.getKeyCode(), keyEvent.isControlDown(), keyEvent.isShiftDown(), keyEvent.isAltDown())) {
                        Client.instance.sendKeyboardAction(action.getAction());
                        break;
                    }
                }
            }

            if (keyCode >= 0 && keyCode < keyCodes.length) {
                int keyHandlerKeyCode = keyCodes[keyCode];
                keyCode = keyHandlerKeyCode;
                boolean var4 = (keyHandlerKeyCode & 128) != 0;
                if (var4) {
                    keyCode = -1;
                }
            } else {
                keyCode = -1;
            }

            if (keyCode >= 0) {
                if (!this.pressedKeys[keyCode]) {
                    idleCycles = 0;
                }

                this.pressedKeys[keyCode] = true;
                this.keyEventQueue.add(new KeyEventWrapper(1, keyCode));
            }


            keyEvent.consume();
        }


    }

    public static final char[] cp1252AsciiExtension = new char[]{'€', '\u0000', '‚', 'ƒ', '„', '…', '†', '‡', 'ˆ', '‰', 'Š', '‹', 'Œ', '\u0000', 'Ž', '\u0000', '\u0000', '‘', '’', '“', '”', '•', '–', '—', '˜', '™', 'š', '›', 'œ', '\u0000', 'ž', 'Ÿ'};


    public static boolean method4577(char var0) {
        if ((var0 <= 0 || var0 >= 128) && (var0 < 160 || var0 > 255)) {
            if (var0 != 0) {
                char[] var1 = cp1252AsciiExtension;

                for (int var2 = 0; var2 < var1.length; ++var2) {
                    char var3 = var1[var2];
                    if (var0 == var3) {
                        return true;
                    }
                }
            }

            return false;
        } else {
            return true;
        }
    }


}
