package com.rivergillis.androidgames.mineralminer;

import com.rivergillis.androidgames.framework.gl.SpriteBatcher;
import com.rivergillis.androidgames.framework.gl.TextureRegion;
import com.rivergillis.androidgames.framework.math.Rectangle;

public class MinerRenderer {

	public void drawRock(SpriteBatcher batcher, int breakLevel) {
		switch (Wallet.currentRock) {
		default:
		case 0:
			if (breakLevel < 1)
				batcher.drawSprite(160, 240, 200, 200, Assets.rock);
			else if (breakLevel == 1)
				batcher.drawSprite(160, 240, 200, 200, Assets.rockd1);
			else
				batcher.drawSprite(160, 240, 200, 200, Assets.rockd2);
			break;
		case 1:
			if (breakLevel < 1)
				batcher.drawSprite(160, 240, 200, 200, Assets.rock2);
			else if (breakLevel == 1)
				batcher.drawSprite(160, 240, 200, 200, Assets.rock2d1);
			else
				batcher.drawSprite(160, 240, 200, 200, Assets.rock2d2);
			break;
		}
	}
	
	public void drawPickHit(SpriteBatcher batcher) {
		switch (Wallet.pick.level) {
		default:
		case 0:
			batcher.drawSprite(160 + 50, 240 + 50, 200, 200, Assets.pickHit);
			break;
		case 1:
			batcher.drawSprite(160 + 50, 240 + 50, 200, 200, Assets.pickHit2);
			break;
		}
	}
	
	public void drawPick(SpriteBatcher batcher) {
		switch (Wallet.pick.level) {
		default:
		case 0:
			batcher.drawSprite(160 + 50, 240 + 50, 200, 200, Assets.pick);
			break;
		case 1:
			batcher.drawSprite(160 + 50, 240 + 50, 200, 200, Assets.pick2);
		}
	}
	
	public void drawButtons(SpriteBatcher batcher) {
		if (Wallet.currentRock < MinerScreen.NUMBER_OF_ROCKS - 1) {
			if (Wallet.defeatedRocks[Wallet.currentRock])
				batcher.drawSprite(300, 20, 32, 32, Assets.rightButton);
			else
				batcher.drawSprite(300, 20, 32, 32, Assets.rightButtonDimmed);
		}
		if (Wallet.currentRock > 0)
			batcher.drawSprite(20, 20, 32, 32, Assets.leftButton);
		batcher.drawSprite(300, 460, 32, 32, Assets.rightButton);
	}
	
	public void drawBackground(SpriteBatcher batcher) {
		switch (Wallet.currentBackground) {
		case 0:
			batcher.drawSprite(160, 240, 320, 480, Assets.backgroundRegion);
		}
	}
	
	public static Rectangle buyBounds = new Rectangle(271 - 32, 25 - 16, 64, 32);
	public static Rectangle exitBounds = new Rectangle(300 - 16, 460 - 16, 32, 32);
	public static Rectangle optionsBOunds = new Rectangle(20 - 16, 460 - 16, 32, 32);
	
