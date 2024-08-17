package com.client.itembonus;

import com.client.sign.Signlink;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.reflection.Sun14ReflectionProvider;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemBonusDefinitionLoader {
	private static final Map<Integer, ItemBonusDefinition> itemBonusDefinitions =
			new HashMap<>();


	private static final XStream xStream = new XStream(new Sun14ReflectionProvider());

	public static ItemBonusDefinition getItemBonusDefinition(int i) {
		return itemBonusDefinitions.get(i);
	}

	public static short[] getItemBonuses(int id) {
		ItemBonusDefinition def = getItemBonusDefinition(id);
		if (def != null) {
			return def.getBonuses();
		}
		return null;
	}

	/*public static int getTotal(int id) {
		int total = 0;
		for(short s : getItemBonuses(id)) {
			total += s;
		}
		return total;

	}*/

	@SuppressWarnings("unchecked")
	public static void loadItemBonusDefinitions() throws IOException {
		xStream.alias("ItemBonusDefinition", ItemBonusDefinition.class);
		// file added?
		List<ItemBonusDefinition> list = (List<ItemBonusDefinition>) xStream.fromXML(
				new FileInputStream(Signlink.getCacheDirectory()+ "ItemBonusDefinitions.xml"));
		for (ItemBonusDefinition definition : list) {
			itemBonusDefinitions.put(definition.getId(), definition);
		}
		System.out.println("Loaded " + list.size() + " item bonus definitions.");
	}
}

