import java.util.Scanner;

public class RegexTest {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.print("Write regex: ");
            String str = sc.nextLine();
            str = str.replaceAll("//(?!.*\")(.*)", "");
            System.out.println(str);
        }
    }
}