	public void drawUpgradeScreen(SpriteBatcher batcher, int selected, boolean canBuy) {
		for (int i = 0; i < 3; i++) {
			batcher.drawSprite(160, 240, 320, 480, Assets.upgradesRegion);	// for opacity
		}
		
		if (canBuy)
			batcher.drawSprite(271, 25, 64, 32, Assets.buyButton);
		else
			batcher.drawSprite(271, 25, 64, 32, Assets.buyButtonDimmed);
		
		batcher.drawSprite(49, 400, 64, 64, Assets.upgrade1);
		batcher.drawSprite(123, 400, 64, 64, Assets.upgrade1);
		batcher.drawSprite(197, 400, 64, 64, Assets.upgrade1);
		batcher.drawSprite(271, 400, 64, 64, Assets.upgrade1);
		batcher.drawSprite(49, 326, 64, 64, Assets.upgrade1);
		batcher.drawSprite(123, 326, 64, 64, Assets.upgrade1);
		batcher.drawSprite(197, 326, 64, 64, Assets.upgrade1);
		batcher.drawSprite(271, 326, 64, 64, Assets.upgrade1);
		batcher.drawSprite(49, 252, 64, 64, Assets.upgrade1);
		batcher.drawSprite(123, 252, 64, 64, Assets.upgrade1);
		batcher.drawSprite(197, 252, 64, 64, Assets.upgrade1);
		batcher.drawSprite(271, 252, 64, 64, Assets.upgrade1);
		
		switch(selected) {
		case 1:
			batcher.drawSprite(49, 400, 64, 64, Assets.upgradeSelection);
			break;
		case 2:
			batcher.drawSprite(123, 400, 64, 64, Assets.upgradeSelection);
			break;
		case 3:
			batcher.drawSprite(197, 400, 64, 64, Assets.upgradeSelection);
			break;
		case 4:
			batcher.drawSprite(271, 400, 64, 64, Assets.upgradeSelection);
			break;
		case 5:
			batcher.drawSprite(49, 326, 64, 64, Assets.upgradeSelection);
			break;
		case 6:
			batcher.drawSprite(123, 326, 64, 64, Assets.upgradeSelection);
			break;
		case 7:
			batcher.drawSprite(197, 326, 64, 64, Assets.upgradeSelection);
			break;
		case 8:
			batcher.drawSprite(271, 326, 64, 64, Assets.upgradeSelection);
			break;
		case 9:
			batcher.drawSprite(49, 252, 64, 64, Assets.upgradeSelection);
			break;
		case 10:
			batcher.drawSprite(123, 252, 64, 64, Assets.upgradeSelection);
			break;
		case 11:
			batcher.drawSprite(197, 252, 64, 64, Assets.upgradeSelection);
			break;
		case 12:
			batcher.drawSprite(271, 252, 64, 64, Assets.upgradeSelection);
		}
		
		batcher.drawSprite(300, 460, 32, 32, Assets.exitButton);
		batcher.drawSprite(20, 460, 32, 32, Assets.optionsButton);
	}
	
	String top = "";		// declare variables here to prevent constant garbage collection
	String upper = "";
	String middle = "";
	String lower = "";
	String topSub = "";
	String name = "";
	String level = "";
	String effect = "";
	String effect2 = "";
	String price = "";
	String description = "";
	String lowerDescr = "";
	
	float topWidth = 0;
	float upWidth = 0;
	float midWidth = 0;
	float lowWidth = 0;
	float tsubWidth = 0;
	float namWidth = 0;
	float levWidth = 0;
	float effWidth = 0;
	float eff2Width = 0;
	float prcWidth = 0;
	float desWidth = 0;
	float ldesWidth = 0;
	
	boolean has2Effects = false;

