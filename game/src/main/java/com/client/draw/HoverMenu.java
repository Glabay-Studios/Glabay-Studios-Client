package com.client.draw;

import java.util.List;

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
