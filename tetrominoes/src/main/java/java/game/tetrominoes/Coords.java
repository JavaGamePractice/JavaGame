package java.game.tetrominoes;

import java.io.Serializable;

public class Coords implements Cloneable, Serializable {
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
	return this.x;
    }

    public int getY() {
	return this.y;
    }

    public Coords copyFrom(final Coords coords) {
	this.x = coords.x;
	this.y = coords.y;
	return this;
    }

    public Coords moveX(final int dx) {
	this.x = this.x + dx;
	return this;
    }

    public Coords moveY(final int dy) {
	this.y = this.y + dy;
	return this;
    }

    @Override
    public String toString() {
	return String.format("(%s, %s)", this.x, this.y);
    }

}
