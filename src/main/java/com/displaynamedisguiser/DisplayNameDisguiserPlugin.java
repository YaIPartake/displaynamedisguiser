/*
 * Copyright (c) 2020, ThatGamerBlue <thatgamerblue@gmail.com>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.displaynamedisguiser;

import com.google.inject.Provides;
import javax.inject.Inject;

import net.runelite.api.*;
import net.runelite.api.events.*;
import net.runelite.api.widgets.Widget;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.util.ImageUtil;
import net.runelite.client.util.Text;
import net.runelite.client.game.ChatIconManager;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import java.awt.datatransfer.StringSelection;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;

import static net.runelite.api.MenuAction.*;

@PluginDescriptor(
		name = "Display Name Disguiser",
		description = "Disguise your display name. For content creators.",
		tags = {"disguise", "fake", "display", "username", "user", "name", "rsn"}
)
public class DisplayNameDisguiserPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private ClientThread clientThread;

	@Inject
	private DisplayNameDisguiserConfig config;

	@Inject
	private ChatIconManager chatIconManager;

	private String fakeRsn;
	public String[] NAMES;
	private String[] otherPlayers;
	private String obfuscationKey;
	private int iconId = -1;
	private static final String ALPHA_NUMERIC_STRING = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
	@Provides
	public DisplayNameDisguiserConfig getConfig(ConfigManager configManager)
	{
		return configManager.getConfig(DisplayNameDisguiserConfig.class);
	}


	@Override
	public void startUp() throws Exception
	{
		NAMES = config.selfNameList().split(",");
		if (config.changeSelf()) {
			Random generator = new Random();
			int randomIndex = generator.nextInt(NAMES.length);
			fakeRsn = NAMES[randomIndex];
		}
		otherPlayers = config.otherNameList().split("\n");
		obfuscationKey = config.obfuscationKey();
		//Load Icon
		clientThread.invoke(() ->
		{
			if (client.getModIcons() == null)
			{
				return false;
			}
			loadDisguiseIcon();
			return true;
		});
		/*
		Pastes an encrypted Name List in the dev console.
		Use this output in the OtherPlayers Name List config
		Must have Change Others & Obfuscate Others option on.
		Must not change the Secret Key.
		Used to create the config necessary to make the
		Obfuscate Others option functional.


		for (int i = 0; i < otherPlayers.length; i++)
		{
			System.out.println(encrypt(otherPlayers[i]));
		}
		*/
	}

	@Override
	public void shutDown()
	{
		clientThread.invokeLater(() -> client.runScript(ScriptID.CHAT_PROMPT_INIT));
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged event)
	{
		/*
		Adds an encrypted version of the Other Player's Name List in the clipboard
		 */
		if(event.getKey().equals("obfuscationToggle"))
		{
			String myString = encrypt(config.otherNameList());
			StringSelection stringSelection = new StringSelection(myString);
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			clipboard.setContents(stringSelection, null);
		}
		/*
		Generates a new Obfuscation key and puts it in the user's clipboard.
		 */
		if(event.getKey().equals("generateKey"))
		{
			String myString = generateKey();
			StringSelection stringSelection = new StringSelection(myString);
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			clipboard.setContents(stringSelection, null);
		}
		if(event.getKey().equals("obfuscationKey"))
		{
			obfuscationKey = event.getNewValue();
		}
	}
	@Subscribe
	public void onScriptCallbackEvent(ScriptCallbackEvent event)
	{
		if (!config.changeOthers())
		{
			return;
		}
		/*
		Changes another player's username in their Friend's List.
		 */
		if (event.getEventName().equalsIgnoreCase("friendsChatSetText")) {
			String[] stringStack = client.getStringStack();
			int stringStackSize = client.getStringStackSize();
			String rsn = stringStack[stringStackSize - 1];
			String sanitized = Text.toJagexName(Text.removeTags(rsn));
			if (config.changeOthers())
			{
				if (config.obfuscateOthers())
				{
					for (String otherPlayer : otherPlayers)
					{
						String oldName = otherPlayer.split(":")[0];
						String newName = otherPlayer.split(":")[1];
						if (sanitized.equalsIgnoreCase(decrypt(oldName))) {
							stringStack[stringStackSize - 1] = decrypt(newName);
						}
					}
				} else {
					for (String otherPlayer : otherPlayers)
					{
						String oldName = otherPlayer.split(":")[0];
						String newName = otherPlayer.split(":")[1];
						if (sanitized.equalsIgnoreCase(oldName)) {
							stringStack[stringStackSize - 1] = "<img=" + iconId + ">" + newName;
						}
					}
				}
			}
		}
	}

	@Subscribe
	private void onBeforeRender(BeforeRender event)
	{
		if (client.getGameState() != GameState.LOGGED_IN)
		{
			return;
		}

		if (config.hideWidgets())
		{
			// do every widget
			for (Widget widgetRoot : client.getWidgetRoots())
			{
				processWidget(widgetRoot);
			}
		}
		else
		{
			// just do the chatbox
			updateChatbox();
		}
	}

	/**
	 * Recursively traverses widgets looking for text containing the players name, replacing it if necessary
	 * @param widget The root widget to process
	 */

	private void processWidget(Widget widget)
	{
		if (widget == null)
		{
			return;
		}

		if (widget.getText() != null)
		{
			widget.setText(replaceRsn(widget.getText()));
		}

		for (String otherPlayer : otherPlayers)
		{
			if (config.changeSelf() && widget.getName().contains(Text.standardize(otherPlayer))) {
				String oldName = Text.standardize(otherPlayer.split(":")[0]);
				String newName = Text.standardize(otherPlayer.split(":")[1]);

				widget.setName(widget.getName().replace(oldName, newName));
			}
		}

		for (Widget child : widget.getStaticChildren())
		{
			processWidget(child);
		}

		for (Widget dynamicChild : widget.getDynamicChildren())
		{
			processWidget(dynamicChild);
		}

		for (Widget nestedChild : widget.getNestedChildren())
		{
			processWidget(nestedChild);
		}
	}

	private void updateChatbox()
	{
		Widget chatboxTypedText = client.getWidget(162,55);
		if (chatboxTypedText == null || chatboxTypedText.isHidden())
		{
			return;
		}
		//noinspection ConstantConditions
		//changes the player's username in the chatbox
		if (config.changeSelf())
		{
			String[] chatbox = chatboxTypedText.getText().split(":", 2);
			chatbox[0] = fakeRsn;
			chatboxTypedText.setText(chatbox[0] + ":" + chatbox[1]);
		}
	}

	@Subscribe
	private void onChatMessage(ChatMessage event)
	{
		//noinspection ConstantConditions
		if (client.getLocalPlayer().getName() == null)
		{
			return;
		}

		if (event.getName() == null)
		{
			return;
		}

		boolean isLocalPlayer =
				Text.standardize(event.getName()).equalsIgnoreCase(Text.standardize(client.getLocalPlayer().getName()));

		String replaced = replaceRsn(event.getMessage());
		event.setMessage(replaced);
		event.getMessageNode().setValue(replaced);

		//changes the player's or another player's username for sent messages in the chatbox.
		if (isLocalPlayer)
		{
			if (config.changeSelf())
			{
				event.setName(fakeRsn);
				event.getMessageNode().setName(fakeRsn);
			}
		}
		if (!isLocalPlayer)
		{
			if (config.changeOthers())
			{
				changeOthers(event);
				changeTrades(event);
			}
		}
	}

	@Subscribe
	private void onOverheadTextChanged(OverheadTextChanged event)
	{
		event.getActor().setOverheadText(replaceRsn(event.getOverheadText()));
	}

	/*
	Changes another player's username in the right-click menu
	 */
	@Subscribe(priority = -3)
	public void onClientTick(ClientTick clientTick) {
		if (client.isMenuOpen()) {
			return;
		}

		MenuEntry[] menuEntries = client.getMenuEntries();

		for (MenuEntry entry : menuEntries) {
			MenuAction type = entry.getType();

			if (type == WALK
					|| type == WIDGET_TARGET_ON_PLAYER
					//|| type == ITEM_USE_ON_PLAYER
					|| type == PLAYER_FIRST_OPTION
					|| type == PLAYER_SECOND_OPTION
					|| type == PLAYER_THIRD_OPTION
					|| type == PLAYER_FOURTH_OPTION
					|| type == PLAYER_FIFTH_OPTION
					|| type == PLAYER_SIXTH_OPTION
					|| type == PLAYER_SEVENTH_OPTION
					|| type == PLAYER_EIGHTH_OPTION
					|| type == RUNELITE_PLAYER) {
				Player[] players = client.getCachedPlayers();
				Player player = null;

				int identifier = entry.getIdentifier();

				// 'Walk here' identifiers are offset by 1 because the default
				// identifier for this option is 0, which is also a player index.
				if (type == WALK) {
					identifier--;
				}

				if (identifier >= 0 && identifier < players.length) {
					player = players[identifier];

				}

				if (player == null) {
					return;
				}



				String oldTarget = entry.getTarget();
				String newTarget = renameTarget(oldTarget, player.getName());

				entry.setTarget(newTarget);
			}
		}
	}
	/*
	Helps with changing another player's username in the right-click menu.
	 */
	public String renameTarget(String oldTarget, String playerName)
	{
		otherPlayers = config.otherNameList().split("\n");
		if (config.changeOthers()) {
			String newTarget;
			for (String otherPlayer : otherPlayers)
			{
				String oldName = otherPlayer.split(":", 2)[0];
				String newName = otherPlayer.split(":", 2)[1];
				String standardized = Text.removeTags(playerName);
				if (standardized.equalsIgnoreCase(oldName)) {
					if (standardized.contains(" ")) {
						newTarget = oldTarget;
						String[] oldTemp = standardized.split(" ");
						for (int j = 0; j < oldTemp.length; j++) {
							if (j < 1) {
								newTarget = newTarget.replace(oldTemp[j], "<img=" + iconId + ">" + newName);
							} else {
								newTarget = newTarget.replace(oldTemp[j], "");
								newTarget = newTarget.replace("  ", " ");
							}
						}
					} else {
						newTarget = oldTarget.replace(playerName, "<img=" + iconId + ">" + newName);
					}
					return newTarget;
				}

				if (config.obfuscateOthers()) {
					if (standardized.equalsIgnoreCase(decrypt(oldName))) {
						if (standardized.contains(" ")) {
							newTarget = oldTarget;
							String[] oldTemp = standardized.split(" ");
							for (int j = 0; j < oldTemp.length; j++) {
								if (j < 1) {
									newTarget = newTarget.replace(oldTemp[j], "<img=" + iconId + ">" + decrypt(newName));
								} else {
									newTarget = newTarget.replace(oldTemp[j], "");
									newTarget = newTarget.replace("  ", " ");
								}
							}
						} else {
							newTarget = oldTarget.replace(playerName, "<img=" + iconId + ">" + decrypt(newName));
						}
						return newTarget;
					}
				}
			}
			return oldTarget;
		}
		return oldTarget;
	}
	/*
	Helps replace the player's username in the chatbox.
	 */
	private String replaceRsn(String textIn)
	{
		//noinspection ConstantConditions
		String playerRsn = Text.toJagexName(client.getLocalPlayer().getName());
		String standardized = Text.standardize(playerRsn);
		while (Text.standardize(textIn).contains(standardized))
		{
			int idx = textIn.replace("\u00A0", " ").toLowerCase().indexOf(playerRsn.toLowerCase());
			int length = playerRsn.length();
			String partOne = textIn.substring(0, idx);
			String partTwo = textIn.substring(idx + length);
			textIn = partOne + fakeRsn + partTwo;
		}
		return textIn;
	}
	/*
	Helps change another player's username for sent messages in the chatbox.
	 */
	public void changeOthers(ChatMessage event)
	{
		otherPlayers = config.otherNameList().split("\n");
		for (String otherPlayer : otherPlayers)
		{
			String oldName = otherPlayer.split(":", 2)[0];
			String newName = otherPlayer.split(":", 2)[1];
			if (Text.standardize(event.getName()).equalsIgnoreCase(oldName)) {
				event.setName(newName);
				event.getMessageNode().setName(newName);
			}

			if (config.obfuscateOthers()) {
				if (Text.standardize(event.getName()).equalsIgnoreCase(decrypt(oldName))) {
					event.setName("<img=" + iconId + ">" + decrypt(newName));
					event.getMessageNode().setName("<img=" + iconId + ">" + decrypt(newName));
				}
			}
		}
	}
	/*
	Changes another player's name when they send trade requests.
	Doesn't work for the trade window. Looking into solutions.
	 */
	public void changeTrades(ChatMessage event)
	{
		if (event.getMessage().toLowerCase().contains("trade")
				|| event.getMessage().toLowerCase().contains("clan"))
		{
			if (config.changeOthers()) {
				otherPlayers = config.otherNameList().split("\n");
				for (int i = 0; i < otherPlayers.length; i++)
				{
					String oldName = otherPlayers[i].split(":", 2)[0];
					String newName = otherPlayers[i].split(":", 2)[1];
					if (Text.standardize(event.getMessageNode().getValue().toLowerCase()).contains(oldName.toLowerCase()))
					{
						event.setMessage(event.getMessage().replace(oldName,"<img=" + iconId + ">" + newName));
						event.getMessageNode().setValue(event.getMessageNode().getValue().replace(oldName,"<img=" + iconId + ">" + newName));
					}
				}
			}
			if (config.obfuscateOthers()) {
				otherPlayers = config.otherNameList().split("\n");
				for (int i = 0; i < otherPlayers.length; i++)
				{
					String oldName = otherPlayers[i].split(":", 2)[0];
					String newName = otherPlayers[i].split(":", 2)[1];
					if (Text.standardize(event.getMessageNode().getValue().toLowerCase()).contains(decrypt(oldName).toLowerCase()))
					{
						event.setMessage(event.getMessage().replace(decrypt(oldName),"<img=" + iconId + ">" + decrypt(newName)));
						event.getMessageNode().setValue(event.getMessageNode().getValue().replace(decrypt(oldName),"<img=" + iconId + ">" + decrypt(newName)));
					}
				}
			}
		}
	}
	/*
	Encrypts a name for the Secret Key function to obfuscate usernames.
	Created this for game purposes, so content creators can truly anonymize
	contestants. The encrypt function is mostly for development testing.
	It basically compares 2 strings, an alphabetical numerical string & a
	shuffled alphabetical numerical string, and swaps their place.
	 */
	public String encrypt(String name)
	{
		char oldNameArray[] = name.toCharArray();
		String newName = "";
		char Key[] = obfuscationKey.toCharArray();
		int index;
		for (int i = 0; i < name.length(); i++)
		{
			index = ALPHA_NUMERIC_STRING.indexOf(oldNameArray[i]);
			if (index != -1) {
				newName += Key[index];
			} else {
				newName += oldNameArray[i];
			}
		}
		return newName;
	}
	/*
	Decrypts a name for the Secret Key function to convert obfuscated usernames to plaintext.
	Created this for game purposes, so content creators can truly anonymize
	contestants. Decrypt function allows the Change Others config option to function.
	It basically compares 2 strings, an alphabetical numerical string & a
	shuffled alphabetical numerical string, and swaps their place.
	 */
	public String decrypt(String name)
	{
		char oldNameArray[] = name.toCharArray();
		String newName = "";
		char ANS[] = ALPHA_NUMERIC_STRING.toCharArray();
		int index;
		for (int i = 0; i < name.length(); i++)
		{
			index = obfuscationKey.indexOf(oldNameArray[i]);
			if (index != -1) {
				newName += ANS[index];
			} else {
				newName += oldNameArray[i];
			}
		}
		return newName;
	}

	/*
	Generates a new key by shuffling the Alpha_Numeric string
	 */
	public String generateKey()
	{
		List<String> letters = Arrays.asList(ALPHA_NUMERIC_STRING.split(""));
		Collections.shuffle(letters);
		String shuffled = "";
		for (String letter : letters) {
			shuffled += letter;
		}
		return shuffled;
	}

	/*
	Couldn't figure out the proper way to do this, but I found someone else do it a different way & used that
	 */
	private void loadDisguiseIcon()
	{
		final IndexedSprite[] modIcons = client.getModIcons();

		if (iconId != -1 || modIcons == null)
		{
			return;
		}

		final BufferedImage image = ImageUtil.loadImageResource(getClass(),"/disguiser_icon.png");

		IndexedSprite indexedSprite = ImageUtil.getImageIndexedSprite(image, client);

		iconId = modIcons.length;

		final IndexedSprite[] newModIcons = Arrays.copyOf(modIcons, modIcons.length + 1);
		newModIcons[newModIcons.length - 1] = indexedSprite;

		client.setModIcons(newModIcons);
	}
}