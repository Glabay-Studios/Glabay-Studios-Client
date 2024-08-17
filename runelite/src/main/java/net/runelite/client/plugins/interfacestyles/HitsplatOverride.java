/*
 * Copyright (c) 2019 Hydrox6 <ikada@protonmail.ch>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.runelite.client.plugins.interfacestyles.hitsplat;

import lombok.Getter;
import net.runelite.client.game.SpriteOverride;
import net.runelite.client.plugins.interfacestyles.HitsplatSkin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public enum HitsplatOverride implements SpriteOverride {
	// Hitsplat skins for 2002
	BLOCK_NORMAL_2002(1358, "2002/block_normal.png", HitsplatSkin.AROUND_2002),
	BLOCK_TINTED_2002(1630, "2002/block_tinted.png",HitsplatSkin.AROUND_2002),
	DAMAGE_NORMAL_2002(1359, "2002/damage_normal.png",HitsplatSkin.AROUND_2002),
	DAMAGE_TINTED_2002(1631, "2002/damage_tinted.png",HitsplatSkin.AROUND_2002),
	DAMAGE_MAX_2002(3571, "2002/damage_max.png",HitsplatSkin.AROUND_2002),
	POISON_NORMAL_2002(1360, "2002/poison_normal.png",HitsplatSkin.AROUND_2002),
	DISEASE_NORMAL_2002(1633, "2002/disease_normal.png",HitsplatSkin.AROUND_2002),
	VENOM_NORMAL_2002(1632, "2002/venom_normal.png",HitsplatSkin.AROUND_2002),

	// Hitsplat skins for 2010
	// Following the same pattern as above, just an example
	BLOCK_NORMAL_2010(1358, "2010/block_normal.png",HitsplatSkin.AROUND_2010),
	BLOCK_TINTED_2010(1630, "2010/block_tinted.png",HitsplatSkin.AROUND_2010),

	DAMAGE_NORMAL_2010(1359, "2010/damage_normal.png",HitsplatSkin.AROUND_2010),
	DAMAGE_TINTED_2010(1631, "2010/damage_tinted.png",HitsplatSkin.AROUND_2010),
	DAMAGE_MAX_2010(3571, "2010/damage_max.png",HitsplatSkin.AROUND_2010),

	POISON_NORMAL_2010(1360, "2010/poison_normal.png",HitsplatSkin.AROUND_2010),

	DISEASE_NORMAL_2010(1633, "2010/disease_normal.png",HitsplatSkin.AROUND_2010),

	VENOM_NORMAL_2010(1632, "2010/venom_normal.png",HitsplatSkin.AROUND_2010),

	BLOCK_NORMAL_2011(1358, "2011/block_normal.png",HitsplatSkin.AROUND_2011),
	BLOCK_TINTED_2011(1630, "2011/block_tinted.png",HitsplatSkin.AROUND_2011),

	DAMAGE_NORMAL_2011(1359, "2011/damage_normal.png",HitsplatSkin.AROUND_2011),
	DAMAGE_TINTED_2011(1631, "2011/damage_tinted.png",HitsplatSkin.AROUND_2011),
	DAMAGE_MAX_2011(3571, "2011/damage_max.png",HitsplatSkin.AROUND_2011),

	POISON_NORMAL_2011(1360, "2011/poison_normal.png",HitsplatSkin.AROUND_2011),

	DISEASE_NORMAL_2011(1633, "2011/disease_normal.png",HitsplatSkin.AROUND_2011),

	VENOM_NORMAL_2011(1632, "2011/venom_normal.png",HitsplatSkin.AROUND_2011);

	@Getter
	private final int spriteId;
	private final String fileName;

	private final HitsplatSkin hitsplatSkin;

	// Additional attributes if needed, like padding
	@Getter
	private int padding = 1;

	HitsplatOverride(int spriteId, String fileName, HitsplatSkin skin) {
		this.spriteId = spriteId;
		this.fileName = fileName;
		this.hitsplatSkin = skin;
	}

	@Override
	public String getFileName() {
		return "hitsplats/" + this.fileName;
	}

	public static HitsplatOverride[] get(HitsplatSkin skin) {
		List<HitsplatOverride> matchingOverrides = new ArrayList<>();
		for (HitsplatOverride hitsplat : values()) {
			if (hitsplat.hitsplatSkin.equals(skin)) {
				matchingOverrides.add(hitsplat);
			}
		}
		return matchingOverrides.toArray(new HitsplatOverride[0]);
	}

}