	public void drawUpgradeText(SpriteBatcher batcher, int selected) {
		top = "Current gold: " + displayLargeNumber(Wallet.coins);
		topSub = "Upgrades";
		topWidth = Assets.font.glyphWidth * top.length();
		tsubWidth = Assets.font.glyphWidth * topSub.length();
		
		Assets.font.drawText(batcher, top, 160 - topWidth / 2, 480 - 15);
		Assets.font.drawText(batcher, topSub, 160 - tsubWidth / 2, 480 - 35);
		
		switch (selected) {
		case 1:
			name = Wallet.pick.name;
			if (Wallet.pick.level < Wallet.pick.maxLevel) {
				level = "Level: " + Wallet.pick.level + " -> " + (Wallet.pick.level + 1);
				effect = "Damage: " + displayLargeNumber(Wallet.pick.damage) + " -> " + displayLargeNumber(Wallet.pick.nextDamage);
				price = "Price: " + displayLargeNumber(Wallet.pick.price) + " gold";
			} else {
				level = "Level: " + Wallet.pick.level + " (MAX)";
				effect = "Damage: " + Wallet.pick.damage + " (MAX)";
				price = "";
			}
			description = Wallet.pick.description;
			lowerDescr = Wallet.pick.lowerDescription;
			effect2 = "";
			has2Effects = false;
			break;
		case 2:
			name = Wallet.armorPierce.name;
			if (Wallet.armorPierce.level < Wallet.armorPierce.maxLevel) {
				level = "Level: " + Wallet.armorPierce.level + " -> " + (Wallet.armorPierce.level + 1);
				effect = "Pierce: " + (int)Wallet.armorPierce.percentPierce + "%" + " -> " + (int)Wallet.armorPierce.nextPierce + "%";
				price = "Price: " + displayLargeNumber(Wallet.armorPierce.price) + " gold";
			} else {
				level = "Level: " + Wallet.armorPierce.level + " (MAX)";
				effect = "Pierce: " + (int)Wallet.armorPierce.percentPierce + "% (MAX)";
				price = "";
			}
			description = Wallet.armorPierce.description;
			lowerDescr = Wallet.armorPierce.lowerDescription;
			effect2 = "Affects familiars";
			has2Effects = true;
			break;
		case 3:
			name = Wallet.mineralMerchant.name;
			if (Wallet.mineralMerchant.level < Wallet.mineralMerchant.maxLevel) {
				level = "Level: " + Wallet.mineralMerchant.level + " -> " + (Wallet.mineralMerchant.level + 1);
				effect = "Chance: " + (int)Wallet.mineralMerchant.percentChance + "%" + " -> " + (int)(Wallet.mineralMerchant.nextChance) + "%";
				effect2 = "Gained: " + (Math.round((Wallet.mineralMerchant.percentGained * 100) * 1000.0f) / 1000.0f) + "%" + " -> ";
				effect2 += (Math.round((Wallet.mineralMerchant.nextGained * 100) * 1000.0f) / 1000.0f) + "%";
				price = "Price: " + displayLargeNumber(Wallet.mineralMerchant.price) + " gold";
			} else {
				level = "Level: " + Wallet.mineralMerchant.level + " (MAX)";
				effect = "Chance: " + (int)Wallet.mineralMerchant.percentChance + "%" + " (MAX)";
				effect2 = "Gained: " + (Math.round((Wallet.mineralMerchant.percentGained * 100) * 1000.0f) / 1000.0f) + "% (MAX)";
				price = "";
			}
			description = Wallet.mineralMerchant.description;
			lowerDescr = Wallet.mineralMerchant.lowerDescription;
			has2Effects = true;
			break;
		case 4:
			name = Wallet.luckyHit.name;
			if (Wallet.luckyHit.level < Wallet.luckyHit.maxLevel) {
				level = "Level: " + Wallet.luckyHit.level + " -> " + (Wallet.luckyHit.level + 1);
				effect = "Chance: " + (int)Wallet.luckyHit.percentChance + "%" + " -> " + (int)(Wallet.luckyHit.nextChance) + "%";
				effect2 = "Damage: " + (int)(Wallet.luckyHit.damagePercent * 100) + "%" + " -> " + (int)(Wallet.luckyHit.nextDamage * 100) + "%";
				price = "Price: " + displayLargeNumber(Wallet.luckyHit.price) + " gold";
			} else {
				level = "Level: " + Wallet.luckyHit.level + " (MAX)";
				effect = "Chance: " + (int)Wallet.luckyHit.percentChance + "% (MAX)";
				effect2 = "Damage: " + (int)(Wallet.luckyHit.damagePercent * 100) + "%" + " (MAX)";
				price = "";
			}
			description = Wallet.luckyHit.description;
			lowerDescr = Wallet.luckyHit.lowerDescription;
			has2Effects = true;
			break;
		case 5:
			name = Wallet.armorChip.name;
			if (Wallet.armorChip.level < Wallet.armorChip.maxLevel) {
				level = "Level: " + Wallet.armorChip.level + " -> " + (Wallet.armorChip.level + 1);
				effect = "Chance: " + (int)Wallet.armorChip.chance + "%" + " -> " + (int)Wallet.armorChip.nextChance + "%";
				effect2 = "Chip: " + displayLargeNumber(Wallet.armorChip.chip) + " -> " + displayLargeNumber(Wallet.armorChip.nextChip);
				price = "Price: " + displayLargeNumber(Wallet.armorChip.price) + " gold";
			} else {
				level = "Level: " + Wallet.armorChip.level + " (MAX)";
				effect = "Chance: " + (int)Wallet.armorChip.chance + " (MAX)";
				effect2 = "Chip: " + displayLargeNumber(Wallet.armorChip.chip) + " (MAX)";
				price = "";
			}
			description = Wallet.armorChip.description;
			lowerDescr = Wallet.armorChip.lowerDescription;
			has2Effects = true;
			break;
		case 8:
			name = Wallet.sharpenedPick.name;
			if (Wallet.sharpenedPick.level < Wallet.sharpenedPick.maxLevel) {
				level = "Level: " + Wallet.sharpenedPick.level + " -> " + (Wallet.sharpenedPick.level + 1);
				effect = "Percent: " + Wallet.sharpenedPick.percentDamage + "% -> " + Wallet.sharpenedPick.nextPercent + "%";
				price = "Price: " + displayLargeNumber(Wallet.sharpenedPick.price) + " gold";
			} else {
				level = "Level: " + Wallet.sharpenedPick.level + " (MAX)";
				effect = "Percent: " + Wallet.sharpenedPick.percentDamage + "% (MAX)";
				price = "";
			}
			effect2 = "";
			description = Wallet.sharpenedPick.description;
			lowerDescr = Wallet.sharpenedPick.lowerDescription;
			has2Effects = false;
			break;
		case 9:
			name = Wallet.addFamiliar.name;
			if (Wallet.addFamiliar.level < Wallet.addFamiliar.maxLevel) {
				level = "Familiars: " + Wallet.addFamiliar.level + " -> " + (Wallet.addFamiliar.level + 1);
				effect = "DPS: " + (Math.round(Wallet.addFamiliar.numberOfFamiliars * Wallet.familiarDamage.damage / (float)Wallet.familiarSpeed.speed * 10.0f) / 10.0f);
				effect += " -> " + (Math.round((Wallet.addFamiliar.numberOfFamiliars + 1)* Wallet.familiarDamage.damage / (float)Wallet.familiarSpeed.speed * 10.0f) / 10.0f);
				price = "Price: " + displayLargeNumber(Wallet.addFamiliar.price) + " gold";
			} else {
				level = "Familiars: " + Wallet.addFamiliar.level + " (MAX)";
				effect = "DPS: " + (Math.round(Wallet.addFamiliar.numberOfFamiliars * Wallet.familiarDamage.damage / (float)Wallet.familiarSpeed.speed * 10.0f) / 10.0f);
				price = "";
			}
			description = Wallet.addFamiliar.description;
			lowerDescr = Wallet.addFamiliar.lowerDescription;
			effect2 = "";
			has2Effects = false;
			break;
		case 10:
			name = Wallet.familiarDamage.name;
			if (Wallet.familiarDamage.level < Wallet.familiarDamage.maxLevel) {
				level = "Level: " + Wallet.familiarDamage.level + " -> " + (Wallet.familiarDamage.level + 1);
				effect = "Damage: " + Wallet.familiarDamage.damage + " -> " + Wallet.familiarDamage.nextDamage;
				effect2 = "DPS: " + (Math.round(Wallet.addFamiliar.numberOfFamiliars * Wallet.familiarDamage.damage / (float)Wallet.familiarSpeed.speed * 10.0f) / 10.0f);
				effect2 += " -> " + (Math.round(Wallet.addFamiliar.numberOfFamiliars * Wallet.familiarDamage.nextDamage / (float)Wallet.familiarSpeed.speed * 10.0f) / 10.0f);
				price = "Price: " + displayLargeNumber(Wallet.familiarDamage.price) + " gold";
			} else {
				level = "Level: " + Wallet.familiarDamage.level + " (MAX)";
				effect = "Damage: " + Wallet.familiarDamage.damage + " (MAX)";
				effect2 = "DPS: " + (Math.round(Wallet.addFamiliar.numberOfFamiliars * Wallet.familiarDamage.damage / (float)Wallet.familiarSpeed.speed * 10.0f) / 10.0f);
				price = "";
			}
			description = Wallet.familiarDamage.description;
			lowerDescr = Wallet.familiarDamage.lowerDescription;
			has2Effects = true;
			break;
		case 11:
			name = Wallet.familiarSpeed.name;
			if (Wallet.familiarSpeed.level < Wallet.familiarSpeed.maxLevel) {
				level = "Level: " + Wallet.familiarSpeed.level + " -> " + (Wallet.familiarSpeed.level + 1);
				effect = "Speed (sec): " + (Math.round(Wallet.familiarSpeed.speed * 10.0f) / 10.0f) + " -> " + (Math.round(Wallet.familiarSpeed.nextSpeed * 10.0f) / 10.0f);
				effect2 = "DPS: " + (Math.round(Wallet.addFamiliar.numberOfFamiliars * Wallet.familiarDamage.damage / (float)Wallet.familiarSpeed.speed * 10.0f) / 10.0f);
				effect2 += " -> " + (Math.round(Wallet.addFamiliar.numberOfFamiliars * Wallet.familiarDamage.damage / (float)Wallet.familiarSpeed.nextSpeed * 10.0f) / 10.0f);
				price = "Price: " + displayLargeNumber(Wallet.familiarSpeed.price) + " gold";
			} else {
				level = "Level: " + Wallet.familiarSpeed.level + " (MAX)";
				effect = "Speed (sec): " + (Math.round(Wallet.familiarSpeed.speed * 10.0f) / 10.0f) + " (MAX)";
				effect2 = "DPS: " + (Math.round(Wallet.addFamiliar.numberOfFamiliars * Wallet.familiarDamage.damage / (float)Wallet.familiarSpeed.speed * 10.0f) / 10.0f);;
				price = "";
			}
			description = Wallet.familiarSpeed.description;
			lowerDescr = Wallet.familiarSpeed.lowerDescription;
			has2Effects = true;
			break;
		case 12:
			name = Wallet.magneticFamiliars.name;
			if (Wallet.magneticFamiliars.level < Wallet.magneticFamiliars.maxLevel) {
				level = "Level: " + Wallet.magneticFamiliars.level + " -> " + (Wallet.magneticFamiliars.level + 1);
				effect = "Gold: " + Math.round(Wallet.magneticFamiliars.goldPerSecond) + " -> " + Math.round(Wallet.magneticFamiliars.nextGoldPerSecond);
				effect2 = "GPS: " + Math.round(Wallet.addFamiliar.numberOfFamiliars * Wallet.magneticFamiliars.goldPerSecond);
				effect2 += " -> " + Math.round(Wallet.addFamiliar.numberOfFamiliars * Wallet.magneticFamiliars.nextGoldPerSecond);
				price = "Price: " + displayLargeNumber(Wallet.magneticFamiliars.price) + " gold";
			} else {
				level = "Level: " + Wallet.magneticFamiliars.level + " (MAX)";
				effect = "Gold: " + Wallet.magneticFamiliars.goldPerSecond + " (MAX)";
				effect2 = "Total GPS: " + Math.round(Wallet.addFamiliar.numberOfFamiliars * Wallet.magneticFamiliars.goldPerSecond);
				price = "";
			}
			description = Wallet.magneticFamiliars.description;
			lowerDescr = Wallet.magneticFamiliars.lowerDescription;
			has2Effects = true;
			break;
		default:
			name = "Error";
			level = "Error";
			effect = "Error";
			effect2 = "";
			price = "Error";
			description = "Error";
			lowerDescr = "Error";
			has2Effects = false;
			break;
		}
		
		namWidth = Assets.font.glyphWidth * name.length();
		levWidth = Assets.font.glyphWidth * level.length();
		effWidth = Assets.font.glyphWidth * effect.length();
		eff2Width = Assets.font.glyphWidth * effect2.length();
		prcWidth = Assets.font.glyphWidth * price.length();
		desWidth = Assets.font.glyphWidth * description.length();
		ldesWidth = Assets.font.glyphWidth * lowerDescr.length();
		
		Assets.font.drawText(batcher, name, 160 - namWidth / 2, 167);
		Assets.font.drawText(batcher, level, 160 - levWidth / 2, 145);
		Assets.font.drawText(batcher, effect, 160 - effWidth / 2, 125);
		if (!has2Effects) {
			Assets.font.drawText(batcher, price, 160 - prcWidth / 2, 105);
			Assets.font.drawText(batcher, description, 160 - desWidth / 2, 85);
			Assets.font.drawText(batcher, lowerDescr, 160 - ldesWidth / 2, 70);
		} else {
			Assets.font.drawText(batcher, effect2, 160 - eff2Width / 2, 105);
			Assets.font.drawText(batcher, price, 160 - prcWidth / 2, 85);
			Assets.font.drawText(batcher, description, 160 - desWidth / 2, 65);
			Assets.font.drawText(batcher, lowerDescr, 160 - ldesWidth / 2, 50);
		}
	}
	
