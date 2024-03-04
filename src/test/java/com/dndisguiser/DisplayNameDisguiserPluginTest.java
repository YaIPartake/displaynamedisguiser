package net.runelite.client.plugins.displaynamedisguiser.src.test.java.com.dndisguiser;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;
import net.runelite.client.plugins.displaynamedisguiser.src.main.java.com.dndisguiser.DisplayNameDisguiserPlugin;

public class DisplayNameDisguiserPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(DisplayNameDisguiserPlugin.class);
		RuneLite.main(args);
	}
}