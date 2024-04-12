package com.displaynamedisguiser;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("displaynamedisguiser")
public interface DisplayNameDisguiserConfig extends Config
{
	@ConfigItem(
			keyName = "selfNames",
			name = "Display Names for Self",
			description = "Set the list of possible display names to choose from. Separate each name with a comma (,)",
			position = 0
	)
	default String selfNameList()
	{
		return "Partake,Brock,Salty";
	}
	@ConfigItem(
			keyName = "selfToggle",
			name = "Change Name for Self?",
			description = "Toggle changing the display name for Yourself.",
			position = 1
	)
	default boolean changeSelf()
	{
		return true;
	}
	@ConfigItem(
			keyName = "othersNames",
			name = "Display Names for Others",
			description = "Set the display name for another player. The format is OldName:NewName with each person per line.",
			position = 2
	)
	default String otherNameList()
	{
		return "Old Name:New Name" + "\n" + "Zezima:Not Zezima" + "\n" + "xXDragonSlay3rXx:Dragon Slayer" + "\n" + "TurboWet:Charlie";
	}
	@ConfigItem(
			keyName = "othersToggle",
			name = "Change Name for Others?",
			description = "Toggle changing the display name for Other Players.",
			position = 3
	)
	default boolean changeOthers()
	{
		return false;
	}
	@ConfigItem(
			keyName = "obfuscationToggle",
			name = "Secret Mode? (Copies to Clipboard)",
			description = "Toggle obfuscating display name changes for Others. Paste your clipboard in the Display Names for Others textbox.",
			position = 4
	)
	default boolean obfuscateOthers()
	{
		return false;
	}
	@ConfigItem(
			keyName = "obfuscationKey",
			name = "Secret Key",
			secret = true,
			description = "The secret obfuscation key, do not touch.",
			position = 5
	)
	default String obfuscationKey()
	{
		return "h9TIlznNraYkB2vEDd6Apj4Wx8bSJ3P7OyqKQfsZM1GCouXUiRVLH5mt0Fwceg";
	}
	@ConfigItem(
			keyName = "generateKey",
			name = "Click to Generate Key to Clipboard ->",
			description = "Generate a new obfuscation key to clipboard. Paste into Secret Key textbox before enabling Secret Mode.",
			position = 6
	)
	default boolean generateKey()
	{
		return false;
	}
	@ConfigItem(
			name = "Hide in widgets (Lag warning)",
			keyName = "hideWidgets",
			description = "Hides your Display Name everywhere. Might lag your game.",
			position = 7
	)
	default boolean hideWidgets()
	{
		return false;
	}
}