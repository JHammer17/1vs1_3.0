package de.onevsone.enums;

/**
 * Der Code ist von JHammer
 *
 * 11.05.2016 um 21:10:00 Uhr
 */
public enum KitPrefs {
 
	
	
	NOBUILD("Nicht Bauen"),
	HUNGER("Hunger"),
	INSTANTTNT("InstantTNT"),
	NOTNTDMG("Kein Explosions-Block-Schaden"),
	NOCRAFTING("Kein Crafting"),
	SOUPHEAL("Suppenheilung"),
	NOFALLDMG("Kein Fallschaden"),
	NOARROWPICKUP("Keine Pfeile aufheben"),
	SOUPNOOB("Soup-Noob"),
	DOUBLEJUMP("Doppelsprung"),
	NOHITDELAY("Kein Hit-Delay"),
	NOREGENERATION("Keine natürliche Regeneration"),
	NOITEMDROPS("Keine Item Drops"),
	NOFRIENDLYFIRE("Kein Friendly Fire"),
	WATERDMG("Schaden im Wasser");
	
	public static String getName(KitPrefs pref) {
		if(pref == KitPrefs.NOBUILD) return KitPrefs.NOBUILD.getName();
		if(pref == KitPrefs.HUNGER) return KitPrefs.HUNGER.getName();
		if(pref == KitPrefs.INSTANTTNT) return KitPrefs.INSTANTTNT.getName();
		if(pref == KitPrefs.NOTNTDMG) return KitPrefs.NOTNTDMG.getName();
		if(pref == KitPrefs.NOCRAFTING) return KitPrefs.NOCRAFTING.getName();
		if(pref == KitPrefs.SOUPHEAL) return KitPrefs.SOUPHEAL.getName();
		if(pref == KitPrefs.NOFALLDMG) return KitPrefs.NOFALLDMG.getName();
		if(pref == KitPrefs.NOARROWPICKUP) return KitPrefs.NOARROWPICKUP.getName();
		if(pref == KitPrefs.SOUPNOOB) return KitPrefs.SOUPNOOB.getName();
		if(pref == KitPrefs.DOUBLEJUMP) return KitPrefs.DOUBLEJUMP.getName();
		if(pref == KitPrefs.NOHITDELAY) return KitPrefs.NOHITDELAY.getName();
		if(pref == KitPrefs.NOREGENERATION) return KitPrefs.NOREGENERATION.getName();
		if(pref == KitPrefs.NOITEMDROPS) return KitPrefs.NOITEMDROPS.getName();
		if(pref == KitPrefs.NOFRIENDLYFIRE) return KitPrefs.NOFRIENDLYFIRE.getName();
		if(pref == KitPrefs.WATERDMG) return KitPrefs.WATERDMG.getName();
		
		return KitPrefs.NOBUILD.getName();
	}
	
	private String name;

	KitPrefs(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	
	
	public static KitPrefs getPrefByID(int id) {
		if(id == 0) return KitPrefs.NOBUILD;
		if(id == 1) return KitPrefs.HUNGER;
		if(id == 2) return KitPrefs.NOFALLDMG;
		if(id == 3) return KitPrefs.NOARROWPICKUP;
		if(id == 4) return KitPrefs.INSTANTTNT;
		if(id == 5) return KitPrefs.NOTNTDMG;
		if(id == 6) return KitPrefs.SOUPNOOB;
		if(id == 7) return KitPrefs.NOHITDELAY;
		if(id == 8) return KitPrefs.NOCRAFTING;
		if(id == 9) return KitPrefs.SOUPHEAL;
		if(id == 10) return KitPrefs.NOREGENERATION;
		if(id == 11) return KitPrefs.DOUBLEJUMP;
		if(id == 12) return KitPrefs.NOITEMDROPS;
		if(id == 13) return KitPrefs.NOFRIENDLYFIRE;
		if(id == 14) return KitPrefs.WATERDMG;
		
		return KitPrefs.NOBUILD;
	}
	
}
