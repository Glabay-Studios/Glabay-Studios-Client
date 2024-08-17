package net.runelite.client.plugins.hd.utils;

@FunctionalInterface
public interface VariableSupplier {
	Object get(String variableName);
}
