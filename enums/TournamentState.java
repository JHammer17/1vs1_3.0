/**
 * 
 */
package de.onevsone.enums;

/**
 * ######################################################
 * # @author JHammer17								    #
 * # Erstellt am 31.03.2018 17:58:14					#
 * #												   	#
 * # Alle Ihhalte dieser Klasse dürfen					#
 * # frei verwendet oder verbreitet werden.				#
 * # Es wird keine Zustimmung von JHammer17 benötigt.	#
 * #													#
 * ######################################################
*/
public enum TournamentState {

	WAITING("Warten"),
	STARTING("Starte"),
	QUALIFICATIONPHASE("Quallifikation"),
	KOPHASE("K.O.-Phase");
	
	
	private String name;

	TournamentState(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	
}
