package org.bisqt;

public class Variable {
    final String name;
    private Object val;
    public Variable(String name, Object val) {
        this.name = name;
        this.val = val;
    }

    public void chVal(Object newVal) {
        val = newVal;
    }
    public Object getVal() {
        return val;
    }
}
