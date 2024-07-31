package org.bisqt;

import java.util.ArrayList;

public class BiscuitEx extends Exception {
    public BiscuitEx(String err_id, int on_line, ArrayList<String> line, String vars_str) {
        super("\nERR: "+err_id+"\nON LINE: "+on_line+" ("+line+")\nVARS: {"+vars_str+"}");
    }
}
