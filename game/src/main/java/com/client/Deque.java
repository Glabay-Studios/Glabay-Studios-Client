package com.client;
// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)

import net.runelite.rs.api.RSNode;
import net.runelite.rs.api.RSNodeDeque;

import java.util.Iterator;

public final class Deque implements RSNodeDeque {

	public Deque() {
		head = new Linkable();
		head.prev = head;
		head.next = head;
	}

	public void insertHead(Linkable node) {
		if (node.next != null)
			node.unlink();
		node.next = head.next;
		node.prev = head;
		node.next.prev = node;
		node.prev.next = node;
	}

	public void insertTail(Linkable node) {
		if (node.next != null)
			node.unlink();
		node.next = head;
		node.prev = head.prev;
		node.next.prev = node;
		node.prev.next = node;
	}

	public final void method894(Linkable class3, Linkable class3_27_) {
		if (class3.prev != null) {
			class3.unlink();
		}

		class3.next = class3_27_;
		class3.prev = class3_27_.prev;
		class3.prev.next = class3;
		class3.next.prev = class3;
	}


	public Linkable popHead() {
		Linkable node = head.prev;
		if (node == head) {
			return null;
		} else {
			node.unlink();
			return node;
		}
	}

	public Linkable reverseGetFirst() {
		Linkable node = head.prev;
		if (node == head) {
			current = null;
			return null;
		} else {
			current = node.prev;
			return node;
		}
	}

	public Linkable getFirst() {
		Linkable node = head.next;
		if (node == head) {
			current = null;
			return null;
		} else {
			current = node.next;
			return node;
		}
	}

	public Linkable reverseGetNext() {
		Linkable node = current;
		if (node == head) {
			current = null;
			return null;
		} else {
			current = node.prev;
			return node;
		}
	}

	public Linkable getNext() {
		Linkable node = current;
		if (node == head) {
			current = null;
			return null;
		}
		current = node.next;
		return node;
	}

	public void removeAll() {
		if (head.prev == head)
			return;
		do {
			Linkable node = head.prev;
			if (node == head)
				return;
			node.unlink();
		} while (true);
	}

	public final Linkable head;
	private Linkable current;

	@Override
	public Iterator iterator() {
		return null;
	}

	@Override
	public RSNode getCurrent() {
		return current;
	}

	@Override
	public RSNode getSentinel() {
		return head;
	}

	@Override
	public RSNode last() {
		return getFirst();
	}

	@Override
	public RSNode previous() {
		return reverseGetFirst();
	}

	@Override
	public void addFirst(RSNode val) {
		insertHead((Linkable) val);
	}

	@Override
	public RSNode removeLast() {
		return reverseGetNext();
	}

	@Override
	public void clear() {
		removeAll();
	}

}
