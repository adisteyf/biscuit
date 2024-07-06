package org.bisqt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Biscuit {
    final static ArrayList<Variable> vars = new ArrayList<>();
    public static String nextScriptForStart;

    private static ArrayList<ArrayList<String>> getDivs(ArrayList<String> str) {
        Pattern pattern = Pattern.compile("[^\\s\"']+|\"([^\"]*)\"");
        ArrayList<ArrayList<String>> result = new ArrayList<>();

        for (String s : str) {
            Matcher matcher = pattern.matcher(s);
            ArrayList<String> words = new ArrayList<>();
            while (matcher.find()) {
                words.add(matcher.group());
            }
            result.add(words);
        }

        for (ArrayList<String> strings : result) {
            for (int j = 0; j < strings.size(); j++) {
                if (strings.get(j).startsWith("//")) { // Comment
                    strings.subList(j, strings.size()).clear();
                }
            }
        }

        return result;
    }

    public void readScript(String path) {
        nextScriptForStart=null;
        File file = new File(path);
        ArrayList<String> lines = new ArrayList<>();

        try (FileReader fileReader = new FileReader(file);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            System.err.println("Error during reading file: " + e.getMessage());
        }

        ArrayList<ArrayList<String>> divs = getDivs(lines);
        checkCommands(divs);
        if (nextScriptForStart != null) {
            readScript(nextScriptForStart);
        }
    }

    private static void checkCommands(ArrayList<ArrayList<String>> divs) {
        for (ArrayList<String> line : divs) {
            switch (line.getFirst()) {
                case "set":
                    if (line.size() == 3) {
                        switch (checkType(line.get(2))) {
                            case 0:
                                addVar(new Variable(line.get(1), line.get(2).replace("\"", "")));
                                break;
                            case 1:
                                addVar(new Variable(line.get(1), Integer.parseInt(line.get(2))));
                                break;
                            case 2:
                                addVar(new Variable(line.get(1), Double.parseDouble(line.get(2))));
                                break;
                            case 3:
                                if (Objects.equals(line.get(2), "true")) addVar(new Variable(line.get(1), true));
                                else addVar(new Variable(line.get(1), false));
                            case -1:
                                if (getVar(line.get(2)) != null && getVar(line.get(2)).getVal() != null) {
                                    addVar(new Variable(line.get(1), getVar(line.get(2)).getVal()));
                                } else {
                                    System.err.println("ERROR: checkType(): INCORRECT TYPE OF VAR");
                                    System.exit(-1);
                                }
                                break;
                        }
                    }
                    break;

                case "exit":
                    return;

                case "clearvars":
                    vars.clear();
                    break;

                default:
                    if (line.size() == 2 && getVar(line.getFirst()) != null) {
                        if (checkType(line.get(1)) != -1) {
                            changeVal(line.getFirst(), strToObj(line.get(1)));
                        } else if (getVar(line.get(1)) != null) {
                            changeVal(line.getFirst(), getVar(line.get(1)).getVal());
                        } else {
                            System.err.println("ERROR: '"+line+"'. NOT A STATEMENT");
                            System.exit(-1);
                        }
                    } else {
                        System.err.println("ERROR: '"+line+"'. NOT A STATEMENT");
                        System.exit(-1);
                    }
                    break;
            }
        }
    }

    private static void changeVal(String name, Object val) {
        boolean changed = false;
        for (Variable var : vars) {
            if (Objects.equals(var.name, name)) {
                var.chVal(val);
                SpecVars.checkSpecVar(var);
                changed=true;
            }
        }

        if (!changed) {
            System.err.println("ERROR: set: NO VARIABLE WITH NAME '"+name+"'");
            System.exit(-1);
        }
    }

    private static void addVar(Variable var) {
        for (Variable check : vars) {
            if (Objects.equals(var.name, check.name))
                return;
            if (!var.name.matches("^(?![0-9])[a-zA-Z0-9_]*$") && !SpecVars.isSpecVar(var)) { // ^[^0-9][a-zA-Z0-9_]*$
                System.err.println("ERROR: set: INCORRECT VAR NAME '"+var.name+"'");
                System.exit(-1);
            }
        }
        vars.add(var);
        SpecVars.checkSpecVar(var);
    }

    public static short checkType(String val) { // 0 == str, 1 == int, 2 == double, 3 == boolean
        if (val.startsWith("\"") && val.endsWith("\"")) {
            return 0;
        } else if (val.matches("\\d+")) {
            return 1;
        } else if (val.matches("[+-]?([0-9]+\\.[0-9]*|\\.[0-9]+)")) {
            return 2;
        } else if (val.equals("true") || val.equals("false")) {
            return 3;
        }
        return -1;
    }

    private static Variable getVar(String name) {
        for (Variable check : vars) {
            if (Objects.equals(check.name, name)) return check;
        }
        return null;
    }

    private static Object strToObj(String line) { // TODO: сделать так, чтобы проверялось, нет ли других " кроме начала и нонца в checkType()
        switch (checkType(line)) {
            case 0:
                return line.replace("\"", "");
            case 1:
                return Integer.parseInt(line);
            case 2:
                return Double.parseDouble(line);
            case 3:
                if (Objects.equals(line, "true")) return true;
                else return false;
            case -1:
                if (getVar(line) != null && getVar(line).getVal() != null) {
                    return getVar(line).getVal();
                } else {
                    System.err.println("ERROR: checkType(): INCORRECT TYPE OF VAR");
                    System.exit(-1);
                }
        }
        return -1;
    }
}