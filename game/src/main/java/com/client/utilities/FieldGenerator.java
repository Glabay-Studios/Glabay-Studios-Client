package com.client.utilities;

import java.util.HashSet;
import java.util.function.Consumer;

import org.apache.commons.lang3.math.NumberUtils;

/**
 * Generates fields from definitions.
 * @author Michael Sasse (https://github.com/mikeysasse/)
 */
public class FieldGenerator {

    private final Consumer<String> consumer;
//    private final HashSet<String> standardSet = new HashSet<>();
//    private final HashSet<String> osrsSet = new HashSet<>();
    private final HashSet<String> fields = new HashSet<>();

    public FieldGenerator(Consumer<String> consumer) {
        this.consumer = consumer;
    }

    public void add(String definitionName, int id) {
        if (definitionName != null && definitionName.length() > 0
                && !definitionName.equalsIgnoreCase("null")
                && !definitionName.contains("col=")
                && !definitionName.equals("Dwarf remains")
                && !definitionName.contains("shad=")
                && !definitionName.contains("@")) {
            String name = definitionName.toUpperCase().replaceAll(" ", "_");
            name = name.replaceAll("\\(", "");
            name = name.replaceAll("\\)", "");
            name = name.replaceAll("'", "");
            name = name.replaceAll("-", "_");
            name = name.replaceAll("\\.", "");
            name = name.replaceAll(",", "");
            name = name.replaceAll("!", "");
            name = name.replaceAll("\\�", "");
            name = name.replaceAll("\\?", "");
            name = name.replaceAll("%", "");
            name = name.replaceAll(":", "");
            name = name.replaceAll("\"", "");
            name = name.replaceAll("&", "AND");
            name = name.replaceAll("1/2", "HALF");
            name = name.replaceAll("2/3", "TWO_THIRDS");
            name = name.replaceAll("1/3", "ONE_THIRD");
            name = name.replaceAll("1/5", "ONE_FIFTH");
            name = name.replaceAll("2/5", "TWO_FIFTHS");
            name = name.replaceAll("3/5", "THREE_FIFTHS");
            name = name.replaceAll("4/5", "FOUR_FIFTHS");
            name = name.replaceAll("5/5", "FIVE_FIFTHS");
            name = name.replaceAll("3RD", "THIRD");
            name = name.replaceAll("4TH", "FOURTH");
            name = name.replaceAll("/", "_OF_");
            name = name.replaceAll("\\+", "PLUS");


            name = name.replaceAll("__", "_");
            if (NumberUtils.isNumber(name.substring(0, 1))) {
                name = "_" + name;
            }
//            if (osrs && standardSet.contains(name)) {
//                return;
//            }
//
//            HashSet<String> set = osrs ? osrsSet : standardSet;
//            set.add(name);

            int i = 2;
            String field = name;
            while (fields.contains(field)) {
                field = name + "_" + i++;
            }

            fields.add(field);
            String generated = "public static final int "
                    + field + " = "
                    + Misc.getUnderscoredNumber(id)
                    + ";";
            consumer.accept(generated);
        }
    }


}
