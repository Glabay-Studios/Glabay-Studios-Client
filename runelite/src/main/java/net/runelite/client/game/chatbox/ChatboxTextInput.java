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
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.input.KeyListener;

import java.awt.event.KeyEvent;
import java.util.function.Consumer;
import java.util.function.Predicate;

@Slf4j
public class ChatboxTextInput extends ChatboxInput implements KeyListener
{
	private final ChatboxPanelManager chatboxPanelManager;
	protected final ClientThread clientThread;

	@Getter
	private String prompt;

	@Getter
	private int lines;

	@Inject
	private Client client;

	private String value;

	@Getter
	private Runnable onClose = null;

	@Getter
	private Predicate<String> onDone = null;

	@Getter
	private Consumer<String> onChanged = null;

	@Inject
	protected ChatboxTextInput(ChatboxPanelManager chatboxPanelManager, ClientThread clientThread)
	{
		this.chatboxPanelManager = chatboxPanelManager;
		this.clientThread = clientThread;
	}

	public ChatboxTextInput lines(int lines)
	{
		this.lines = lines;
		return this;
	}

	public ChatboxTextInput prompt(String prompt)
	{
		this.prompt = prompt;
		return this;
	}

	public ChatboxTextInput value(String value)
	{
		this.value = value;
		return this;
	}

	public String getValue()
	{
		return value;
	}

	public ChatboxTextInput onClose(Runnable onClose)
	{
		this.onClose = onClose;
		return this;
	}

	public ChatboxTextInput onDone(Consumer<String> onDone)
	{
		this.onDone = (s) ->
		{
			onDone.accept(s);
			return true;
		};
		return this;
	}

	/**
	 * Called when the user attempts to close the input by pressing enter
	 * Return false to cancel the close
	 */
	public ChatboxTextInput onDone(Predicate<String> onDone)
	{
		this.onDone = onDone;
		return this;
	}

	public ChatboxTextInput onChanged(Consumer<String> onChanged)
	{
		this.onChanged = onChanged;
		return this;
	}


	@Override
	protected void open()
	{
	}

	@Override
	protected void close()
	{
		if (this.onClose != null)
		{
			this.onClose.run();
		}
	}

	public ChatboxTextInput build()
	{
		if (prompt == null)
		{
			throw new IllegalStateException("prompt must be non-null");
		}

		chatboxPanelManager.openInput(this);
		client.setDialogState(prompt, 10, value);

		return this;
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent ev)
	{
		switch(ev.getKeyCode()) {
			case KeyEvent.VK_ENTER:
				value = client.getInputValue();
				if (onDone != null && !onDone.test(getValue())) {
					return;
				}
				chatboxPanelManager.close();
				return;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}
}