package com.rivergillis.androidgames.mineralminer;

import android.util.Log;

public class UpgradeArmorChip extends Upgrade{
	public static final int UPGRADE_NUMBER = 5;
	public static final float CHANCE_INCREASE = 1.0f;
	
	public long chip;
	public long nextChip;
	public float chance;
	public float nextChance;
	
	public UpgradeArmorChip(long initialPrice, float priceIncrease) {
		super(initialPrice, priceIncrease);
		name = "Armor Chip";
		description = "Pickaxe gains chance";
		lowerDescription = "to chip off more armor";
		maxLevel = 100;
		chip = 0;
		nextChip = 5;
		chance = 0;
		nextChance = chance + CHANCE_INCREASE;
	}
	
	@Override
	public void buyUpgrade() {
		if (Wallet.canBuy(UPGRADE_NUMBER)) {
			super.buyUpgrade();
			chip = nextChip;
			chance = nextChance;
			nextChance = chance + CHANCE_INCREASE;
			if (level < 15)
				nextChip = chip + 2;
			else if (level < 30)
				nextChip = chip + 5;
			else if (level < 40)
				nextChip = chip + 30;
			else if (level < 50)
				nextChip = chip + 50;
			else if (level < 60)
				nextChip = chip + 60;
			else if (level < 75)
				nextChip = chip + 100;
			else if (level < 85)
				nextChip = chip + 200;
			else
				nextChip = chip + 500;
		} else {
			Log.d("Upgrade", "Error, not enough money in wallet");
		}
	}
	
	@Override
	public void load(int level) {
		super.load(level);
		chance = CHANCE_INCREASE * level;
		nextChance = chance + CHANCE_INCREASE;
	}
}
