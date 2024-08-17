package com.client.graphics.interfaces.impl;

import com.client.graphics.interfaces.MenuItem;

public class DropdownMenu {

	private final int height;
	private final int width;
	private final String[] options;
	private final MenuItem menuIt;
	private boolean open;
	private final boolean split;
	private String optionSelected;

	public DropdownMenu(int width, boolean split, int defaultOption, String[] options, MenuItem menuIt) {
		this.width = width;
		this.height = split ? ((14 * options.length) / 2) + 3 : (14 * options.length) + 3;
		this.options = options;
		this.optionSelected = defaultOption == -1 ? "Select an option" : options[defaultOption];
		this.open = false;
		this.menuIt = menuIt;
		this.split = split;
	}

	public int getHeight() {
		return this.height;
	}

	public int getWidth() {
		return this.width;
	}

	public String[] getOptions() {
		return this.options;
	}

	public boolean isOpen() {
		return this.open;
	}

	public void setOpen(boolean b) {
		this.open = b;
	}

	public String getSelected() {
		return this.optionSelected;
	}

	public void setSelected(String s) {
		this.optionSelected = s;
	}

	public MenuItem getMenuItem() {
		return this.menuIt;
	}

	public boolean doesSplit() {
		return this.split;
	}
}