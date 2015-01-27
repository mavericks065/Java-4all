package nicolasguignard.lambdas_tests.java.models;

public class Shape {

    public enum Color {
        YELLOW, ORANGE, GREEN, BLUE, PURPLE, BLACK, WHITE, INDIGO, RED, BROWN;
    }

    private Color color;
    private String curve;
    private int surface;

    public void setColor( final Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public String getCurve() {
        return curve;
    }

    public void setCurve(String curve) {
        this.curve = curve;
    }

    public int getSurface() {
        return surface;
    }

    public void setSurface(int surface) {
        this.surface = surface;
    }
}
