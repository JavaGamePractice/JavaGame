package javagame.tetrominoes.data;

import java.io.Serializable;

/**
 * 坐标，(x, y) 表示。
 */
public class Coords implements Serializable {
    private static final long serialVersionUID = -4221204629878638945L;

    private int x;
    private int y;

    public Coords() {
        super();
    }

    public Coords(final int x, final int y) {
        super();
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Coords moveX(final int dx) {
        x = x + dx;
        return this;
    }

    public Coords moveY(final int dy) {
        y = y + dy;
        return this;
    }

    public Coords copyFrom(final Coords coords) {
        x = coords.x;
        y = coords.y;
        return this;
    }

    @Override
    public String toString() {
        return String.format("(%s, %s)", x, y);
    }

}
