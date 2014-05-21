package com.rivergillis.androidgames.mineralminer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.util.Log;

import com.rivergillis.androidgames.framework.FileIO;

/**
 * The wallet contains all player data, and saves and loads all of it.
 * @author jgillis
 *
 */

public class Wallet {
	public static final int CURRENT_GAME_VERSION = 0;
	public static int lastPlayedVersion = -1;
	
	public static boolean[] defeatedRocks;
	public static int currentRock;
	public static int currentBackground;
	
	public static long coins;
	
	public static UpgradeArmorPierce armorPierce;
	public static UpgradeMineralMerchant mineralMerchant;
	public static UpgradeLuckyHit luckyHit;
	public static UpgradeFamiliar addFamiliar;
	public static UpgradeFamiliarDamage familiarDamage;
	public static UpgradeFamiliarSpeed familiarSpeed;
	public static UpgradeMagneticFamiliars magneticFamiliars;
	public static UpgradeArmorChip armorChip;
	
	public static Pick pick;
	
	public static void setUpWallet(FileIO files) {
		armorPierce = new UpgradeArmorPierce(200, 2.0f);
		mineralMerchant = new UpgradeMineralMerchant(200, 2.0f);
		luckyHit = new UpgradeLuckyHit(200, 3.0f);
		addFamiliar = new UpgradeFamiliar(2000, 2.5f);
		familiarDamage = new UpgradeFamiliarDamage(1000, 3.0f);
		familiarSpeed = new UpgradeFamiliarSpeed(200, 2.5f);
		magneticFamiliars = new UpgradeMagneticFamiliars(200, 2.0f);
		armorChip = new UpgradeArmorChip(300, 2.5f);
		pick = new Pick(500, 2.5f);
		defeatedRocks = new boolean[MinerScreen.NUMBER_OF_ROCKS];
		currentRock = 0;
		currentBackground = 0;
		coins = 0;
		load(files);
	}
	
	public static void buySelection(int selectedUpgrade) {
		switch (selectedUpgrade) {
		case 1:
			pick.buyUpgrade();
			break;
		case 2:
			armorPierce.buyUpgrade();
			break;
		case 3:
			mineralMerchant.buyUpgrade();
			break;
		case 4:
			luckyHit.buyUpgrade();
			break;
		case 5:
			armorChip.buyUpgrade();
			break;
		case 9:
			addFamiliar.buyUpgrade();
			break;
		case 10:
			familiarDamage.buyUpgrade();
			break;
		case 11:
			familiarSpeed.buyUpgrade();
			break;
		case 12:
			magneticFamiliars.buyUpgrade();
		}
	}
	
	public static boolean canBuy(int selectedUpgrade) {
		switch (selectedUpgrade) {
		case 1:
			if (coins >= pick.price && pick.level < pick.maxLevel)
				return true;
			return false;
		case 2:
			if (coins >= armorPierce.price && armorPierce.level < armorPierce.maxLevel)
				return true;
			return false;
		case 3:
			if (coins >= mineralMerchant.price && mineralMerchant.level < mineralMerchant.maxLevel)
				return true;
			return false;
		case 4:
			if (coins >= luckyHit.price && luckyHit.level < luckyHit.maxLevel)
				return true;
			return false;
		case 5:
			if (coins >= armorChip.price && armorChip.level < armorChip.maxLevel)
				return true;
			return false;
		case 9:
			if (coins >= addFamiliar.price && addFamiliar.level < addFamiliar.maxLevel)
				return true;
			return false;
		case 10:
			if (coins >= familiarDamage.price && familiarDamage.level < familiarDamage.maxLevel)
				return true;
			return false;
		case 11:
			if (coins >= familiarSpeed.price && familiarSpeed.level < familiarSpeed.maxLevel && familiarSpeed.speed > familiarSpeed.maxSpeed)
				return true;
			return false;
		case 12:
			if (coins >= magneticFamiliars.price && magneticFamiliars.level < magneticFamiliars.maxLevel)
				return true;
			return false;
		default:
			return false;
		}
	}
	
    public static void save(FileIO files) {
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(files.writeFile(".mm")));
            out.write("lastplayedversion\n");
            out.write(Integer.toString(CURRENT_GAME_VERSION));
            out.write("\n");
            
            out.write("currentr\n");
            out.write(Integer.toString(currentRock));
            out.write("\n");
            
            out.write("currentb\n");
            out.write(Integer.toString(currentBackground));
            out.write("\n");
            
            out.write("coins\n");
            out.write(Long.toString(coins));
            out.write("\n");
            
