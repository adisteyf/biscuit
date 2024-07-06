# Biscuit
Programming language for **configuration Java apps**.
# Syntax
```bsqt
set test "string"
set test1 123 // int
set test2 12.3 // double
set test3 true // boolean
test "new value"
```

# Hello world program
Main.java
```java
import org.bisqt.Biscuit;

public class Main {
    public static void main(String[] args) {
        Biscuit test = new Biscuit();
        test.readScript("<path to your biscuit script>/src/main/hw.bsqt");
    }
}
```
hw.bsqt
```bsqt
set $echo "Hello world"
```

# How to build
1. `git clone https://github.com/adisteyf/biscuit`.
2. `cd biscuit`.
3. `./gradlew build`.