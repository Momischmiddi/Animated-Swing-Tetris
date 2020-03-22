package containers;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import threads.Shared;

public class MainFrame extends JFrame {

    private StatusPanel statusPanel;
    private GamePanel gamePanel;

    public MainFrame() {
        init();
    }

    private void init() {
        this.statusPanel = new StatusPanel(this);
        this.gamePanel = new GamePanel(this, statusPanel);
        
        this.setTitle("Tetris - By Moritz Schmidt");
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.fill(gamePanel, statusPanel);
        this.setVisible(true);
        this.pack();
        this.setLocationRelativeTo(null);
        
        this.addKeyListener(new KeyListener() {
            @Override public void keyTyped(KeyEvent e) {}
            
            @Override public void keyReleased(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {
                gamePanel.handleKeyPressed(e.getKeyCode());
            }
        });
    }

    private void fill(GamePanel gamePanel, StatusPanel statusPanel) {
        this.add(gamePanel, BorderLayout.WEST);
        this.add(statusPanel, BorderLayout.EAST);
    }    

    public void restart() {
        Shared.reset();
        gamePanel.restart();
    }
}
