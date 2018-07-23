package java.game.tetrominoes.data;

import java.awt.Color;
import java.game.tetrominoes.enums.MovementDirection;
import java.game.tetrominoes.enums.RotationDirection;
import java.io.Serializable;

/**
 * 俄罗斯方块控制区的数据板实体。
 */
public class Board implements Serializable {
    private static final long serialVersionUID = -4663437492142402258L;
    private static final int BOARD_WIDTH = 10;
    private static final int BOARD_HEIGHT = 22;

    // 宽度（几个方格）
    private final int boardWidth;
    // 高度（几个方格）
    private final int boardHight;

    // 新方块出现的初始位置（非必要放在Class级别）
    private final Coords initialCoords;

    // 数据板，二维数组，存放所有方格的信息，每个方格位置存放对应的方块对象
    private final Shape[][] board;

    // 当前正在动作的方块
    private Shape curShape;

    // 统计数据： 消除的行数
    private int linesOfCleared;
    // 统计数据： 得分
    private int scores;

    public Board() {
	this(Board.BOARD_WIDTH, Board.BOARD_HEIGHT);
    }

    public Board(final int boardWidth, final int boardHight) {
	this.boardWidth = boardWidth;
	this.boardHight = boardHight;
	// 根据方块数据板大小设置生成方块的初始位置
	initialCoords = new Coords((boardWidth / 2), boardHight - 1);

	board = new Shape[boardWidth][boardHight];
	clearBoard();
    }

    public synchronized void clearBoard() {
	linesOfCleared = 0;
	scores = 0;

	for (int i = 0; i < boardWidth; i++) {
	    for (int j = 0; j < boardHight; j++) {
		board[i][j] = null;
	    }
	}
    }

    public synchronized void newShap() {
	curShape = new Shape(initialCoords);
    }

    public synchronized boolean tryToMove(final MovementDirection movementDirection,
	    final RotationDirection rotationDirection) {
	if (checkMovable(curShape, movementDirection, rotationDirection)) {
	    curShape.moveAndRotate(movementDirection, rotationDirection);

	    addOrUpdateShape(curShape);

	    if (movementDirection == MovementDirection.Down) {
		scores = scores + 1;
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

	    final Shape shapeAt = board[x][y];
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

		board[x][y] = null;
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

		if (board[x][y] != null) {
		    System.out.println("方块内容重叠。");
		    return false;
		}

		board[x][y] = shape;
	    }
	}
	return true;
    }

    public synchronized int clearFullLines() {
	final Integer[] yList = curShape.getAllY();

	int cleardLines = 0;
	for (final Integer y : yList) {
	    if (checkLineFullAndClear(y)) {
		cleardLines++;
	    }
	}

	linesOfCleared = linesOfCleared + cleardLines;
	switch (cleardLines) {
	case 4:
	    scores = scores + 500;
	    break;
	case 3:
	    scores = scores + 200;
	    break;
	case 2:
	    scores = scores + 100;
	    break;
	case 1:
	    scores = scores + 50;
	    break;
	default:
	    scores = scores + 1;
	    break;
	}

	return cleardLines;
    }

    private boolean checkLineFullAndClear(final Integer y) {
	for (int x = 0; x < Board.BOARD_WIDTH; x++) {
	    if (board[x][y] == null) {
		// 该行没有满
		return false;
	    }
	}

	for (int x = 0; x < Board.BOARD_WIDTH; x++) {
	    board[x][y] = null;
	}

	final int maxY = Board.BOARD_HEIGHT - 1;
	for (int dy = y; dy < maxY; dy++) {
	    for (int x = 0; x < Board.BOARD_WIDTH; x++) {
		board[x][dy] = board[x][dy + 1];
		board[x][dy + 1] = null;
	    }
	}
	return true;
    }

    public int getBoardWidth() {
	return boardWidth;
    }

    public int getBoardHight() {
	return boardHight;
    }

    public int getLinesOfCleared() {
	return linesOfCleared;
    }

    public int getScores() {
	return scores;
    }

    public Color getColor(final int x, final int y) {
	final Shape shape = board[x][y];
	if (shape == null) {
	    return null;
	}
	return shape.getColor();
    }
}