            for (int i = 0; i < MinerScreen.NUMBER_OF_ROCKS; i++) {
            	out.write("defeated\n");
            	out.write(Boolean.toString(defeatedRocks[i]));
            	out.write("\n");
            }
            
            out.write("picklevel\n");
            out.write(Integer.toString(pick.level));
            out.write("\n");
            
            out.write("pickdamage\n");
            out.write(Long.toString(pick.damage));
            out.write("\n");
            
            out.write("picknextdamage\n");
            out.write(Long.toString(pick.nextDamage));
            out.write("\n");
            
            out.write("uaplevel\n");
            out.write(Integer.toString(armorPierce.level));
            out.write("\n");
            
            out.write("ummlevel\n");
            out.write(Integer.toString(mineralMerchant.level));
            out.write("\n");
            
            out.write("ulhlevel\n");
            out.write(Integer.toString(luckyHit.level));
            out.write("\n");
            
            out.write("numfamiliars\n");
            out.write(Integer.toString(addFamiliar.level));
            out.write("\n");
            
            out.write("famdamagelevel\n");
            out.write(Integer.toString(familiarDamage.level));
            out.write("\n");
            
            out.write("famdamage\n");
            out.write(Integer.toString(familiarDamage.damage));
            out.write("\n");
            
            out.write("famnextdamage\n");
            out.write(Integer.toString(familiarDamage.nextDamage));
            out.write("\n");
            
            out.write("famspeedlevel\n");
            out.write(Integer.toString(familiarSpeed.level));
            out.write("\n");
            
            out.write("famspeed\n");
            out.write(Float.toString(familiarSpeed.speed));
            out.write("\n");
            
            out.write("famnextspeed\n");
            out.write(Float.toString(familiarSpeed.nextSpeed));
            out.write("\n");
            
            out.write("magnetfamlevel\n");
            out.write(Integer.toString(magneticFamiliars.level));
            out.write("\n");
            
            out.write("magnetfamgps\n");
            out.write(Float.toString(magneticFamiliars.goldPerSecond));
            out.write("\n");
            
            out.write("magnetfamnextgps\n");
            out.write(Float.toString(magneticFamiliars.nextGoldPerSecond));
            out.write("\n");
            
            out.write("uaclevel\n");
            out.write(Integer.toString(armorChip.level));
            out.write("\n");
            
            out.write("uacchip\n");
            out.write(Long.toString(armorChip.chip));
            out.write("\n");
            
            out.write("uacnextchip\n");
            out.write(Long.toString(armorChip.nextChip));
            out.write("\n");
            
