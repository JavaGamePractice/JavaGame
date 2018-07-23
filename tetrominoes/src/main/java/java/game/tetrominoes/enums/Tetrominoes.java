package java.game.tetrominoes.enums;

import java.awt.Color;
import java.game.tetrominoes.data.Coords;
import java.util.Random;

/**
 * 方块（形状）类型。
 */
public enum Tetrominoes {
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

    // 方块各个顶点坐标数组
    private Coords[][] vertexes;
    // 方块颜色
    private Color color;

    private Tetrominoes(final Coords[] coords, final Color c) {
	vertexes = new Coords[RotationDegree.values().length][];
	vertexes[RotationDegree.Degree0.ordinal()] = coords;

	// 初始化各个角度的形状数据
	initVertexes();

	color = c;
    }

    /**
     * 逆时针（向左）旋转计算剩下3个方向的形状坐标数据。
     */
    private void initVertexes() {
	for (final RotationDegree degree : RotationDegree.values()) {
	    if (degree == RotationDegree.Degree0) {
		// 0度作为初始值不需要计算，跳过
		continue;
	    }

	    // 获取前一个度数及顶点坐标数组
	    final RotationDegree preDegree = degree.rotate(RotationDirection.Right);
	    final Coords[] preVertexes = vertexes[preDegree.ordinal()];

	    // 构造当前度数的顶点坐标数组，并赋值到顶点数组中
	    final Coords[] curVertexes = new Coords[preVertexes.length];
	    vertexes[degree.ordinal()] = curVertexes;

	    // 计算坐标（逆时针（向左）旋转算法）
	    for (int i = 0; i < preVertexes.length; i++) {
		curVertexes[i] = new Coords(preVertexes[i].getY(), -preVertexes[i].getX());
	    }
	}
    }

    /**
     * 获取一个随机的方块。
     */
    public static Tetrominoes buidRandomTetrominoes() {
	final Random random = new Random(System.currentTimeMillis());
	final Tetrominoes[] values = Tetrominoes.values();
	// 生成随机数
	final int randomIndex = random.nextInt(values.length);

	return values[randomIndex];
    }

    /**
     * 根据角度获取当前方块（形状）的顶点数组。
     *
     * @param degree
     *            角度
     * @return 顶点数组
     */
    public Coords[] getVertexes(final RotationDegree degree) {
	return vertexes[degree.ordinal()];
    }

    /**
     * 获取方块颜色
     * 
     * @return 方块颜色
     */
    public Color getColor() {
	return color;
    }

    /**
     * 获取一个方块初始Y最高顶点的值（为了判断方块初始出现时放置的位置）
     * 
     * @return 最高Y坐标值
     */
    public int getInitialMaxY() {
	final Coords[] coordsOfDegree0 = vertexes[RotationDegree.Degree0.ordinal()];
	int m = coordsOfDegree0[0].getY();

	for (final Coords coords : coordsOfDegree0) {
	    m = Math.max(m, coords.getY());
	}
	return m;
    }

}