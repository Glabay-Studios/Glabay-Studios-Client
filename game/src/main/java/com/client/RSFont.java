package com.client;

import com.util.AssetUtils;
import org.apache.commons.io.IOUtils;

import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class RSFont extends Rasterizer2D {

	public int baseCharacterHeight = 0;
	public int anInt4142;
	public int anInt4144;
	public int[] characterDrawYOffsets;
	public int[] characterHeights;
	public int[] characterDrawXOffsets;
	public int[] characterWidths;
	public int[] iconWidths;
	public byte[] aByteArray4151;
	public byte[][] fontPixels;
	public int[] characterScreenWidths;
	public static Sprite[] chatImages;
	public static Sprite[] clanImages;
	public static Sprite[] iconPack;
	public static String aRSString_4135;
	public static String startTransparency;
	public static String startDefaultShadow;
	public static String endShadow = "/shad";
	public static String endEffect;
	public static String aRSString_4143;
	public static String endStrikethrough = "/str";
	public static String aRSString_4147;
	public static String startColor;
	public static String lineBreak;
	public static String startStrikethrough;
	public static String endColor;
	public static String startImage;
	public static String startIcon;
	public static String startClanImage;
	public static String endUnderline;
	public static String defaultStrikethrough;
	public static String startShadow;
	public static String startEffect;
	public static String aRSString_4162;
	public static String aRSString_4163;
	public static String endTransparency;
	public static String aRSString_4165;
	public static String startUnderline;
	public static String startDefaultUnderline;
	public static String aRSString_4169;
	public static String[] splitTextStrings;
	public static int defaultColor;
	public static int textShadowColor;
	public static int strikethroughColor;
	public static int defaultTransparency;
	public static int anInt4175;
	public static int underlineColor;
	public static int defaultShadow;
	public static int anInt4178;
	public static int transparency;
	public static int textColor;

	public RSFont(boolean TypeFont, String s, FileArchive archive) {
		fontPixels = new byte[256][];
		characterWidths = new int[256];
		characterHeights = new int[256];
		characterDrawXOffsets = new int[256];
		characterDrawYOffsets = new int[256];
		characterScreenWidths = new int[256];
		Buffer stream = new Buffer(archive.readFile(s + ".dat"));
		Buffer stream_1 = new Buffer(archive.readFile("index.dat"));
		stream_1.currentPosition = stream.readUShort() + 4;
		int k = stream_1.readUnsignedByte();
		if (k > 0) {
			stream_1.currentPosition += 3 * (k - 1);
		}
		for (int l = 0; l < 256; l++) {
			characterDrawXOffsets[l] = stream_1.readUnsignedByte();
			characterDrawYOffsets[l] = stream_1.readUnsignedByte();
			int i1 = characterWidths[l] = stream_1.readUShort();
			int j1 = characterHeights[l] = stream_1.readUShort();
			int k1 = stream_1.readUnsignedByte();
			int l1 = i1 * j1;
			fontPixels[l] = new byte[l1];
			if (k1 == 0) {
				for (int i2 = 0; i2 < l1; i2++) {
					fontPixels[l][i2] = stream.readSignedByte();
				}

			} else if (k1 == 1) {
				for (int j2 = 0; j2 < i1; j2++) {
					for (int l2 = 0; l2 < j1; l2++) {
						fontPixels[l][j2 + l2 * i1] = stream.readSignedByte();
					}

				}

			}
			if (j1 > baseCharacterHeight && l < 128) {
				baseCharacterHeight = j1;
			}
			characterDrawXOffsets[l] = 1;
			characterScreenWidths[l] = i1 + 2;
			int k2 = 0;
			for (int i3 = j1 / 7; i3 < j1; i3++) {
				k2 += fontPixels[l][i3 * i1];
			}

			if (k2 <= j1 / 7) {
				characterScreenWidths[l]--;
				characterDrawXOffsets[l] = 0;
			}
			k2 = 0;
			for (int j3 = j1 / 7; j3 < j1; j3++) {
				k2 += fontPixels[l][(i1 - 1) + j3 * i1];
			}

			if (k2 <= j1 / 7) {
				characterScreenWidths[l]--;
			}
		}

		if (TypeFont) {
			characterScreenWidths[32] = characterScreenWidths[73];
		} else {
			characterScreenWidths[32] = characterScreenWidths[105];
		}
	}


	public RSFont(boolean TypeFont, String s) {
		fontPixels = new byte[256][];
		characterWidths = new int[256];
		characterHeights = new int[256];
		characterDrawXOffsets = new int[256];
		characterDrawYOffsets = new int[256];
		characterScreenWidths = new int[256];
		Buffer datBuf = null;
		Buffer idxBuf = null;
		try {
			InputStream in = AssetUtils.INSTANCE.getResource(s + ".dat").openStream();
			InputStream in1 = AssetUtils.INSTANCE.getResource("index.dat").openStream();
			datBuf = new Buffer(IOUtils.toByteArray(in));
			idxBuf = new Buffer(IOUtils.toByteArray(in1));
		} catch (IOException e) {
			e.printStackTrace();
		}
		idxBuf.currentPosition = datBuf.readUShort() + 4;
		int k = idxBuf.readUnsignedByte();

		if (k > 0) {
			idxBuf.currentPosition += 3 * (k - 1);
		}

		for (int l = 0; l < 256; l++) {
			characterDrawXOffsets[l] = idxBuf.readUnsignedByte();
			characterDrawYOffsets[l] = idxBuf.readUnsignedByte();
			int i1 = characterWidths[l] = idxBuf.readUShort();
			int j1 = characterHeights[l] = idxBuf.readUShort();
			int k1 = idxBuf.readUnsignedByte();
			int l1 = i1 * j1;
			fontPixels[l] = new byte[l1];

			if (k1 == 0) {
				for (int i2 = 0; i2 < l1; i2++) {
					fontPixels[l][i2] = datBuf.readSignedByte();
				}
			} else if (k1 == 1) {
				for (int j2 = 0; j2 < i1; j2++) {
					for (int l2 = 0; l2 < j1; l2++) {
						fontPixels[l][j2 + l2 * i1] = datBuf.readSignedByte();
					}
				}
			}

			if (j1 > baseCharacterHeight && l < 128) {
				baseCharacterHeight = j1;
			}

			characterDrawXOffsets[l] = 1;
			characterScreenWidths[l] = i1 + 2;
			int k2 = 0;

			for (int i3 = j1 / 7; i3 < j1; i3++) {
				k2 += fontPixels[l][i3 * i1];
			}

			if (k2 <= j1 / 7) {
				characterScreenWidths[l]--;
				characterDrawXOffsets[l] = 0;
			}

			k2 = 0;

			for (int j3 = j1 / 7; j3 < j1; j3++) {
				k2 += fontPixels[l][(i1 - 1) + j3 * i1];
			}

			if (k2 <= j1 / 7) {
				characterScreenWidths[l]--;
			}
		}

		if (TypeFont) {
			characterScreenWidths[32] = characterScreenWidths[73];
		} else {
			characterScreenWidths[32] = characterScreenWidths[105];
		}
	}

	public String[] wrap(String text, int maximumWidth) {
		String[] words = text.split(" ");

		if (words.length == 0) {
			return new String[] { text };
		}

		List<String> lines = new ArrayList<>();

		String line = new String();

		int lineWidth = 0;

		int spaceWidth = getTextWidth(" ");

		for (String word : words) {
			if (word.isEmpty()) {
				continue;
			}
			int wordWidth = getTextWidth(word);
			boolean isLastWord = word.equals(words[words.length - 1]);

			if (wordWidth + lineWidth >= maximumWidth && !isLastWord) {
				lines.add(line.trim());
				line = new String(word.concat(" "));
				lineWidth = wordWidth + spaceWidth;
			} else if (isLastWord) {
				if (wordWidth + lineWidth > maximumWidth) {
					lines.add(line.trim());
					lines.add(word);
				} else {
					lines.add(line.concat(word));
				}
			} else {
				line = line.concat(word).concat(" ");
				lineWidth += wordWidth + spaceWidth;
			}
		}

		return lines.toArray(new String[lines.size()]);
	}

	public void drawStringMoveY(String string, int drawX, int drawY, int color, int shadow, int randomMod,
			int randomMod2) {
		if (string != null) {
			setColorAndShadow(color, shadow);
			double d = 7.0 - randomMod2 / 8.0;
			if (d < 0.0) {
				d = 0.0;
			}
			int[] yOffset = new int[string.length()];
			for (int index = 0; index < string.length(); index++) {
				yOffset[index] = (int) (Math.sin(index / 1.5 + randomMod) * d);
			}
			drawBaseStringMoveXY(string, drawX - getTextWidth(string) / 2, drawY, null, yOffset);
		}
	}

	public void setDefaultTextEffectValues(int color, int shadow, int trans) {
		strikethroughColor = -1;
		underlineColor = -1;
		textShadowColor = defaultShadow = shadow;
		textColor = defaultColor = color;
		transparency = defaultTransparency = trans;
		anInt4178 = 0;
		anInt4175 = 0;
	}

	public static int method1014(byte[][] is, byte[][] is_27_, int[] is_28_, int[] is_29_, int[] is_30_, int i,
			int i_31_) {
		int i_32_ = is_28_[i];
		int i_33_ = i_32_ + is_30_[i];
		int i_34_ = is_28_[i_31_];
		int i_35_ = i_34_ + is_30_[i_31_];
		int i_36_ = i_32_;
		if (i_34_ > i_32_) {
			i_36_ = i_34_;
		}
		int i_37_ = i_33_;
		if (i_35_ < i_33_) {
			i_37_ = i_35_;
		}
		int i_38_ = is_29_[i];
		if (is_29_[i_31_] < i_38_) {
			i_38_ = is_29_[i_31_];
		}
		byte[] is_39_ = is_27_[i];
		byte[] is_40_ = is[i_31_];
		int i_41_ = i_36_ - i_32_;
		int i_42_ = i_36_ - i_34_;
		for (int i_43_ = i_36_; i_43_ < i_37_; i_43_++) {
			int i_44_ = is_39_[i_41_++] + is_40_[i_42_++];
			if (i_44_ < i_38_) {
				i_38_ = i_44_;
			}
		}
		return -i_38_;
	}

	public void drawCenteredStringMoveXY(String string, int drawX, int drawY, int color, int shadow, int randomMod) {
		if (string != null) {
			setColorAndShadow(color, shadow);
			int[] xMods = new int[string.length()];
			int[] yMods = new int[string.length()];
			for (int index = 0; index < string.length(); index++) {
				xMods[index] = (int) (Math.sin(index / 5.0 + randomMod / 5.0) * 5.0);
				yMods[index] = (int) (Math.sin(index / 3.0 + randomMod / 5.0) * 5.0);
			}
			drawBaseStringMoveXY(string, drawX - getTextWidth(string) / 2, drawY, xMods, yMods);
		}
	}

	public void drawCenteredStringMoveY(String class100, int drawX, int drawY, int color, int shadow, int i_54_) {
		if (class100 != null) {
			setColorAndShadow(color, shadow);
			int[] yOffset = new int[class100.length()];
			for (int index = 0; index < class100.length(); index++) {
				yOffset[index] = (int) (Math.sin(index / 2.0 + i_54_ / 5.0) * 5.0);
			}
			drawBaseStringMoveXY(class100, drawX - getTextWidth(class100) / 2, drawY, null, yOffset);
		}
	}

	public static void unpackImages(Sprite[] icons, Sprite[] clan, Sprite[] iconPack) {
		chatImages = icons;
		clanImages = clan;
		RSFont.iconPack = iconPack;
	}

	public void drawString(String string, int x, int y, int color, int shadow, int trans) {
		if (transparency < 0 || transparency > 256) {
			transparency = defaultTransparency;
		}
		setColorAndShadow(color, shadow);
		transparency = trans;
		drawBasicString(string, x, y);
	}

	public void drawCenteredString(String string, int drawX, int drawY, int color, int shadow, int trans) {
		if (transparency < 0 || transparency > 256) {
			transparency = defaultTransparency;
		}
		if (string != null) {
			setColorAndShadow(color, shadow);
			string = handleOldSyntax(string);
			transparency = trans;
			drawBasicString(string, drawX - getTextWidth(string) / 2, drawY);
		}
	}

	public void drawBasicString(String string, int drawX, int drawY) {
		drawBasicString(string, drawX, drawY, true);
	}

	public void drawBasicString(String string, int drawX, int drawY, boolean textEffects) {
		drawY -= baseCharacterHeight;
		int startIndex = -1;
		string = handleOldSyntax(string);
		for (int currentCharacter = 0; currentCharacter < string.length(); currentCharacter++) {
			int character = string.charAt(currentCharacter);
			if (character > 255) {
				character = 32;
			}

			// Effect
			if (textEffects) {
				if (character == 60) {
					startIndex = currentCharacter;
				} else {
					if (character == 62 && startIndex != -1) {
						String effectString = string.substring(startIndex + 1, currentCharacter);
						startIndex = -1;
						if (effectString.equals(startEffect)) {
							character = 60;
						} else if (effectString.equals(endEffect)) {
							character = 62;
						} else if (effectString.equals(aRSString_4135)) {
							character = 160;
						} else if (effectString.equals(aRSString_4162)) {
							character = 173;
						} else if (effectString.equals(aRSString_4165)) {
							character = 215;
						} else if (effectString.equals(aRSString_4147)) {
							character = 128;
						} else if (effectString.equals(aRSString_4163)) {
							character = 169;
						} else if (effectString.equals(aRSString_4169)) {
							character = 174;
						} else {
							if (effectString.startsWith(startImage)) {
								try {
									String sub = effectString.substring(4);
									if (sub.length() > 0) {
										int imageId = Integer.parseInt(sub);
										if (imageId > -1) {
											Sprite icon = chatImages[imageId];
											int iconModY = icon.myHeight;
											if (transparency == 256) {
												icon.drawSprite(drawX, (drawY + baseCharacterHeight - iconModY));
											} else {
												icon.drawSprite(drawX, (drawY + baseCharacterHeight - iconModY), transparency);
											}
											drawX += icon.myWidth;
										}
									}
								} catch (Exception exception) {
									exception.printStackTrace();
								}
							} else if (effectString.startsWith(startIcon)) {
								try {
									String sub = effectString.substring(5);
									if (sub.length() > 0) {
										int imageId = Integer.parseInt(sub);
										if (imageId > -1) {
											Sprite icon = iconPack[imageId];
											int iconModY = icon.myHeight;
											if (transparency == 256) {
												icon.drawSprite(drawX, (drawY + baseCharacterHeight - iconModY));
											} else {
												icon.drawSprite(drawX, (drawY + baseCharacterHeight - iconModY), transparency);
											}
											drawX += icon.myWidth;
										}
									}
								} catch (Exception exception) {
									exception.printStackTrace();
								}
							} else if (effectString.startsWith(startClanImage)) {
								try {
									String sub = effectString.substring(5);
									if (sub.length() > 0) {
										int imageId = Integer.parseInt(sub);
										if (imageId > -1) {
											Sprite icon = clanImages[imageId];
											int iconModY = icon.myHeight + icon.drawOffsetY + 1;
											if (transparency == 256) {
												icon.drawSprite(drawX, (drawY + baseCharacterHeight - iconModY));
											} else {
												icon.drawSprite(drawX, (drawY + baseCharacterHeight - iconModY), transparency);
											}
											drawX += 11;
										}
									}
								} catch (Exception exception) {
									exception.printStackTrace();
								}
							} else {
								setTextEffects(effectString);
							}
							continue;
						}
					}
				}
			}

			// Draw character
			if (startIndex == -1) {
				int width = characterWidths[character];
				int height = characterHeights[character];
				if (character != 32) {
					if (transparency == 256) {
						if (textShadowColor != -1) {
							drawCharacter(character, drawX + characterDrawXOffsets[character] + 1,
									drawY + characterDrawYOffsets[character] + 1, width, height, textShadowColor,
									true);
						}
						drawCharacter(character, drawX + characterDrawXOffsets[character],
								drawY + characterDrawYOffsets[character], width, height, textColor, false);
					} else {
						if (textShadowColor != -1) {
							drawTransparentCharacter(character, drawX + characterDrawXOffsets[character] + 1,
									drawY + characterDrawYOffsets[character] + 1, width, height, textShadowColor,
									transparency, true);
						}
						drawTransparentCharacter(character, drawX + characterDrawXOffsets[character],
								drawY + characterDrawYOffsets[character], width, height, textColor, transparency,
								false);
					}
				} else if (anInt4178 > 0) {
					anInt4175 += anInt4178;
					drawX += anInt4175 >> 8;
					anInt4175 &= 0xff;
				}
				int lineWidth = characterScreenWidths[character];
				if (strikethroughColor != -1) {
					drawHorizontalLine(drawY + (int) ((double) baseCharacterHeight * 0.6999999999),
							strikethroughColor, lineWidth, drawX);
				}
				if (underlineColor != -1) {

					drawHorizontalLine(drawX, drawY + baseCharacterHeight, lineWidth, underlineColor);
				}
				drawX += lineWidth;
			}

		}
	}

	public void drawRAString(String string, int drawX, int drawY, int color, int shadow) {
		if (string != null) {
			setColorAndShadow(color, shadow);
			drawBasicString(string, drawX - getTextWidth(string), drawY);
		}
	}

	public void drawBaseStringMoveXY(String string, int drawX, int drawY, int[] xModifier, int[] yModifier) {
		drawY -= baseCharacterHeight;
		int startIndex = -1;
		int modifierOffset = 0;
		for (int currentCharacter = 0; currentCharacter < string.length(); currentCharacter++) {
			int character = string.charAt(currentCharacter);
			if (character == 60) {
				startIndex = currentCharacter;
			} else {
				if (character == 62 && startIndex != -1) {
					String effectString = string.substring(startIndex + 1, currentCharacter);
					startIndex = -1;
					if (effectString.equals(startEffect)) {
						character = 60;
					} else if (effectString.equals(endEffect)) {
						character = 62;
					} else if (effectString.equals(aRSString_4135)) {
						character = 160;
					} else if (effectString.equals(aRSString_4162)) {
						character = 173;
					} else if (effectString.equals(aRSString_4165)) {
						character = 215;
					} else if (effectString.equals(aRSString_4147)) {
						character = 128;
					} else if (effectString.equals(aRSString_4163)) {
						character = 169;
					} else if (effectString.equals(aRSString_4169)) {
						character = 174;
					} else {
						if (effectString.startsWith(startImage)) {
							try {
								int xModI;
								if (xModifier != null) {
									xModI = xModifier[modifierOffset];
								} else {
									xModI = 0;
								}
								int yMod;
								if (yModifier != null) {
									yMod = yModifier[modifierOffset];
								} else {
									yMod = 0;
								}
								modifierOffset++;
								int iconId = Integer.valueOf(effectString.substring(4));
								Sprite icon = chatImages[iconId];
								int iconOffsetY = icon.maxHeight;
								if (transparency == 256) {
									icon.drawSprite(drawX + xModI, (drawY + baseCharacterHeight - iconOffsetY + yMod));
								} else {
									icon.drawSprite(drawX + xModI, (drawY + baseCharacterHeight - iconOffsetY + yMod),
											transparency);
								}
								drawX += icon.maxWidth;
							} catch (Exception exception) {
								exception.printStackTrace();
							}
						} else if (effectString.startsWith(startIcon)) {
							try {
								int xModI;
								if (xModifier != null) {
									xModI = xModifier[modifierOffset];
								} else {
									xModI = 0;
								}
								int yMod;
								if (yModifier != null) {
									yMod = yModifier[modifierOffset];
								} else {
									yMod = 0;
								}
								modifierOffset++;
								int iconId = Integer.valueOf(effectString.substring(5));
								Sprite icon = iconPack[iconId];
								int iconOffsetY = icon.maxHeight;
								if (transparency == 256) {
									icon.drawSprite(drawX + xModI, (drawY + baseCharacterHeight - iconOffsetY + yMod));
								} else {
									icon.drawSprite(drawX + xModI, (drawY + baseCharacterHeight - iconOffsetY + yMod),
											transparency);
								}
								drawX += icon.maxWidth;
							} catch (Exception exception) {
								exception.printStackTrace();
							}
						} else {
							setTextEffects(effectString);
						}
						continue;
					}
				}
				if (startIndex == -1) {
					int width = characterWidths[character];
					int height = characterHeights[character];
					int xOff;
					if (xModifier != null) {
						xOff = xModifier[modifierOffset];
					} else {
						xOff = 0;
					}
					int yOff;
					if (yModifier != null) {
						yOff = yModifier[modifierOffset];
					} else {
						yOff = 0;
					}
					modifierOffset++;
					if (character != 32) {
						if (transparency == 256) {
							if (textShadowColor != -1) {
								drawCharacter(character, (drawX + characterDrawXOffsets[character] + 1 + xOff),
										(drawY + characterDrawYOffsets[character] + 1 + yOff), width, height,
										textShadowColor, true);
							}
							drawCharacter(character, drawX + characterDrawXOffsets[character] + xOff,
									drawY + characterDrawYOffsets[character] + yOff, width, height, textColor, false);
						} else {
							if (textShadowColor != -1) {
								drawTransparentCharacter(character,
										(drawX + characterDrawXOffsets[character] + 1 + xOff),
										(drawY + characterDrawYOffsets[character] + 1 + yOff), width, height,
										textShadowColor, transparency, true);
							}
							drawTransparentCharacter(character, drawX + characterDrawXOffsets[character] + xOff,
									drawY + characterDrawYOffsets[character] + yOff, width, height, textColor,
									transparency, false);
						}
					} else if (anInt4178 > 0) {
						anInt4175 += anInt4178;
						drawX += anInt4175 >> 8;
						anInt4175 &= 0xff;
					}
					int i_109_ = characterScreenWidths[character];
					if (strikethroughColor != -1) {
						Rasterizer2D.drawHorizontalLine(drawX, drawY + (int) (baseCharacterHeight * 0.7), i_109_,
								strikethroughColor);
					}
					if (underlineColor != -1) {
						Rasterizer2D.drawHorizontalLine(drawX, drawY + baseCharacterHeight, i_109_, underlineColor);
					}
					drawX += i_109_;
				}
			}
		}
	}

	public void setTextEffects(String string) {
		do {
			try {
				if (string.startsWith(startColor)) {
					String color = string.substring(4);
					textColor = color.length() < 6 ? Color.decode(color).getRGB() : Integer.parseInt(color, 16);
				} else if (string.equals(endColor)) {
					textColor = defaultColor;
				} else if (string.startsWith(startTransparency)) {
					transparency = Integer.valueOf(string.substring(6));
				} else if (string.equals(endTransparency)) {
					transparency = defaultTransparency;
				} else if (string.startsWith(startStrikethrough)) {
					strikethroughColor = Integer.valueOf(string.substring(4));
				} else if (string.equals(defaultStrikethrough)) {
					strikethroughColor = 8388608;
				} else if (string.equals(endStrikethrough)) {
					strikethroughColor = -1;
				} else if (string.startsWith(startUnderline)) {
					underlineColor = Integer.valueOf(string.substring(2));
				} else if (string.equals(startDefaultUnderline)) {
					underlineColor = 0;
				} else if (string.equals(endUnderline)) {
					underlineColor = -1;
				} else if (string.startsWith(startShadow)) {
					textShadowColor = Integer.valueOf(string.substring(5));
				} else if (string.equals(startDefaultShadow)) {
					textShadowColor = 0;
				} else if (string.equals(endShadow)) {
					textShadowColor = defaultShadow;
				} else {
					if (!string.equals(lineBreak)) {
						break;
					}
					setDefaultTextEffectValues(defaultColor, defaultShadow, defaultTransparency);
				}
			} catch (Exception exception) {
				break;
			}
			break;
		} while (false);
	}

	public void setColorAndShadow(int color, int shadow) {
		strikethroughColor = -1;
		underlineColor = -1;
		textShadowColor = defaultShadow = shadow;
		textColor = defaultColor = color;
		transparency = defaultTransparency = 256;
		anInt4178 = 0;
		anInt4175 = 0;
	}

	public void setColorAndShadowAndTrans(int color, int shadow, int trasparency) {
		strikethroughColor = -1;
		underlineColor = -1;
		textShadowColor = defaultShadow = shadow;
		textColor = defaultColor = color;
		transparency = trasparency == 0 ? 256 : transparency;
		defaultTransparency = transparency;
		;
		anInt4178 = 0;
		anInt4175 = 0;
	}

	public int getTextWidth(String string) {
		if (string == null) {
			return 0;
		}
		string = handleOldSyntax(string);
		int startIndex = -1;
		int finalWidth = 0;
		for (int currentCharacter = 0; currentCharacter < string.length(); currentCharacter++) {
			int character = string.charAt(currentCharacter);
			if (character > 255) {
				character = 32;
			}
			if (character == 60) {
				startIndex = currentCharacter;
			} else {
				if (character == 62 && startIndex != -1) {
					String effectString = string.substring(startIndex + 1, currentCharacter);
					startIndex = -1;
					if (effectString.equals(startEffect)) {
						character = 60;
					} else if (effectString.equals(endEffect)) {
						character = 62;
					} else if (effectString.equals(aRSString_4135)) {
						character = 160;
					} else if (effectString.equals(aRSString_4162)) {
						character = 173;
					} else if (effectString.equals(aRSString_4165)) {
						character = 215;
					} else if (effectString.equals(aRSString_4147)) {
						character = 128;
					} else if (effectString.equals(aRSString_4163)) {
						character = 169;
					} else if (effectString.equals(aRSString_4169)) {
						character = 174;
					} else {
						if (effectString.startsWith(startImage)) {
							try {// <img=
								int iconId = Integer.valueOf(effectString.substring(4));
								finalWidth += chatImages[iconId].maxWidth;
							} catch (Exception exception) {
								exception.printStackTrace();
							}
						}
						if (effectString.startsWith(startClanImage)) {
							try {// <img=
								int iconId = Integer.valueOf(effectString.substring(5));
								finalWidth += clanImages[iconId].maxWidth;
							} catch (Exception exception) {
								exception.printStackTrace();
							}
						}
						if (effectString.startsWith(startIcon)) {
							try {// <img=
								int iconId = Integer.valueOf(effectString.substring(5));
								finalWidth += iconPack[iconId].maxWidth;
							} catch (Exception exception) {
								exception.printStackTrace();
							}
						}
						continue;
					}
				}
				if (startIndex == -1) {
					finalWidth += characterScreenWidths[character];
				}
			}
		}
		return finalWidth;
	}

	public void drawBasicString(String string, int drawX, int drawY, int color, int shadow) {
		drawBasicString(string, drawX, drawY, color, shadow, true);
	}

	public void drawBasicString(String string, int drawX, int drawY, int color, int shadow, boolean textEffects) {
		if (string != null) {
			setColorAndShadow(color, shadow);
			drawBasicString(string, drawX, drawY, textEffects);
		}
	}

	public void drawCenteredString(String string, int drawX, int drawY, int color, int shadow) {
		if (string != null) {
			setColorAndShadow(color, shadow);
			string = handleOldSyntax(string);
			drawBasicString(string, drawX - getTextWidth(string) / 2, drawY);
		}
	}

	private static String replace(String text, String replace, String replaceWith) {
		if (text.contains(replace)) {
			return text.replaceAll(replace, replaceWith);
		}
		return text;
	}

	public static String handleOldSyntax(String text) {
		if (text.contains("@")) {
			text = replace(text, "@pur@", "<col=A10081>");
			text = replace(text, "@red@", "<col=ff0000>");
			text = replace(text, "@gre@", "<col=65280>");
			text = replace(text, "@blu@", "<col=255>");
			text = replace(text, "@bl2@", "<col=0F0085>");
			text = replace(text, "@bl3@", "<col=00AFFF>");
			text = replace(text, "@yel@", "<col=ffff00>");
			text = replace(text, "@cya@", "<col=65535>");
			text = replace(text, "@mag@", "<col=ff00ff>");
			text = replace(text, "@whi@", "<col=ffffff>");
			text = replace(text, "@gol@","<col=FFD200>");
			text = replace(text, "@lre@", "<col=ff9040>");
			text = replace(text, "@dre@", "<col=800000>");
			text = replace(text, "@bla@", "<col=0>");
			text = replace(text, "@or0@", "<col=A67711>");
			text = replace(text, "@or1@", "<col=ffb000>");
			text = replace(text, "@or2@", "<col=ff7000>");
			text = replace(text, "@or3@", "<col=ff3000>");
            text = replace(text, "@or4@", "<col=FF9933>");
			text = replace(text, "@gr0@", "<col=148200>");
			text = replace(text, "@gr1@", "<col=c0ff00>");
			text = replace(text, "@gr2@", "<col=80ff00>");
			text = replace(text, "@gr3@", "<col=40ff00>");
			text = replace(text, "@OR0", "<col=<A67711>");
			text = replace(text, "@PUR@", "<col=A10081>");
			text = replace(text, "@RED@", "<col=ffff00>");
			text = replace(text, "@GRE@", "<col=65280>");
			text = replace(text, "@BLU@", "<col=255>");
			text = replace(text, "@YEL@", "<col=ff0000>");
			text = replace(text, "@CYA@", "<col=65535>");
			text = replace(text, "@MAG@", "<col=ff00ff>");
			text = replace(text, "@WHI@", "<col=ffffff>");
			text = replace(text, "@LRE@", "<col=ff9040>");
			text = replace(text, "@DRE@", "<col=800000>");
			text = replace(text, "@BLA@", "<col=0>");
			text = replace(text, "@OR1@", "<col=ffb000>");
			text = replace(text, "@OR2@", "<col=ff7000>");
			text = replace(text, "@OR3@", "<col=ff3000>");
			text = replace(text, "@GR1@", "<col=c0ff00>");
			text = replace(text, "@GR2@", "<col=80ff00>");
			text = replace(text, "@GR3@", "<col=40ff00>");
			if (text.contains("@cr")) {
				text = replace(text, "@cr1@", "<img=0>");
				text = replace(text, "@cr2@", "<img=2>");
				text = replace(text, "@cr3@", "<img=3>");
				text = replace(text, "@cr4@", "<img=4>");
				text = replace(text, "@cr5@", "<img=5>");
				text = replace(text, "@cr6@", "<img=6>");
				text = replace(text, "@cr7@", "<img=7>");
				text = replace(text, "@cr8@", "<img=8>");
				text = replace(text, "@cr9@", "<img=9>");
				text = replace(text, "@cr10@", "<img=10>");
				text = replace(text, "@cr11@", "<img=11>");
				text = replace(text, "@cr12@", "<img=12>");
				text = replace(text, "@cr13@", "<img=13>");
				text = replace(text, "@cr14@", "<img=14>");
				text = replace(text, "@cr15@", "<img=15>");
				text = replace(text, "@cr16@", "<img=16>");
				text = replace(text, "@cr17@", "<img=17>");
				text = replace(text, "@cr18@", "<img=18>");
				text = replace(text, "@cr19@", "<img=19>");
				text = replace(text, "@cr20@", "<img=20>");
				text = replace(text, "@cr21@", "<img=21>");
				text = replace(text, "@cr22@", "<img=22>");
				text = replace(text, "@cr25@", "<img=25>");
				text = replace(text, "@cr26@", "<img=26>");
			}
		}
		return text;
	}

	public static void nullLoader() {
		startEffect = null;
		endEffect = null;
		aRSString_4135 = null;
		aRSString_4162 = null;
		aRSString_4165 = null;
		aRSString_4147 = null;
		aRSString_4163 = null;
		aRSString_4169 = null;
		startImage = null;
		startIcon = null;
		lineBreak = null;
		startColor = null;
		endColor = null;
		startTransparency = null;
		endTransparency = null;
		startUnderline = null;
		startDefaultUnderline = null;
		endUnderline = null;
		startShadow = null;
		startDefaultShadow = null;
		endShadow = null;
		startStrikethrough = null;
		defaultStrikethrough = null;
		endStrikethrough = null;
		aRSString_4143 = null;
		splitTextStrings = null;
	}

	public static void createTransparentCharacterPixels(int[] is, byte[] is_0_, int i, int i_1_, int i_2_, int i_3_,
			int i_4_, int i_5_, int i_6_, int i_7_) {
		i = ((i & 0xff00ff) * i_7_ & ~0xff00ff) + ((i & 0xff00) * i_7_ & 0xff0000) >> 8;
		i_7_ = 256 - i_7_;
		for (int i_8_ = -i_4_; i_8_ < 0; i_8_++) {
			for (int i_9_ = -i_3_; i_9_ < 0; i_9_++) {
				if (is_0_[i_1_++] != 0) {
					int i_10_ = is[i_2_];
					drawAlpha(is, i_2_++, (((i_10_ & 0xff00ff) * i_7_ & 0xff00ff00) + ((i_10_ & 0xff00) * i_7_ & 0xff0000) >> 8)
							+ i, 255);
				} else {
					i_2_++;
				}
			}
			i_2_ += i_5_;
			i_1_ += i_6_;
		}
	}

	public void drawTransparentCharacter(int i, int i_11_, int i_12_, int i_13_, int i_14_, int i_15_, int i_16_,
			boolean bool) {
		int i_17_ = i_11_ + i_12_ * Rasterizer2D.width;
		int i_18_ = Rasterizer2D.width - i_13_;
		int i_19_ = 0;
		int i_20_ = 0;
		if (i_12_ < Rasterizer2D.yClipStart) {
			int i_21_ = Rasterizer2D.yClipStart - i_12_;
			i_14_ -= i_21_;
			i_12_ = Rasterizer2D.yClipStart;
			i_20_ += i_21_ * i_13_;
			i_17_ += i_21_ * Rasterizer2D.width;
		}
		if (i_12_ + i_14_ > Rasterizer2D.yClipEnd) {
			i_14_ -= i_12_ + i_14_ - Rasterizer2D.yClipEnd;
		}
		if (i_11_ < Rasterizer2D.xClipStart) {
			int i_22_ = Rasterizer2D.xClipStart - i_11_;
			i_13_ -= i_22_;
			i_11_ = Rasterizer2D.xClipStart;
			i_20_ += i_22_;
			i_17_ += i_22_;
			i_19_ += i_22_;
			i_18_ += i_22_;
		}
		if (i_11_ + i_13_ > Rasterizer2D.xClipEnd) {
			int i_23_ = i_11_ + i_13_ - Rasterizer2D.xClipEnd;
			i_13_ -= i_23_;
			i_19_ += i_23_;
			i_18_ += i_23_;
		}
		if (i_13_ > 0 && i_14_ > 0) {
			createTransparentCharacterPixels(Rasterizer2D.pixels, fontPixels[i], i_15_, i_20_, i_17_, i_13_, i_14_,
					i_18_, i_19_, i_16_);
		}
	}

	private void createCharacterPixels(int ai[], byte abyte0[], int i, int j, int k, int l, int i1, int j1, int k1) {
		int l1 = -(l >> 2);
		l = -(l & 3);
		for(int i2 = -i1; i2 < 0; i2++) {
			for(int j2 = l1; j2 < 0; j2++) {
				if(abyte0[j++] != 0)
					drawAlpha(ai, k++, i, 255);
				else
					k++;
				if(abyte0[j++] != 0)
					drawAlpha(ai, k++, i, 255);
				else
					k++;
				if(abyte0[j++] != 0)
					drawAlpha(ai, k++, i, 255);
				else
					k++;
				if(abyte0[j++] != 0)
					drawAlpha(ai, k++, i, 255);
				else
					k++;
			}
			for(int k2 = l; k2 < 0; k2++)
				if(abyte0[j++] != 0)
					drawAlpha(ai, k++, i, 255);
				else
					k++;

			k += j1;
			j += k1;
		}
	}


	public void drawCharacter(int character, int i_35_, int i_36_, int i_37_, int i_38_, int i_39_, boolean bool) {
		int i_40_ = i_35_ + i_36_ * Rasterizer2D.width;
		int i_41_ = Rasterizer2D.width - i_37_;
		int i_42_ = 0;
		int i_43_ = 0;
		if (i_36_ < Rasterizer2D.yClipStart) {
			int i_44_ = Rasterizer2D.yClipStart - i_36_;
			i_38_ -= i_44_;
			i_36_ = Rasterizer2D.yClipStart;
			i_43_ += i_44_ * i_37_;
			i_40_ += i_44_ * Rasterizer2D.width;
		}
		if (i_36_ + i_38_ > Rasterizer2D.yClipEnd) {
			i_38_ -= i_36_ + i_38_ - Rasterizer2D.yClipEnd;
		}
		if (i_35_ < Rasterizer2D.xClipStart) {
			int i_45_ = Rasterizer2D.xClipStart - i_35_;
			i_37_ -= i_45_;
			i_35_ = Rasterizer2D.xClipStart;
			i_43_ += i_45_;
			i_40_ += i_45_;
			i_42_ += i_45_;
			i_41_ += i_45_;
		}
		if (i_35_ + i_37_ > Rasterizer2D.xClipEnd) {
			int i_46_ = i_35_ + i_37_ - Rasterizer2D.xClipEnd;
			i_37_ -= i_46_;
			i_42_ += i_46_;
			i_41_ += i_46_;
		}
		if (i_37_ > 0 && i_38_ > 0) {
			createCharacterPixels(Rasterizer2D.pixels, fontPixels[character], i_39_, i_43_, i_40_, i_37_, i_38_, i_41_,
					i_42_);

		}
	}

	static {
		startTransparency = "trans=";
		startStrikethrough = "str=";
		startDefaultShadow = "shad";
		startColor = "col=";
		lineBreak = "br";
		defaultStrikethrough = "str";
		endUnderline = "/currentY";
		startImage = "img=";
		startIcon = "icon=";
		startClanImage = "clan=";
		startShadow = "shad=";
		startUnderline = "currentY=";
		endColor = "/col";
		startDefaultUnderline = "currentY";
		endTransparency = "/trans";

		aRSString_4143 = Integer.toString(100);
		aRSString_4135 = "nbsp";
		aRSString_4169 = "reg";
		aRSString_4165 = "times";
		aRSString_4162 = "shy";
		aRSString_4163 = "copy";
		endEffect = "gt";
		aRSString_4147 = "euro";
		startEffect = "lt";
		defaultTransparency = 256;
		defaultShadow = -1;
		anInt4175 = 0;
		textShadowColor = -1;
		textColor = 0;
		defaultColor = 0;
		strikethroughColor = -1;
		splitTextStrings = new String[100];
		underlineColor = -1;
		anInt4178 = 0;
		transparency = 256;
	}
}