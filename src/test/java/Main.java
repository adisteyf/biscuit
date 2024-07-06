import org.bisqt.Biscuit;
import org.bisqt.Variable;

public class Main {
    public static void varCallbackTest(Variable var) {
        System.out.println("!!! "+var.name+". !!! "+var.getVal());
    }
    public static void main(String[] args) {
        Biscuit test = new Biscuit();
        test.setVarCallback(Main::varCallbackTest);
        test.readScript("/home/adisteyf/IdeaProjects/biscuit/src/main/testscript.bsqt");
    }
}
