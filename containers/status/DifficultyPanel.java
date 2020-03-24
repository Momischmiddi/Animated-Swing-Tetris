package containers.status;

import containers.MainFrame;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import threads.Shared;

public class DifficultyPanel extends JPanel {

    private final JLabel levelLabel = new JLabel("Stufe:", JLabel.CENTER);
    private final JLabel levelValueLabel = new JLabel("1", JLabel.CENTER);
    
    private final JButton startButton = new JButton("Start");
    private int level = 1;
    private MainFrame mainFrame;
    
    public DifficultyPanel(MainFrame mainFrame) {
        init(mainFrame);
        fill();
    }

    private void fill() {
        add(levelLabel);
        add(Box.createRigidArea(new Dimension(0, Shared.blockSize / 10)));
        add(levelValueLabel);
        add(Box.createRigidArea(new Dimension(0, Shared.blockSize * Shared.Y_SIZE / 5)));
        add(startButton);
    }

    private void init(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        
        
        this.levelLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        this.levelValueLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        this.startButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
        
        startButton.setFocusable(false);
        this.levelValueLabel.setFont(Shared.statusFont);
        this.levelLabel.setFont(Shared.statusFont);
        this.startButton.setFont(Shared.statusFont);

        
        startButton.addActionListener((ActionEvent e) -> {
            mainFrame.restart();
        });
    }

    public void enableDifficulty(boolean enabled) {
        this.startButton.setEnabled(enabled);
    }
    
    public boolean incLevel() {
        Shared.setSpeed(++level);
        
        levelValueLabel.setText(String.valueOf(level));
        
        return level == 34;
   }    

    public void reset() {
        level = 1;
    }
}