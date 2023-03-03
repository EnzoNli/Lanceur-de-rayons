package rayon;

public class TestLanceurRayon {
    public static void main(String[] args) {
        LanceurRayon r = new LanceurRayon(args[0]);
        r.process();
    }
}
