package com.xinxin.tetrominoes;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Shape implements Serializable {
    private static final long serialVersionUID = -2648641437993769489L;
    private final Tetrominoes tetrominoes;
    private final Coords curCoords;
    private final Coords preCoords;
    // 形状的旋转角度，初始为0度
    private RotationDegree rotationDegree = RotationDegree.Degree0;
    private Coords[] preVertexes;

    public Shape(final Coords curCoords) {
	this.preCoords = new Coords().copyFrom(curCoords);
	this.curCoords = new Coords().copyFrom(curCoords);

	this.tetrominoes = Tetrominoes.buidRandomTetrominoes();
	this.preVertexes = this.getVertexes();

	// 根据图形形状，调整当前坐标
	this.curCoords.moveY(-this.tetrominoes.getInitialMaxY());

	this.logShap();
    }

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
	coords.copyFrom(this.curCoords).moveX(dx).moveY(dy);

	return coords;
    }

    public Coords[] computeRotation(final RotationDirection rotationDirection) {
	if ((rotationDirection == null) || (rotationDirection == RotationDirection.NoRotation)) {
	    // 没有旋转时返回当前的顶点坐标数组
	    return this.getVertexes();
	}

	// 返回旋转目标方向的顶点坐标数组
	return this.tetrominoes.getVertexes(this.rotationDegree.rotate(rotationDirection));
    }

    public void moveAndRotate(final MovementDirection movementDirection, final RotationDirection rotationDirection) {
	// 保留之前的原点位置和顶点坐标
	this.preCoords.copyFrom(this.curCoords);
	this.preVertexes = this.getVertexes();

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
	this.curCoords.moveX(dx).moveY(dy);

	if ((rotationDirection == null) || (rotationDirection == RotationDirection.NoRotation)) {
	    // 没有旋转时
	    return;
	}

	// 旋转角度
	this.rotationDegree = this.rotationDegree.rotate(rotationDirection);

	this.logShap();
    }

    private void logShap() {
	System.out.println("preCoords: " + this.preCoords + "  curCoords: " + this.curCoords + "  rotationDegree: "
		+ this.rotationDegree.name() + "  " + Arrays.toString(this.getVertexes()));
    }

    public Coords[] getVertexes() {
	return this.tetrominoes.getVertexes(this.rotationDegree);
    }

    public Coords[] getPreVertexes() {
	return this.preVertexes;
    }

    public Integer[] getAllY() {
	final Coords[] coords = this.getVertexes();
	final int baseY = this.curCoords.getY();

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
	return this.tetrominoes.getColor();
    }

    public Coords getCurCoords() {
	return this.curCoords;
    }

    public Coords getPreCoords() {
	return this.preCoords;
    }
}