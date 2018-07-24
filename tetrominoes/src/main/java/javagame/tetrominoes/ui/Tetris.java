package javagame.tetrominoes.ui;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.border.BevelBorder;

/**
 * 俄罗斯方块主体窗口。
 */
public class Tetris {
    private final JFrame frame;
    private final JLabel statusBar;

    public Tetris() {
        // 构建窗体Frame对象
        frame = new JFrame();

        // 构建消息栏
        statusBar = new JLabel("0");
        frame.add(statusBar, BorderLayout.SOUTH);
        statusBar.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        statusBar.setBackground(new Color(100, 100, 0));

        // 构建方块控制窗口并启动
        final BoardPanel boardPanel = new BoardPanel(this);
        frame.add(boardPanel);
        boardPanel.start();

        // 设置主窗体大小、标题、缺省关闭动作
        frame.setSize(400, 800);
        frame.setTitle("哈哈，俄罗斯方块");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * 设置窗体位置并显示。
     */
    public void startup() {
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /**
     * 更新状态区的文字内容。
     */
    public void updateStatus(final String status) {
        statusBar.setText(status);
    }
}