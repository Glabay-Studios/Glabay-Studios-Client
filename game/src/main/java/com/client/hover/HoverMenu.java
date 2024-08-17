package com.client.hover;

import java.util.List;

/**
 *
 * @author C.T for koranes
 *
 */

public class HoverMenu {

    public String text;

    public List<Integer> items;

    public HoverMenu(String text, List<Integer> items) {
        this.text = text;
        this.items = items;
    }

    public HoverMenu(String text) {
        this(text, null);
    }
}
