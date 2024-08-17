package com.client.engine.keys;

import java.util.Arrays;

public class KeyEventProcessorImpl implements KeyEventProcessor {
    private char[] typedCharacters;
    private int[] typedKeyCodes;
    public int[] pressedKeyCodes;
    public int pressedKeyCount;
    private int[] releasedKeyCodes;
    private int releasedKeyCount;
    private int writeIndex;
    private int readIndex;
    private int processingIndex;
    private boolean[] field2357;
    private boolean[] field2361;
    private boolean[] field2362;
    public char lastTypedCharacter;
    public int lastProcessedKeyCode;

    public KeyEventProcessorImpl() {
        this.typedCharacters = new char[128];
        this.typedKeyCodes = new int[128];
        this.pressedKeyCodes = new int[128];
        this.pressedKeyCount = 0;
        this.releasedKeyCodes = new int[128];
        this.releasedKeyCount = 0;
        this.writeIndex = 0;
        this.readIndex = 0;
        this.processingIndex = 0;
        this.field2357 = new boolean[112];
        this.field2361 = new boolean[112];
        this.field2362 = new boolean[112];
    }

    public boolean processKeyPressed(int keyCode) {
        recordKeyPress(keyCode);
        this.field2357[keyCode] = true;
        this.field2361[keyCode] = true;
        this.field2362[keyCode] = false;
        this.pressedKeyCodes[this.pressedKeyCount++] = keyCode;
        return true;
    }

    public boolean processKeyReleased(int keyCode) {
        this.field2357[keyCode] = false;
        this.field2361[keyCode] = false;
        this.field2362[keyCode] = true;
        this.releasedKeyCodes[this.releasedKeyCount++] = keyCode;
        return true;
    }

    public boolean processKeyTyped(char character) {
        int nextWriteIndex = (this.writeIndex + 1) & 127;
        if (nextWriteIndex != this.processingIndex) {
            this.typedKeyCodes[this.writeIndex] = -1;
            this.typedCharacters[this.writeIndex] = character;
            this.writeIndex = nextWriteIndex;
        }

        return false;
    }

    public boolean processFocusChange(boolean hasFocus) {
        return false;
    }

    private void recordKeyPress(int keyCode) {
        int nextWriteIndex = (this.writeIndex + 1) & 127;
        if (nextWriteIndex != this.processingIndex) {
            this.typedKeyCodes[this.writeIndex] = keyCode;
            this.typedCharacters[this.writeIndex] = 0;
            this.writeIndex = nextWriteIndex;
        }
    }

    public void prepareForNextCycle() {
        this.processingIndex = this.readIndex;
        this.readIndex = this.writeIndex;
        this.pressedKeyCount = 0;
        this.releasedKeyCount = 0;
        Arrays.fill(this.field2361, false);
        Arrays.fill(this.field2362, false);
    }

    public boolean hasNextKey() {
        if (this.processingIndex == this.readIndex) {
            return false;
        } else {
            this.lastProcessedKeyCode = this.typedKeyCodes[this.processingIndex];
            this.lastTypedCharacter = this.typedCharacters[this.processingIndex];
            this.processingIndex = (this.processingIndex + 1) & 127;
            return true;
        }
    }

    public boolean wasKeyJustPressed(int keyCode) {
        return keyCode >= 0 && keyCode < 112 && this.field2361[keyCode];
    }

    public boolean isKeyPressed(int keyCode) {
        return keyCode >= 0 && keyCode < 112 && this.field2357[keyCode];
    }


    public boolean method4315(int var1) {
        return var1 >= 0 && var1 < 112 ? this.field2361[var1] : false;
    }


    public boolean method4316(int var1) {
        return var1 >= 0 && var1 < 112 ? this.field2357[var1] : false;
    }


    public boolean method4317(int var1) {
        return var1 >= 0 && var1 < 112 ? this.field2362[var1] : false;
    }

    public boolean wasKeyJustReleased(int keyCode) {
        return keyCode >= 0 && keyCode < 112 && this.field2362[keyCode];
    }

    public int[] getPressedKeyCodes() {
        return Arrays.copyOf(this.pressedKeyCodes, this.pressedKeyCount);
    }

    public int[] getReleasedKeyCodes() {
        return Arrays.copyOf(this.releasedKeyCodes, this.releasedKeyCount);
    }

}