	public void drawGameText(SpriteBatcher batcher, Rock rock) {
		top = "Current gold: " + displayLargeNumber(Wallet.coins);
		upper = rock.name + ", Health: " + displayLargeNumber(rock.health);
		middle = "Armor level: " + displayLargeNumber(rock.armor);
		lower = "Worth " + displayLargeNumber(rock.worth) + " gold";
		topWidth = Assets.font.glyphWidth * top.length();
		upWidth = Assets.font.glyphWidth * upper.length();
		midWidth = Assets.font.glyphWidth * middle.length();
		lowWidth = Assets.font.glyphWidth * lower.length();
		
		Assets.font.drawText(batcher, top, 160 - topWidth / 2, 480 - 15);
		Assets.font.drawText(batcher, upper, 160 - upWidth /2, 120);
		Assets.font.drawText(batcher, middle, 160 - midWidth / 2, 100);
		Assets.font.drawText(batcher, lower, 160 - lowWidth / 2, 80);
	}
	
	public void drawFamiliars(SpriteBatcher batcher) {
		TextureRegion familiarTexture;
		if (Wallet.familiarDamage.level + Wallet.familiarSpeed.level < 5)
			familiarTexture = Assets.familiar1;
		else if (Wallet.familiarDamage.level + Wallet.familiarSpeed.level < 10)
			familiarTexture = Assets.familiar2;
		else
			familiarTexture = Assets.familiar1;
		
		int numFamiliars = Wallet.addFamiliar.numberOfFamiliars;
		int x, y;
		x = 75;
		y = 150;
		for (int i = 0; i < numFamiliars; i++) {
			batcher.drawSprite(x, y, 32, 32, familiarTexture);
			if (i == 12) {
				x = 75;;
				y += 20;
			} else if (i == 25) {
				x = 75;
				y = 320;
			} else if (i == 37) {
				x = 75;
				y = 340;
			} else
				x += 15;
		}
	}
	
