package java.game.tetrominoes;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.border.BevelBorder;

/**
 * 俄罗斯方块主体窗口构造对象。
 */
public class Tetris {
    private final JFrame frame;
    private final JLabel statusBar;

    public Tetris() {
	// 构建窗体Frame对象
	this.frame = new JFrame();

	// 构建消息栏
	this.statusBar = new JLabel("0");
	this.frame.add(this.statusBar, BorderLayout.SOUTH);
	this.statusBar.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
	this.statusBar.setBackground(new Color(100, 100, 0));

	// 构建主窗体并启动
	final BoardPanel board = new BoardPanel(this);
	this.frame.add(board);
	board.start();

	// 设置主窗体大小、标题、缺省关闭动作
	this.frame.setSize(400, 800);
	this.frame.setTitle("哈哈，俄罗斯方块");
	this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * 设置窗体位置并显示。
     */
    public void startup() {
	this.frame.setLocationRelativeTo(null);
	this.frame.setVisible(true);
    }

    public void updateStatus(final String status) {
	this.statusBar.setText(status);
    }
}