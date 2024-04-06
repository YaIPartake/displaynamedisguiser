package com.displaynamedisguiser;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class DisplayNameDisguiserPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(DisplayNameDisguiserPlugin.class);
		RuneLite.main(args);
	}
}