package org.bisqt;

public class SpecVars {
    public static void checkSpecVar(Variable var) {
        switch (var.name) {
            case "$echo":
                System.out.println(var.getVal());
                return;
            case "$mod":
                Biscuit.nextScriptForStart = (String) var.getVal();
                return;
        }
    }

    public static boolean isSpecVar(Variable var) {
        return switch (var.name) {
            case "$echo", "$mod" -> true;
            default -> false;
        };
    }
}
