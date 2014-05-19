package com.rivergillis.androidgames.mineralminer;


/**
 * A rock object, each rock has a name, a texture, a worth, and a health.
 * Rocks also have armor.
 * @author jgillis
 *
 */
public class Rock {
	
	public String name;
	public long health;
	public long initialHealth;
	public long worth;
	public long armor;
	public int breakLevel;
	
	public Rock(String name, long health, long worth, long armor) {
		this.name = name;
		this.health = health;
		this.initialHealth = health;
		this.worth = worth;
		this.armor = armor;
		breakLevel = 0;
	}
	
}
