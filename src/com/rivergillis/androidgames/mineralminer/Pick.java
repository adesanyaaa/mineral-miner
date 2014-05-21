package com.rivergillis.androidgames.mineralminer;

import java.util.Random;

import android.util.Log;

/**
 * A pick object, each pick has a texture, and a damage
 * @author jgillis
 *
 */
public class Pick extends Upgrade {
	public static final int UPGRADE_NUMBER = 1;
	
	long damage;
	long nextDamage;
	Random rand;

	public Pick(long initialPrice, float priceIncrease) {
		super(initialPrice, priceIncrease);
		rand = new Random();
		maxLevel = 99;
		name = "Pickaxe";
		description = "Increases the strength of";
		lowerDescription = "your weapon";
		damage = 2;
		nextDamage = (long)(damage + ((level + 1) * 1.5));
	}
	
	@Override
	public void buyUpgrade() {
		if (Wallet.canBuy(UPGRADE_NUMBER)) {
			super.buyUpgrade();
			damage = nextDamage;
			if (level < 10)
				nextDamage = (long)(damage + ((level * 1.6) * 2.0));
			else if (level < 20)
				nextDamage = (long)(damage + ((level * 2.8) * 3.0));
			else if (level < 30)
				nextDamage = (long)(damage + ((level * 3.55) * 4.0));
			else if (level < 40)
				nextDamage = (long)(damage + ((level * 5.2) * 5.5));
			else if (level < 50)
				nextDamage = (long)(damage + ((level * 7.2) * 7.0));
			else if (level < 60)
				nextDamage = (long)(damage + ((level * 10.8) * 10.0));
			else if (level < 70)
				nextDamage = (long)(damage + ((level * 13.3) * 13.0));
			else if (level < 80)
				nextDamage = (long)(damage + ((level * 25.8) * 18.0));
			else if (level < 90)
				nextDamage = (long)(damage + ((level * 40.1) * 20.0));
			else
				nextDamage = (long)(damage + ((level * 80.0) * 25.0));
		} else {
			Log.d("Upgrade", "Error, not enough money in wallet");
		}
	}
	
	@Override
	public void increasePrice() {
		price = (long)(price * priceIncrease);
		if (priceIncrease > 1.6) {
			priceIncrease -= 0.2;		// used to balance the prices
		}
	}
}