            Log.d("Wallet", "Save completed.");
        } catch (IOException e) {
        	Log.d("Wallet", "Error, could not save file." + e.getMessage());
        } finally {
            try {
                if (out != null)
                    out.close();
            } catch (IOException e) {
            }
        }
    }
    
    public static void load(FileIO files) {
    	String command = null;
    	int rockNumber = 0;
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(files.readFile(".mm")));
            while((command = in.readLine()) != null) {
            	Log.d("Wallet", command);
            	if (command.equals("defeated")) {
            		defeatedRocks[rockNumber] = Boolean.parseBoolean(in.readLine());
            		rockNumber++;
            	} else if (command.equals("currentr"))
            		currentRock = Integer.parseInt(in.readLine());
            	else if (command.equals("currentb"))
            		currentBackground = Integer.parseInt(in.readLine());
            	else if (command.equals("coins"))
            		coins = Long.parseLong(in.readLine());
            	else if (command.equals("picklevel"))
            		pick.load(Integer.parseInt(in.readLine()));
            	else if (command.equals("pickdamage"))
            		pick.damage = Long.parseLong(in.readLine());
            	else if (command.equals("picknextdamage"))
            		pick.nextDamage = Long.parseLong(in.readLine());
            	else if (command.equals("uaplevel"))
            		armorPierce.load(Integer.parseInt(in.readLine()));
            	else if (command.equals("ummlevel"))
            		mineralMerchant.load(Integer.parseInt(in.readLine()));
            	else if (command.equals("lastplayedversion"))
            		lastPlayedVersion = Integer.parseInt(in.readLine());
            	else if (command.equals("ulhlevel"))
            		luckyHit.load(Integer.parseInt(in.readLine()));
            	else if (command.equals("numfamiliars"))
            		addFamiliar.load(Integer.parseInt(in.readLine()));
            	else if (command.equals("famdamagelevel"))
            		familiarDamage.load(Integer.parseInt(in.readLine()));
            	else if (command.equals("famdamage"))
            		familiarDamage.damage = Integer.parseInt(in.readLine());
            	else if (command.equals("famnextdamage"))
            		familiarDamage.nextDamage = Integer.parseInt(in.readLine());
            	else if (command.equals("famspeedlevel"))
            		familiarSpeed.load(Integer.parseInt(in.readLine()));
            	else if (command.equals("famspeed"))
            		familiarSpeed.speed = Float.parseFloat(in.readLine());
            	else if (command.equals("famnextspeed"))
            		familiarSpeed.nextSpeed = Float.parseFloat(in.readLine());
            	else if (command.equals("magnetfamlevel"))
            		magneticFamiliars.load(Integer.parseInt(in.readLine()));
            	else if (command.equals("magnetfamgps"))
            		magneticFamiliars.goldPerSecond = Float.parseFloat(in.readLine());
            	else if (command.equals("magnetfamnextgps"))
            		magneticFamiliars.nextGoldPerSecond = Float.parseFloat(in.readLine());
            	else if (command.equals("uaclevel"))
            		armorChip.load(Integer.parseInt(in.readLine()));
            	else if (command.equals("uacchip"))
            		armorChip.chip = Long.parseLong(in.readLine());
            	else if (command.equals("uacnextchip"))
            		armorChip.nextChip = Long.parseLong(in.readLine());
            	else
            		Log.d("Wallet", "Error, unknown command " + command);
            }
        } catch (IOException e) {
        	Log.e("Wallet", "Error, could not load file. " + e.getMessage());
        } catch (NumberFormatException e) {
        	Log.e("Wallet", "Error, could not convert string." + e.getMessage());
        } catch (Exception e) {
        	e.printStackTrace();
        } finally {
            try {
                if (in != null)
                    in.close();
            } catch (IOException e) {
            }
        }
    }
    
    public static void deleteAndStartOver(FileIO files) {
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(files.writeFile(".mm")));
            out.write("lastplayedversion\n");
            out.write(Integer.toString(-1));
            out.write("\n");
            
            out.write("currentr\n");
            out.write(Integer.toString(0));
            out.write("\n");
            
            out.write("currentb\n");
            out.write(Integer.toString(0));
            out.write("\n");
            
            out.write("coins\n");
            out.write(Long.toString(0));
            out.write("\n");
            
            for (int i = 0; i < MinerScreen.NUMBER_OF_ROCKS; i++) {
            	out.write("defeated\n");
            	out.write(Boolean.toString(false));
            	out.write("\n");
            }
            
            out.write("picklevel\n");
            out.write(Integer.toString(0));
            out.write("\n");
            
            out.write("pickdamage\n");
            out.write(Long.toString(2));
            out.write("\n");
            
            out.write("picknextdamage\n");
            out.write(Long.toString(4));
            out.write("\n");
            
            out.write("uaplevel\n");
            out.write(Integer.toString(0));
            out.write("\n");
            
            out.write("ummlevel\n");
            out.write(Integer.toString(0));
            out.write("\n");
            
            out.write("ulhlevel\n");
            out.write(Integer.toString(0));
            out.write("\n");
            
            out.write("numfamiliars\n");
            out.write(Integer.toString(0));
            out.write("\n");
            
            out.write("famdamagelevel\n");
            out.write(Integer.toString(0));
            out.write("\n");
            
            out.write("famdamage\n");
            out.write(Integer.toString(1));
            out.write("\n");
            
            out.write("famnextdamage\n");
            out.write(Integer.toString(3));
            out.write("\n");
            
            out.write("famspeedlevel\n");
            out.write(Integer.toString(0));
            out.write("\n");
            
            out.write("famspeed\n");
            out.write(Float.toString(5.0f));
            out.write("\n");
            
            out.write("famnextspeed\n");
            out.write(Float.toString(4.8f));
            out.write("\n");
            
            out.write("magnetfamlevel\n");
            out.write(Integer.toString(0));
            out.write("\n");
            
            out.write("magnetfamgps\n");
            out.write(Float.toString(0.0f));
            out.write("\n");
            
            out.write("magnetfamnextgps\n");
            out.write(Float.toString(50f));
            out.write("\n");
            
            out.write("uaclevel\n");
            out.write(Integer.toString(0));
            out.write("\n");
            
            out.write("uacchip\n");
            out.write(Long.toString(0));
            out.write("\n");
            
            out.write("uacnextchip\n");
            out.write(Long.toString(2));
            out.write("\n");
            
            Log.d("Wallet", "Delete completed.");
        } catch (IOException e) {
        	Log.d("Wallet", "Error, could not save file." + e.getMessage());
        } finally {
            try {
                if (out != null)
                    out.close();
            } catch (IOException e) {
            }
            setUpWallet(files);
        }
    }
}
