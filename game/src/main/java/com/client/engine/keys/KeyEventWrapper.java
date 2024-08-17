package com.client.engine.keys;

public class KeyEventWrapper {

    private int eventType;
    private int eventKey;

    /**
     * Constructs a KeyEventWrapper instance.
     *
     * @param eventType The type of key event (e.g., key pressed, key released).
     * @param eventKey The key code or character associated with the event.
     */
    public KeyEventWrapper(int eventType, int eventKey) {
        this.eventType = eventType;
        this.eventKey = eventKey;
    }

    /**
     * Processes the key event with the provided KeyEventProcessor.
     *
     * @param processor The KeyEventProcessor to process the event.
     * @return true if the event was processed, false otherwise.
     */
    public boolean processWith(KeyEventProcessor processor) {
        if (processor == null) {
            return false;
        } else {
            switch (this.eventType) {
                case 1:
                    return processor.processKeyPressed(this.eventKey);
                case 2:
                    return processor.processKeyReleased(this.eventKey);
                case 3:
                    return processor.processKeyTyped((char) this.eventKey);
                case 4:
                    return processor.processFocusChange(this.eventKey == 1);
                default:
                    return false;
            }
        }
    }
}