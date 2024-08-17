package com.client.script;

import org.reflections8.Reflections;
import org.reflections8.scanners.SubTypesScanner;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientScripts {

    private static final Map<Integer, ClientScript> scripts = new HashMap<>();

    public static void load() {
        Reflections reflections = new Reflections("com.client.script.impl", new SubTypesScanner(false));
        Set<Class<?>> classes = reflections.getSubTypesOf(Object.class);

        Pattern pattern = Pattern.compile("_(\\d+)_", Pattern.CASE_INSENSITIVE);

        classes.forEach(clazz -> {
            Matcher matcher = pattern.matcher(clazz.getName());
            if (!matcher.find()) {
                System.err.println("Couldn't load class in com.client.script.impl because the id couldn't be derived:" + clazz.getName());
                return;
            }

            try {
                int scriptId = Integer.parseInt(matcher.group(1));
                Object instance = clazz.getConstructors()[0].newInstance();

                for (Method method : clazz.getDeclaredMethods()) {
                    if (method.getName().equalsIgnoreCase("handle")) {
                        scripts.put(scriptId, new ClientScript(scriptId, instance, method));
                        return;
                    }
                }

                throw new IllegalStateException("No handle method for class: " + clazz);
            } catch (Exception e) {
                System.err.println("Error parsing script" + clazz.getName());
                e.printStackTrace();
            }
        });

        System.out.println("Loaded " + scripts.size() + " client scripts.");
    }

    public static void execute(int id, Object[] arguments) {
        if (!scripts.containsKey(id)) {
            System.err.println("No client script with id: " + id);
            return;
        }

        try {
            ClientScript script = scripts.get(id);
            script.getMethod().invoke(script.getInstance(), arguments);
        } catch (IllegalAccessException | InvocationTargetException e) {
            System.err.println("Error invoking client script: " + id);
            e.printStackTrace();
        }
    }

}
