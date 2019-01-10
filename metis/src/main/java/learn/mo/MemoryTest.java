package learn.mo;

public class MemoryTest {

    public static void main(String[] args) {
        integerTest();
    }

    public static void integerTest() {
        Integer a = new Integer(8);
        int b = 8;
        Integer c = new Integer(a);
        System.out.println(a == b);
        System.out.println(a == c);
        System.out.println(a.equals(c));
    }
}
