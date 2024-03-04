package net.runelite.client.plugins.displaynamedisguiser.src.main.java.com.dndisguiser;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("dndisguiser")
public interface DisplayNameDisguiserConfig extends Config
{
	@ConfigItem(
			keyName = "List of Display Names",
			name = "Display Names for Self",
			description = "Set the list of possible display names to choose from. Separate each name with a comma (,)",
			position = 0
	)
	default String selfNameList()
	{
		return "Partake,Brock,Salty";
	}
	@ConfigItem(
			keyName = "Change the Username for Yourself",
			name = "Change Name for Self?",
			description = "Toggle changing the display name for Yourself.",
			position = 1
	)
	default boolean changeSelf()
	{
		return true;
	}
	@ConfigItem(
			keyName = "List of Other Name Changes",
			name = "Display Names for Others",
			description = "Set the display name for another player. The format is OldName:NewName with each person per line.",
			position = 2
	)
	default String otherNameList()
	{
		return "Old Name:New Name" + "\n" + "Ya I Partake:Partake" + "\n" + "Brock Star:Brock" + "\n" + "Kiing Salty:Salty";
	}
	@ConfigItem(
			keyName = "Change the Username for Others",
			name = "Change Name for Others?",
			description = "Toggle changing the display name for Other Players.",
			position = 3
	)
	default boolean changeOthers()
	{
		return false;
	}
	@ConfigItem(
			keyName = "Obfuscate the Display Name Change for Others",
			name = "Secret Mode?",
			description = "Toggle obfuscating display name changes for Others.",
			position = 4
	)
	default boolean obfuscateOthers()
	{
		return false;
	}
	@ConfigItem(
			keyName = "Obfuscation Key",
			name = "Secret Key",
			description = "The secret obfuscation key, do not touch.",
			position = 5
	)
	default String obfuscationKey()
	{
		return "h9TIlznNraYkB2vEDd6Apj4Wx8bSJ3P7OyqKQfsZM1GCouXUiRVLH5mt0Fwceg";
	}
	@ConfigItem(
			name = "Hide in widgets (Lag warning)",
			keyName = "hideWidgets",
			description = "Hides your Display Name everywhere. Might lag your game.",
			position = 6
	)
	default boolean hideWidgets()
	{
		return false;
	}
}