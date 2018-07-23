package java.game.tetrominoes.data;

import java.awt.Color;
import java.game.tetrominoes.enums.RotationDegree;
import java.util.Random;

enum Tetrominoes {
    // NoShape(new Coords[] { new Coords(0, 0), new Coords(0, 0), new Coords(0,
    // 0), new Coords(0, 0) },
    // new Color(0, 0, 0)), //
    ZShape(new Coords[] { new Coords(0, -1), new Coords(0, 0), new Coords(-1, 0), new Coords(-1, 1) },
	    new Color(204, 102, 102)), //
    SShape(new Coords[] { new Coords(0, -1), new Coords(0, 0), new Coords(1, 0), new Coords(1, 1) },
	    new Color(102, 204, 102)), //
    LineShape(new Coords[] { new Coords(0, -1), new Coords(0, 0), new Coords(0, 1), new Coords(0, 2) },
	    new Color(102, 102, 204)), //
    TShape(new Coords[] { new Coords(-1, 0), new Coords(0, 0), new Coords(1, 0), new Coords(0, 1) },
	    new Color(204, 204, 102)), //
    SquareShape(new Coords[] { new Coords(0, 0), new Coords(1, 0), new Coords(0, 1), new Coords(1, 1) },
	    new Color(204, 102, 204)), //
    LShape(new Coords[] { new Coords(-1, -1), new Coords(0, -1), new Coords(0, 0), new Coords(0, 1) },
	    new Color(102, 204, 204)), //
    MirroredLShape(new Coords[] { new Coords(1, -1), new Coords(0, -1), new Coords(0, 0), new Coords(0, 1) },
	    new Color(218, 170, 0));//

    private Coords[][] vertexes;
    private Color color;

    private Tetrominoes(final Coords[] coords, final Color c) {
	this.vertexes = new Coords[RotationDegree.values().length][];
	this.vertexes[RotationDegree.Degree0.ordinal()] = coords;
	this.initVertexes();

	this.color = c;
    }

    /**
     * 逆时针（向左）旋转计算剩下3个方向的坐标
     */
    private void initVertexes() {
	for (final RotationDegree degree : RotationDegree.values()) {
	    if (degree == RotationDegree.Degree0) {
		// 0度作为初始值不需要计算，跳过
		continue;
	    }

	    // 获取前一个度数及顶点坐标数组
	    final RotationDegree preDegree = degree.rotateRight();
	    final Coords[] preVertexes = this.vertexes[preDegree.ordinal()];

	    // 构造当前度数的顶点坐标数组，并赋值到顶点数组中
	    final Coords[] curVertexes = new Coords[preVertexes.length];
	    this.vertexes[degree.ordinal()] = curVertexes;

	    // 计算坐标（逆时针（向左）旋转算法）
	    for (int i = 0; i < preVertexes.length; i++) {
		curVertexes[i] = new Coords(preVertexes[i].getY(), -preVertexes[i].getX());
	    }
	}
    }

    public static Tetrominoes buidRandomTetrominoes() {
	final Random random = new Random(System.currentTimeMillis());
	final Tetrominoes[] values = Tetrominoes.values();
	// 生成随机数，并跳过NoShape
	final int randomIndex = random.nextInt(values.length);

	return values[randomIndex];
    }

    public Coords[] getVertexes(final RotationDegree degree) {
	return this.vertexes[degree.ordinal()];
    }

    public Color getColor() {
	return this.color;
    }

    public int getInitialMaxY() {
	final Coords[] coordsOfDegree0 = this.vertexes[RotationDegree.Degree0.ordinal()];
	int m = coordsOfDegree0[0].getY();

	for (final Coords coords : coordsOfDegree0) {
	    m = Math.max(m, coords.getY());
	}
	return m;
    }

}