package javagame.tetrominoes.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JPanel;
import javax.swing.Timer;

import javagame.tetrominoes.data.Board;
import javagame.tetrominoes.enums.MovementDirection;
import javagame.tetrominoes.enums.RotationDirection;

/**
 * 俄罗斯方块操控区面板。
 */
public class BoardPanel extends JPanel implements ActionListener {
    private static final long serialVersionUID = -920046887713187928L;

    // 主体窗口控制器引用
    private final Tetris parent;

    // Swing的定时器，控制方块移动
    private final Timer timer;
    // 运行中状态
    private boolean isRunning = false;
    // 暂停状态
    private boolean isPaused = false;

    // 方块控制区的数据板（方块控制区对应的数据）
    private final Board board;

    public BoardPanel(final Tetris parent) {
	this.parent = parent;

	// 初始化定时器
	timer = new Timer(400, this); // timer for lines down

	// 初始化方块数据板
	board = new Board();

	// 添加键盘侦听事件
	addKeyListener(new KeyAdapterAdapter());

	// 设置窗口焦点到这个控件
	setFocusable(true);
    }

    /**
     * 启动： 清空控制区，产生一个新方块，设置运行标志，启动定时器。
     */
    public void start() {
	if (isPaused) {
	    return;
	}

	board.clearBoard();
	newShap();

	isRunning = true;
	timer.start();

	this.updateStatus("已开始~");
    }

    /**
     * 停止： 停止定时器，设置运行标志。
     */
    public void stop() {
	timer.stop();
	isRunning = false;
    }

    /**
     * 暂停： 暂停定时器。
     */
    public void pause() {
	if (!isRunning) {
	    return;
	}

	isPaused = !isPaused;

	if (isPaused) {
	    timer.stop();
	    this.updateStatus("暂停中!");
	} else {
	    timer.start();
	    this.updateStatus("已开始~");
	}

	// 刷新Panel区的内容
	this.repaint();
    }

    /**
     * 定时器任务触发后调用的事件，把当前方块下落一格。
     */
    public void actionPerformed(final ActionEvent ae) {
	BoardPanel.this.tryToMove(MovementDirection.Down, RotationDirection.NoRotation);
    }

    /**
     * 产生一个新方块，并立即尝试向下移动一格，如果能够移动，继续，否则判定为游戏结束。
     */
    public void newShap() {
	board.newShap();

	if (!tryToMove(MovementDirection.Down, RotationDirection.NoRotation)) {
	    // Game Over
	    timer.stop();
	    isRunning = false;
	    this.updateStatus("哦哦，噢噢噢!");
	}
    }

    /**
     * 尝试移动当前方块。
     *
     * @param movementDirection
     *            移动方向
     * @param rotationDirection
     *            旋转方向
     * @return 是否移动成功
     */
    public boolean tryToMove(final MovementDirection movementDirection, final RotationDirection rotationDirection) {
	// 调用数据板进行当前方块移动
	if (board.tryToMove(movementDirection, rotationDirection)) {
	    // 移动成功，重画刷新画面
	    this.repaint();

	    // 更新状态栏
	    this.updateStatus();
	    return true;
	} else {
	    // 方块不能移动，则判断如果为下落，则尝试判断是否需要检查有无满行，进行清空；同时生成新方块
	    if (movementDirection == MovementDirection.Down) {
		// 消除已经满的行
		if (board.clearFullLines() > 0) {
		    // 重画刷新画面
		    this.repaint();
		}

		// 生成新的方块
		newShap();
		this.updateStatus();
	    }
	    return false;
	}
    }

    /**
     * 控制当前方块快速下落，直到不能移动。
     */
    private void dropDown() {
	while (true) {
	    final boolean moved = BoardPanel.this.tryToMove(MovementDirection.Down, RotationDirection.NoRotation);
	    if (!moved) {
		break;
	    }
	}
    }