	public void drawFamiliarsHit(SpriteBatcher batcher) {
		TextureRegion familiarTexture;
		if (Wallet.familiarDamage.level + Wallet.familiarSpeed.level < 5)
			familiarTexture = Assets.familiarHit1;
		else if (Wallet.familiarDamage.level + Wallet.familiarSpeed.level < 10)
			familiarTexture = Assets.familiarHit2;
		else
			familiarTexture = Assets.familiarHit1;
		
		int numFamiliars = Wallet.addFamiliar.numberOfFamiliars;
		int x, y;
		x = 75;
		y = 150;
		for (int i = 0; i < numFamiliars; i++) {
			batcher.drawSprite(x, y, 32, 32, familiarTexture);
			if (i == 12) {
				x = 75;;
				y += 20;
			} else if (i == 25) {
				x = 75;
				y = 320;
			} else if (i == 37) {
				x = 75;
				y = 340;
			} else
				x += 15;
		}
	}
	
	public void drawOptions(SpriteBatcher batcher) {
		batcher.drawSprite(320 / 2, 480 / 2, 200, 248, Assets.optionsScreen);
	}
	
	String first;
	String second;
	String option1;
	String option2;
	
	float firstWidth;
	float secondWidth;
	float option1Width;
	float option2Width;
	
