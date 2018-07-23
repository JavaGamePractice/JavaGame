package com.xinxin.tetrominoes;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JPanel;
import javax.swing.Timer;

public class BoardPanel extends JPanel implements ActionListener {
    private static final long serialVersionUID = -920046887713187928L;

    private final Tetris parent;

    // Swing的定时器，控制方块移动
    private final Timer timer;
    private boolean isStarted = false;
    private boolean isPaused = false;

    private final Coords initialCoords;
    private Shape curShape;
    private final Board board;

    public BoardPanel(final Tetris parent) {
	this.parent = parent;
	this.setFocusable(true);
	this.timer = new Timer(400, this); // timer for lines down
	this.board = new Board();
	this.initialCoords = new Coords((this.board.getBoardWidth() / 2), this.board.getBoardHight() - 1);

	this.addKeyListener(new KeyAdapterAdapter());
    }

    public void start() {
	if (this.isPaused) {
	    return;
	}

	this.isStarted = true;
	this.board.clearBoard();
	this.newShap();
	this.timer.start();

	this.updateStatus("已开始~");
    }

    public void stop() {
	this.timer.stop();
	this.isStarted = false;
	this.isPaused = false;
    }

    public void pause() {
	if (!this.isStarted) {
	    return;
	}

	this.isPaused = !this.isPaused;

	if (this.isPaused) {
	    this.timer.stop();
	    this.updateStatus("暂停中!");
	} else {
	    this.timer.start();
	    this.updateStatus("已开始~");
	}

	this.repaint();
    }

    public void actionPerformed(final ActionEvent ae) {
	BoardPanel.this.tryToMove(MovementDirection.Down, RotationDirection.NoRotation);
    }

    public void newShap() {
	this.curShape = new Shape(this.initialCoords);

	if (!this.tryToMove(MovementDirection.Down, RotationDirection.NoRotation)) {
	    // Game Over
	    this.timer.stop();
	    this.isStarted = false;
	    this.updateStatus("哦哦，噢噢噢!");
	}
    }

    public synchronized boolean tryToMove(final MovementDirection movementDirection,
	    final RotationDirection rotationDirection) {
	if (this.board.tryToMove(this.curShape, movementDirection, rotationDirection)) {
	    this.repaint();

	    this.updateStatus();
	    return true;
	} else {
	    if (movementDirection == MovementDirection.Down) {
		// 消除已经满的行
		if (this.board.clearFullLines(this.curShape) > 0) {
		    this.repaint();
		}

		// 生成新的方块
		this.newShap();
	    }
	}

	this.updateStatus();
	return false;
    }

    private void dropDown() {
	while (true) {
	    final boolean moved = BoardPanel.this.tryToMove(MovementDirection.Down, RotationDirection.NoRotation);
	    if (!moved) {
		break;
	    }
	}
    }

    private void drawSquare(final Graphics g, final int x, final int y, final Color color) {
	g.setColor(color);
	g.fillRect(x + 1, y + 1, this.squareWidth() - 2, this.squareHeight() - 2);
	g.setColor(color.brighter());
	g.drawLine(x, (y + this.squareHeight()) - 1, x, y);
	g.drawLine(x, y, (x + this.squareWidth()) - 1, y);
	g.setColor(color.darker());
	g.drawLine(x + 1, (y + this.squareHeight()) - 1, (x + this.squareWidth()) - 1, (y + this.squareHeight()) - 1);
	g.drawLine((x + this.squareWidth()) - 1, (y + this.squareHeight()) - 1, (x + this.squareWidth()) - 1, y + 1);
    }

    @Override
    public void paint(final Graphics g) {
	super.paint(g);
	final Dimension size = this.getSize();
	final int boardTop = (int) size.getHeight() - (this.board.getBoardHight() * this.squareHeight());

	for (int i = 0; i < this.board.getBoardHight(); i++) {
	    for (int j = 0; j < this.board.getBoardWidth(); ++j) {
		final Color color = this.board.getColor(j, this.board.getBoardHight() - i - 1);

		if (color != null) {
		    this.drawSquare(g, j * this.squareWidth(), boardTop + (i * this.squareHeight()), color);
		}
	    }
	}
    }

    class KeyAdapterAdapter extends KeyAdapter {
	@Override
	public void keyPressed(final KeyEvent ke) {
	    final int keyCode = ke.getKeyCode();

	    if ((keyCode == 'r') || (keyCode == 'R')) {
		BoardPanel.this.stop();
		BoardPanel.this.start();
		return;
	    }

	    if (!BoardPanel.this.isStarted) {
		return;
	    }

	    if ((keyCode == 'p') || (keyCode == 'P')) {
		BoardPanel.this.pause();
	    }

	    if (BoardPanel.this.isPaused) {
		return;
	    }

	    switch (keyCode) {
	    case KeyEvent.VK_LEFT:
		BoardPanel.this.tryToMove(MovementDirection.Left, RotationDirection.NoRotation);
		break;
	    case KeyEvent.VK_RIGHT:
		BoardPanel.this.tryToMove(MovementDirection.Right, RotationDirection.NoRotation);
		break;
	    case KeyEvent.VK_DOWN:
		BoardPanel.this.tryToMove(MovementDirection.NoMovement, RotationDirection.Right);
		break;
	    case KeyEvent.VK_UP:
		BoardPanel.this.tryToMove(MovementDirection.NoMovement, RotationDirection.Left);
		break;
	    case KeyEvent.VK_SPACE:
		BoardPanel.this.dropDown();
		break;
	    case 'd':
	    case 'D':
		BoardPanel.this.tryToMove(MovementDirection.Down, RotationDirection.NoRotation);
		break;
	    }
	}
    }

    private void updateStatus() {
	this.updateStatus("");
    }

    private void updateStatus(final String message) {
	final String status = String.format("已消除: %d  得分: %d  %s", this.board.getLinesOfCleared(),
		this.board.getScores(), message);
	this.parent.updateStatus(status);
    }

    private int squareWidth() {
	return (int) this.getSize().getWidth() / this.board.getBoardWidth();
    }

    private int squareHeight() {
	return (int) this.getSize().getHeight() / this.board.getBoardHight();
    }

}