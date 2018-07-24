package javagame.tetrominoes.data;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javagame.tetrominoes.enums.MovementDirection;
import javagame.tetrominoes.enums.RotationDegree;
import javagame.tetrominoes.enums.RotationDirection;
import javagame.tetrominoes.enums.Tetrominoes;

/**
 * 一个方块（形状）的数据实体。
 */
public class Shape implements Serializable {
    private static final long serialVersionUID = -2648641437993769489L;

    // 使用的方式形状
    private final Tetrominoes tetrominoes;

    // 当前形状的原点坐标
    private final Coords curCoords;
    // 旋转前的原点坐标
    private final Coords preCoords;
    // 形状的旋转角度，初始为0度
    private RotationDegree rotationDegree = RotationDegree.Degree0;
    // 旋转前的顶点坐标数组（当前的通过旋转角度可以随时获取，不需要另存）
    private Coords[] preVertexes;

    public Shape(final Coords initialCoords) {
        preCoords = new Coords().copyFrom(initialCoords);
        curCoords = new Coords().copyFrom(initialCoords);

        tetrominoes = Tetrominoes.buidRandomTetrominoes();
        preVertexes = getVertexes();

        // 根据图形形状，调整当前坐标
        curCoords.moveY(-tetrominoes.getInitialMaxY());
        preCoords.copyFrom(curCoords);

        logShap();
    }

    /**
     * 计算移动后的原点位置。
     *
     * @param movementDirection
     *            移动方向
     * @return 移动后坐标
     */
    public Coords computeMovable(final MovementDirection movementDirection) {
        int dx = 0;
        int dy = 0;
        switch (movementDirection) {
        case Left:
            dx = -1;
            break;
        case Right:
            dx = 1;
            break;
        case Down:
            dy = -1;
            break;
        default:
            break;
        }

        final Coords coords = new Coords();
        coords.copyFrom(curCoords).moveX(dx).moveY(dy);

        return coords;
    }

    /**
     * 计算旋转后顶点坐标数组
     *
     * @param rotationDirection
     *            旋转方向
     * @return 旋转后顶点坐标数组
     */
    public Coords[] computeRotation(final RotationDirection rotationDirection) {
        if ((rotationDirection == null) || (rotationDirection == RotationDirection.NoRotation)) {
            // 没有旋转时返回当前的顶点坐标数组
            return getVertexes();
        }

        // 返回旋转目标方向的顶点坐标数组
        return tetrominoes.getVertexes(rotationDegree.rotate(rotationDirection));
    }

    /**
     * 移动旋转方块。
     *
     * @param movementDirection
     *            移动方向
     * @param rotationDirection
     *            旋转方向
     */
    public synchronized void moveAndRotate(final MovementDirection movementDirection,
            final RotationDirection rotationDirection) {
        // 保留之前的原点位置和顶点坐标
        preCoords.copyFrom(curCoords);
        preVertexes = getVertexes();

        // 更新原点位置
        int dx = 0;
        int dy = 0;
        switch (movementDirection) {
        case Left:
            dx = -1;
            break;
        case Right:
            dx = 1;
            break;
        case Down:
            dy = -1;
            break;
        default:
            break;
        }
        curCoords.moveX(dx).moveY(dy);

        if ((rotationDirection == null) || (rotationDirection == RotationDirection.NoRotation)) {
            // 没有旋转时
            return;
        }

        // 旋转角度
        rotationDegree = rotationDegree.rotate(rotationDirection);

        logShap();
    }

    private void logShap() {
        System.out.println("preCoords: " + preCoords + "  curCoords: " + curCoords + "  rotationDegree: "
                + rotationDegree.name() + "  " + Arrays.toString(getVertexes()));
    }

    public Coords[] getVertexes() {
        return tetrominoes.getVertexes(rotationDegree);
    }

    public Coords[] getPreVertexes() {
        return preVertexes;
    }

    public Integer[] getAllY() {
        final Coords[] coords = getVertexes();
        final int baseY = curCoords.getY();

        final Set<Integer> ySet = new HashSet<Integer>();
        for (final Coords coord : coords) {
            ySet.add(coord.getY() + baseY);
        }

        final Integer[] yArray = new Integer[ySet.size()];
        final ArrayList<Integer> yList = new ArrayList<Integer>(ySet);
        Collections.sort(yList);
        Collections.reverse(yList);
        return yList.toArray(yArray);
    }

    public Color getColor() {
        return tetrominoes.getColor();
    }

    public Coords getCurCoords() {
        return curCoords;
    }

    public Coords getPreCoords() {
        return preCoords;
    }
}