    /**
     * JPanel的画画方法（事件），当有repaint事件发生时被触发。
     */
    @Override
    public void paint(final Graphics graphics) {
	// 调用父类的处理
	super.paint(graphics);

	// 获取Panel尺寸，支持窗口缩放
	final Dimension size = this.getSize();
	// 计算Panel窗口大小不能整除的时候上方留白
	final int boardTop = (int) size.getHeight() - (board.getBoardHight() * squareHeight());

	for (int y = 0; y < board.getBoardHight(); y++) {
	    for (int x = 0; x < board.getBoardWidth(); ++x) {
		// y轴原点在下方，进行存储数据和计算
		final Color color = board.getColor(x, board.getBoardHight() - y - 1);

		if (color != null) {
		    // 如果方格内有方块，则对应有颜色，则根据方块的颜色绘画方格
		    drawSquare(graphics, x * squareWidth(), boardTop + (y * squareHeight()), color);
		}
	    }
	}
    }

    /**
     * 绘画方格。
     */
    private void drawSquare(final Graphics g, final int x, final int y, final Color color) {
	// 用颜色画方格
	g.setColor(color);
	g.fillRect(x + 1, y + 1, squareWidth() - 2, squareHeight() - 2);

	// 画方格左下两个边框
	g.setColor(color.brighter());
	g.drawLine(x, (y + squareHeight()) - 1, x, y);
	g.drawLine(x, y, (x + squareWidth()) - 1, y);
	// 画方格右上两个边框
	g.setColor(color.darker());
	g.drawLine(x + 1, (y + squareHeight()) - 1, (x + squareWidth()) - 1, (y + squareHeight()) - 1);
	g.drawLine((x + squareWidth()) - 1, (y + squareHeight()) - 1, (x + squareWidth()) - 1, y + 1);
    }

    /**
     * 键盘事件处理类。
     */
    class KeyAdapterAdapter extends KeyAdapter {
	@Override
	public void keyPressed(final KeyEvent keyEvent) {
	    final int keyCode = keyEvent.getKeyCode();

	    if ((keyCode == 'r') || (keyCode == 'R')) {
		stop();
		start();
		return;
	    }

	    if (!isRunning) {
		return;
	    }

	    if ((keyCode == 'p') || (keyCode == 'P')) {
		pause();
	    }

	    if (isPaused) {
		return;
	    }

	    switch (keyCode) {
	    case KeyEvent.VK_LEFT:
		tryToMove(MovementDirection.Left, RotationDirection.NoRotation);
		break;
	    case KeyEvent.VK_RIGHT:
		tryToMove(MovementDirection.Right, RotationDirection.NoRotation);
		break;
	    case KeyEvent.VK_DOWN:
		tryToMove(MovementDirection.NoMovement, RotationDirection.Right);
		break;
	    case KeyEvent.VK_UP:
		tryToMove(MovementDirection.NoMovement, RotationDirection.Left);
		break;
	    case KeyEvent.VK_SPACE:
		dropDown();
		break;
	    case 'd':
	    case 'D':
		tryToMove(MovementDirection.Down, RotationDirection.NoRotation);
		break;
	    }
	}
    }

    /**
     * 更新状态栏，不带附加消息。
     */
    private void updateStatus() {
	this.updateStatus("");
    }

    /**
     * 更新状态栏，附带消息。
     *
     * @param message
     *            消息文本内容
     */
    private void updateStatus(final String message) {
	final String status = String.format("已消除: %d  得分: %d  %s", board.getLinesOfCleared(), board.getScores(),
		message);

	// 调用父窗口控件的更新状态栏方法
	parent.updateStatus(status);
    }

    /**
     * 方块一格的宽度。
     *
     * @return 方块一格宽度
     */
    private int squareWidth() {
	return (int) this.getSize().getWidth() / board.getBoardWidth();
    }

    /**
     * 方块一格的高度。
     *
     * @return 方块一格高度
     */
    private int squareHeight() {
	return (int) this.getSize().getHeight() / board.getBoardHight();
    }

}