	public static Rectangle deleteBounds = new Rectangle((320 / 2) - 90, 180 - 15, 180, 30);
	
	public void drawOptionsText(SpriteBatcher batcher, int stage) {
		if (stage < 2) {
			second = "Start over?";
			option1 = "Yes, delete save";
		} else {
			second = "Are you sure?";
			option1 = "Delete it already!";
		}
		first = "Delete save file?";
		option2 = "No, don't delete";
		
		firstWidth = Assets.font.glyphWidth * first.length();
		secondWidth = Assets.font.glyphWidth * second.length();
		option1Width = Assets.font.glyphWidth * option1.length();
		option2Width = Assets.font.glyphWidth * option2.length();
		
		Assets.font.drawText(batcher, first, (160 - firstWidth / 2) + 5, 340);
		Assets.font.drawText(batcher, second, (160 - secondWidth / 2) + 5, 320);
		Assets.font.drawText(batcher, option1, (160 - option1Width / 2) + 5, 180);
		Assets.font.drawText(batcher, option2, (160 - option2Width / 2) + 5, 140);
	}
	
	public String displayLargeNumber(long num) {
		String largeNum = "";
		float tempNum;
		if (num >= 1000000000000000l) { // quadrillion
			tempNum = Math.round((num / 1000000000000000.0f) * 1000.0f) / 1000.0f;
			largeNum = tempNum + "Q";
		} else if (num >= 1000000000000l) { //trillion
			tempNum = Math.round((num / 1000000000000.0f) * 1000.0f) / 1000.0f;
			largeNum = tempNum + "T";
		} else if (num >= 1000000000) { //billion
			tempNum = Math.round((num / 1000000000.0f) * 1000.0f) / 1000.0f;
			largeNum = tempNum + "B";
		} else if (num >= 1000000) {	//million
			tempNum = Math.round((num / 1000000.0f) * 1000.0f) / 1000.0f;
			largeNum = tempNum + "M";
		} else {
			largeNum = Long.toString(num);
		}
		return largeNum;
	}
}
