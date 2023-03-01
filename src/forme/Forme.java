package forme;

public abstract class Forme {
    private Color diffuse;

    public Forme() {
        diffuse = new Color(0,0,0);
    }

    public Forme(Color diffuse) {
        this.diffuse = diffuse;
    }

    public Color getDiffuse() {
        return diffuse;
    }

}
