package com.client.features;

import com.client.Client;
import com.client.Configuration;
import com.client.Rasterizer2D;
import com.client.Entity;
import com.client.Npc;
import com.client.Player;
import com.client.RSFont;

public class EntityTarget {

	private byte state;

	private final Client client = Client.instance;

	private Entity target;

	public EntityTarget(byte state, short entityIndex, short currentHealth, short maximumHealth, RSFont rsFont) {
		this.state = state;
		if (state > 0 && state < 3) {
			try {
				if (state == 1) {
					target = client.npcs[entityIndex];
				} else if (state == 2) {
					target = client.players[entityIndex];
				}
			} catch (Exception e) {
				System.err.println("Error when setting target index: " + entityIndex);
				e.printStackTrace();
				return;
			}

			if (target != null) {
				target.currentHealth = currentHealth;
				target.maxHealth = maximumHealth;
			}
		}
	}

	public void draw() {
		if (state <= 0 || state > 2 || target == null) {
			return;
		}

		int x = Client.instance.getLocalPlayerX();
		int y = Client.instance.getLocalPlayerY();

		// Nightmare
		if (x >= 3862 && y >= 9940 && x <= 3883 && y <= 9961) {
			return;
		}


		String name = "Unknown";

		if (state == 1) {
			Npc npc = (Npc) target;
			if (npc.desc != null) {
				name = npc.desc.name;
			}
		} else if (state == 2) {
			name = ((Player) target).displayName;
		} else {
			state = 0;
			return;
		}
		int offset = 4;
		if (Client.counterOn && Configuration.xpPosition == 2) {
			offset = 31;
		}
		int width = 134;
		int xPos = 6;
		int yPos = 22 + offset;

		Rasterizer2D.drawBoxOutline(xPos, yPos, width - 3, 34, 0x393022);
		Rasterizer2D.drawAlphaBox(xPos, yPos, width - 3, 33, 0x60574E, 110);
		Client.latoBold.drawCenteredString(name, xPos + (width / 2) - 2, yPos + 18, 16777215, 0x000000);
		int barWidth = 124;
		int fillPercentage = target.currentHealth * barWidth / target.maxHealth;
		Rasterizer2D.drawAlphaBox(xPos + 3, yPos + 18, width - 9, 13, 11740160, 160);
		Rasterizer2D.drawAlphaBox(xPos + 3, yPos + 18, fillPercentage, 13, 0x00A900, 160);
		Client.latoBold.drawCenteredString(target.currentHealth + " / " + target.maxHealth, xPos + (width / 2) - 2, yPos + 33,
				16777215, 0x000000);
	}

	public void stop() {
		state = 0;
	}

}