package GUI;

public enum Ratio {
    SMALL(10), MEDIUM(100), LARGE(1000);

    private int ratio;
    Ratio(int r) {
        ratio = r;
    }

    public int getRatio(){
        return ratio;
    }

}
