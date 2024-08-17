package com.client;

import java.util.Arrays;

import com.client.definitions.ItemDefinition;
import com.client.js5.Js5List;
import com.client.js5.util.Js5ConfigType;

public class ItemSearch {
	
	private String itemSearchName;
	
	private int[] itemSearchResults;
	
	private int itemRange;
	
	private int itemSearchResultAmount;
	
	private boolean hideUntradables;
	
	public ItemSearch(String itemSearchName, int itemRange, boolean hideUntradables) {
		this.itemSearchName = itemSearchName;
		this.itemRange = itemRange;
		this.hideUntradables = hideUntradables;
		itemSearchResults = searchForName();
	}
	
	public int[] searchForName() {
		int[] items = new int[itemRange];
		int position = 0;
		for (int itemId = 0; itemId < Js5List.getConfigSize(Js5ConfigType.ITEM); itemId++) {
			ItemDefinition itemDefinitions = ItemDefinition.lookup(itemId);
			if(position >= items.length)
				break;
			
			if(itemDefinitions == null)
				continue;
			
			if(hideUntradables) {
				if(!itemDefinitions.isTradable)
					continue;
			}
				
    		String itemName = itemDefinitions.name;
    		
    		if(itemName == null)
    			continue;
    		
    		if(itemName.toLowerCase().contains(itemSearchName.toLowerCase())) {
    			if(Arrays.binarySearch(items, itemId) >= 0)
    				continue;
    			items[position] = itemId;
    			position++;
    			itemSearchResultAmount++;
    		}
		}
		return items;
	}
	
	public int[] getItemSearchResults() {
		return itemSearchResults;
	}
	
	public int getItemSearchResultAmount() {
		return itemSearchResultAmount;
	}

}