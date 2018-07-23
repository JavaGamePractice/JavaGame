package com.xinxin.tetrominoes;

import java.awt.Color;
import java.io.Serializable;

public class Board implements Serializable {
    private static final long serialVersionUID = -4663437492142402258L;
    private static final int BOARD_WIDTH = 10;
    private static final int BOARD_HEIGHT = 22;

    private final int boardWidth;
    private final int boardHight;
    private final Shape[][] board;

    private int linesOfCleared;
    private int scores;

    public Board() {
	this(Board.BOARD_WIDTH, Board.BOARD_HEIGHT);
    }

    public Board(final int boardWidth, final int boardHight) {
	this.boardWidth = boardWidth;
	this.boardHight = boardHight;

	this.board = new Shape[boardWidth][boardHight];
	this.clearBoard();
    }

    // public Shape at(final int x, final int y) {
    // return this.board[x][y];
    // }

    public synchronized void clearBoard() {
	this.linesOfCleared = 0;
	this.scores = 0;

	for (int i = 0; i < this.boardWidth; i++) {
	    for (int j = 0; j < this.boardHight; j++) {
		this.board[i][j] = null;
	    }
	}
    }

    public synchronized boolean tryToMove(final Shape shape, final MovementDirection movementDirection,
	    final RotationDirection rotationDirection) {
	if (this.checkMovable(shape, movementDirection, rotationDirection)) {
	    shape.moveAndRotate(movementDirection, rotationDirection);

	    this.addOrUpdateShape(shape);

	    if (movementDirection == MovementDirection.Down) {
		this.scores = this.scores + 1;
	    }
	    return true;
	}
	return false;
    }

    private boolean checkMovable(final Shape shape, final MovementDirection movementDirection,
	    final RotationDirection rotationDirection) {

	final Coords targetBaseCoords = shape.computeMovable(movementDirection);
	final Coords[] targetRotationVertexes = shape.computeRotation(rotationDirection);

	final int baseX = targetBaseCoords.getX();
	final int baseY = targetBaseCoords.getY();

	for (final Coords coord : targetRotationVertexes) {
	    final int x = coord.getX() + baseX;
	    final int y = coord.getY() + baseY;

	    if ((x < 0) || (x >= Board.BOARD_WIDTH) || (y < 0) || (y >= Board.BOARD_HEIGHT)) {
		return false;
	    }

	    final Shape shapeAt = this.board[x][y];
	    if ((shapeAt != null) && !shapeAt.equals(shape)) {
		return false;
	    }
	}

	return true;
    }

    private boolean addOrUpdateShape(final Shape shape) {
	{
	    final Coords baseCoords = shape.getPreCoords();
	    final int baseX = baseCoords.getX();
	    final int baseY = baseCoords.getY();
	    for (final Coords coord : shape.getPreVertexes()) {
		final int x = coord.getX() + baseX;
		final int y = coord.getY() + baseY;

		if ((x < 0) || (x >= Board.BOARD_WIDTH) || (y < 0) || (y >= Board.BOARD_HEIGHT)) {
		    continue;
		}

		this.board[x][y] = null;
	    }
	}

	{
	    final Coords baseCoords = shape.getCurCoords();
	    final int baseX = baseCoords.getX();
	    final int baseY = baseCoords.getY();
	    for (final Coords coord : shape.getVertexes()) {
		final int x = coord.getX() + baseX;
		final int y = coord.getY() + baseY;

		if ((x < 0) || (x >= Board.BOARD_WIDTH) || (y < 0) || (y >= Board.BOARD_HEIGHT)) {
		    System.out.println("方块位置超界(" + x + ", " + y + ")");
		    return false;
		}

		if (this.board[x][y] != null) {
		    System.out.println("方块内容重叠。");
		    return false;
		}

		this.board[x][y] = shape;
	    }
	}
	return true;
    }

    public synchronized int clearFullLines(final Shape curShape) {
	final Integer[] yList = curShape.getAllY();

	int cleardLines = 0;
	for (final Integer y : yList) {
	    if (this.checkLineFullAndClear(y)) {
		cleardLines++;
	    }
	}

	this.linesOfCleared = this.linesOfCleared + cleardLines;
	switch (cleardLines) {
	case 4:
	    this.scores = this.scores + 500;
	    break;
	case 3:
	    this.scores = this.scores + 200;
	    break;
	case 2:
	    this.scores = this.scores + 100;
	    break;
	case 1:
	    this.scores = this.scores + 50;
	    break;
	default:
	    this.scores = this.scores + 1;
	    break;
	}

	return cleardLines;
    }

    private boolean checkLineFullAndClear(final Integer y) {
	for (int x = 0; x < Board.BOARD_WIDTH; x++) {
	    if (this.board[x][y] == null) {
		// 该行没有满
		return false;
	    }
	}

	for (int x = 0; x < Board.BOARD_WIDTH; x++) {
	    this.board[x][y] = null;
	}

	final int maxY = Board.BOARD_HEIGHT - 1;
	for (int dy = y; dy < maxY; dy++) {
	    for (int x = 0; x < Board.BOARD_WIDTH; x++) {
		this.board[x][dy] = this.board[x][dy + 1];
		this.board[x][dy + 1] = null;
	    }
	}
	return true;
    }

    public int getBoardWidth() {
	return this.boardWidth;
    }

    public int getBoardHight() {
	return this.boardHight;
    }

    public int getLinesOfCleared() {
	return this.linesOfCleared;
    }

    public int getScores() {
	return this.scores;
    }

    public Color getColor(final int x, final int y) {
	final Shape shape = this.board[x][y];
	if (shape == null) {
	    return null;
	}
	return shape.getColor();
    }
}