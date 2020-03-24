package containers;

import containers.status.StatusPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import threads.BackgroundAudioThread;
import threads.Shared;

public class MainFrame extends JFrame {

    private StatusPanel statusPanel;
    private GamePanel gamePanel;
    private BackgroundAudioThread backgroundAudioThread;

    public MainFrame() throws IOException {
        init();
    }

    private void init() throws IOException {
        Shared.blockSize = calculateBlockSize();
        
        this.statusPanel = new StatusPanel(this);
        this.gamePanel = new GamePanel(this, statusPanel);
        this.backgroundAudioThread = new BackgroundAudioThread();
        
        this.setTitle("Swing - Tetris");
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.fill(gamePanel, statusPanel);
        this.setVisible(true);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setIconImage(ImageIO.read(getClass().getClassLoader().getResource("resources/appicon.png")));
        
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
        statusPanel.restart();
        gamePanel.restart();
    }
    
    private int calculateBlockSize() {        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        
        // The game should use the half screen height
        return screenSize.height / Shared.Y_SIZE / 2;
    }

    public BackgroundAudioThread getBackgroundAudioThread() {
        return backgroundAudioThread;
    }

    public void restartBackgroundThread() {
        backgroundAudioThread = new BackgroundAudioThread();
    }
}
