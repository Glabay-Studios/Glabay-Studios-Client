package com.client.script;

import java.lang.reflect.Method;

public class ClientScript {

    private final int id;
    private final Object instance;
    private final Method method;

    public ClientScript(int id, Object instance, Method method) {
        this.id = id;
        this.instance = instance;
        this.method = method;
    }

    public int getId() {
        return id;
    }

    public Object getInstance() {
        return instance;
    }

    public Method getMethod() {
        return method;
    }
}
