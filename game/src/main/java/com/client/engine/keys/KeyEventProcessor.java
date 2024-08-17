package com.client.engine.keys;

public interface KeyEventProcessor {

    boolean processKeyPressed(int var1);

    boolean processKeyReleased(int var1);

    boolean processKeyTyped(char var1);

    boolean processFocusChange(boolean var1);
}