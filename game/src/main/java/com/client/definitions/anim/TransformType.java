package com.client.definitions.anim;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.Arrays;

@AllArgsConstructor
@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public enum TransformType {
    ORIGIN(0), TRANSLATION(1), ROTATION(2), SCALE(3), ALPHA(5);
    int value;

    private static final TransformType[] VALUES = values();

    public static TransformType getByValue(int value) {
        return Arrays.stream(VALUES).filter(t -> t.getValue() == value).findFirst().orElse(null);
    }
}