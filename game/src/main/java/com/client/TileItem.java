package com.client;

import com.client.definitions.ItemDefinition;
import com.client.entity.model.Model;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Tile;
import net.runelite.api.events.ItemQuantityChanged;
import net.runelite.rs.api.RSTileItem;

@Slf4j
final class TileItem extends Renderable implements RSTileItem {

	private int rl$sceneX = -1;

	private int rl$sceneY = -1;

	@Override
	public Model getRotatedModel() {
		ItemDefinition itemDef = ItemDefinition.lookup(id);
		return itemDef.getModel(quantity);
	}

	TileItem() {
		this.field1136 = 31;
	}

	void method621(int var1) {
		this.field1136 = var1;
	}

	public int id;
	public int x;
	public int y;
	public int quantity;
	int field1136;

	@Override
	public int getId() {
		return id;
	}

	@Override
	public void setId(int id) {
		this.id = id;
	}

	@Override
	public int getQuantity() {
		return quantity;
	}

	@Override
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	@Override
	public int getX()
	{
		return rl$sceneX;
	}


	@Override
	public void setX(int x)
	{
		rl$sceneX = x;
	}

	@Override
	public int getY()
	{
		return rl$sceneY;
	}


	@Override
	public void setY(int y)
	{
		rl$sceneY = y;
	}
	@Override
	public Tile getTile()
	{
		int x = rl$sceneX;
		int y = rl$sceneY;

		if (x == -1 || y == -1)
		{
			return null;
		}

		Tile[][][] tiles = Client.instance.getScene().getTiles();
		return tiles[Client.instance.getPlane()][x][y];
	}

	@Override
	public void pickup() {

	}

	@Override
	public String getName() {
		return ItemDefinition.lookup(id).name;
	}

	@Override
	public boolean isTradable() {
		return ItemDefinition.lookup(id).isTradable;
	}

	@Override
	public boolean isStackable() {
		return ItemDefinition.lookup(id).stackable;
	}

	@Override
	public boolean isMembers() {
		return ItemDefinition.lookup(id).members;
	}

	@Override
	public int getNotedId() {
		return 0;
	}

	@Override
	public boolean isNoted() {
		return false;
	}

	@Override
	public int getStorePrice() {
		return ItemDefinition.lookup(id).price;
	}

	@Override
	public String[] getInventoryActions() {
		return ItemDefinition.lookup(id).interfaceOptions;
	}

	@Override
	public long getTag() {
		return 0;
	}


	public void onUnlink()
	{

		if (rl$sceneX != -1)
		{
			// on despawn, the first item unlinked is the item despawning. However on spawn
			// items can be delinked in order to sort them, so we can't assume the item here is despawning
			if (Client.instance.getLastItemDespawn() == null)
			{
				Client.instance.setLastItemDespawn(this);
			}
		}
	}


	public void quantityChanged(int quantity)
	{
		if (rl$sceneX != -1)
		{
			log.debug("Item quantity changed: {} ({} -> {})", getId(), getQuantity(), quantity);

			ItemQuantityChanged itemQuantityChanged = new ItemQuantityChanged(this, getTile(), getQuantity(), quantity);
			Client.instance.getCallbacks().post(itemQuantityChanged);
		}
	}

	boolean method622(int var1) {
		if (var1 >= 0 && var1 <= 4) {
			return (this.field1136 & 1 << var1) != 0;
		} else {
			return true;
		}
	}


}
