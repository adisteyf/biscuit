import org.bisqt.Biscuit;
import org.bisqt.BiscuitSyntaxEx;
import org.bisqt.Variable;

public class Main {
    private static void v_c(Variable var) {
        switch (var.name) {
            case "test":
                System.out.println(var.getVal());
                break;
        }
    }

    public static void main(String[] args) {
        Biscuit test = new Biscuit();

        Thread test_th = new Thread(new Runnable() {
            Biscuit test;
            public Runnable init(Biscuit bsqt) {
                test = bsqt;
                return this;
            }

            @Override
            public void run() {
                test.setVarCallback(Main::v_c);
                try {
                    test.readScript("/home/adisteyf/IdeaProjects/biscuit/src/main/testscript.bsqt");
                } catch (BiscuitSyntaxEx e) {
                    System.out.println(e.getMessage());
                }

            }
        }.init(test));
        test_th.start();
    }
}
