/*
 * Copyright (c) 2018 Abex
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
package net.runelite.client.game.chatbox;

import com.google.inject.Inject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import net.runelite.api.Client;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.input.KeyListener;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class ChatboxTextMenuInput extends ChatboxInput implements KeyListener
{
	@Data
	@AllArgsConstructor
	private static final class Entry
	{
		private String text;
		private Runnable callback;
	}

	private final ChatboxPanelManager chatboxPanelManager;

	@Getter
	private String title;

	@Getter
	private List<Entry> options = new ArrayList<>();

	@Getter
	private Runnable onClose;

	@Inject
	private Client client;

	@Inject
	protected ChatboxTextMenuInput(ChatboxPanelManager chatboxPanelManager)
	{
		this.chatboxPanelManager = chatboxPanelManager;
	}

	public ChatboxTextMenuInput title(String title)
	{
		this.title = title;
		return this;
	}

	public ChatboxTextMenuInput option(String text, Runnable callback)
	{
		options.add(new Entry(text, callback));
		return this;
	}

	public ChatboxTextMenuInput onClose(Runnable onClose)
	{
		this.onClose = onClose;
		return this;
	}

	public ChatboxTextMenuInput build()
	{
		if (title == null)
		{
			throw new IllegalStateException("Title must be set");
		}

		if (options.size() < 1)
		{
			throw new IllegalStateException("You must have atleast 1 option");
		}

		chatboxPanelManager.openInput(this);

		// Collect all the options
		String[] stringOptions = new String[options.size()];
		for (int index = 0; index < options.size(); index++) {
			stringOptions[index] = options.get(index).getText();
		}

		// Send the option interface
		client.openChatOption(CHATBOX_INTERFACES[options.size() - 1], 2, title, stringOptions);
		return this;
	}

	public void callback(Entry entry)
	{
		client.closeInterface();
		chatboxPanelManager.close();
		entry.callback.run();
	}

	@Override
	protected void open()
	{

	}

	@Override
	protected void close()
	{
		if (onClose != null)
		{
			onClose.run();
		}
	}


	@Override
	public void keyTyped(KeyEvent e)
	{
		char c = e.getKeyChar();

		if (c == '\033')
		{
			client.closeInterface();
			chatboxPanelManager.close();
			e.consume();
			return;
		}

		int n = c - '1';
		if (n >= 0 && n < options.size())
		{
			callback(options.get(n));
			e.consume();
		}
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
		{
			e.consume();
		}
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
	}

	@Subscribe
	public void onMenuOptionClicked(MenuOptionClicked event)
	{
		if (!((event.getParam1() >= FIRST_DIALOGUE_OPTION_OF_FIVE && event.getParam1() <= FIFTH_DIALOGUE_OPTION_OF_FIVE)
				|| (event.getParam1() >= FIRST_DIALOGUE_OPTION_OF_FOUR && event.getParam1() <= FOURTH_DIALOGUE_OPTION_OF_FOUR)
				|| (event.getParam1() >= FIRST_DIALOGUE_OPTION_OF_THREE && event.getParam1() <= THIRD_DIALOGUE_OPTION_OF_THREE)
				|| (event.getParam1() >= FIRST_DIALOGUE_OPTION_OF_TWO && event.getParam1() <= SECOND_DIALOGUE_OPTION_OF_TWO))) {
			return;
		}

		if (title != null) {
			switch(event.getParam1()) {
				case FIRST_DIALOGUE_OPTION_OF_FIVE:
				case FIRST_DIALOGUE_OPTION_OF_FOUR:
				case FIRST_DIALOGUE_OPTION_OF_THREE:
				case FIRST_DIALOGUE_OPTION_OF_TWO:
					callback(options.get(0));
					break;
				case SECOND_DIALOGUE_OPTION_OF_FIVE:
				case SECOND_DIALOGUE_OPTION_OF_FOUR:
				case SECOND_DIALOGUE_OPTION_OF_THREE:
				case SECOND_DIALOGUE_OPTION_OF_TWO:
					callback(options.get(1));
					break;
				case THIRD_DIALOGUE_OPTION_OF_FIVE:
				case THIRD_DIALOGUE_OPTION_OF_FOUR:
				case THIRD_DIALOGUE_OPTION_OF_THREE:
					callback(options.get(2));
					break;
				case FOURTH_DIALOGUE_OPTION_OF_FIVE:
				case FOURTH_DIALOGUE_OPTION_OF_FOUR:
					callback(options.get(3));
					break;
				case FIFTH_DIALOGUE_OPTION_OF_FIVE:
					callback(options.get(4));
					break;
				default:
					break;
			}
		}
	}

	// Dialogue interfaces
	private static final int[] CHATBOX_INTERFACES = { 13760, 2461, 2471, 2482, 2494, };

	// Dialogues
	private static final int FIRST_DIALOGUE_OPTION_OF_FIVE = 2494;
	private static final int SECOND_DIALOGUE_OPTION_OF_FIVE = 2495;
	private static final int THIRD_DIALOGUE_OPTION_OF_FIVE = 2496;
	private static final int FOURTH_DIALOGUE_OPTION_OF_FIVE = 2497;
	private static final int FIFTH_DIALOGUE_OPTION_OF_FIVE = 2498;
	private static final int FIRST_DIALOGUE_OPTION_OF_FOUR = 2482;
	private static final int SECOND_DIALOGUE_OPTION_OF_FOUR = 2483;
	private static final int THIRD_DIALOGUE_OPTION_OF_FOUR = 2484;
	private static final int FOURTH_DIALOGUE_OPTION_OF_FOUR = 2485;
	private static final int FIRST_DIALOGUE_OPTION_OF_THREE = 2471;
	private static final int SECOND_DIALOGUE_OPTION_OF_THREE = 2472;
	private static final int THIRD_DIALOGUE_OPTION_OF_THREE = 2473;
	private static final int FIRST_DIALOGUE_OPTION_OF_TWO = 2461;
	private static final int SECOND_DIALOGUE_OPTION_OF_TWO = 2462;
}