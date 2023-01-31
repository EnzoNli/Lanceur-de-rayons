package comparateur;
public class TestCompareImage{
    public static void main(String[] args) {
        ComparateurImage c = new ComparateurImage(args[0], args[1]);
        c.compare();
    }
}