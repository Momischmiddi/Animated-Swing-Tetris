package containers;

import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class StatusPanel extends JPanel {
    
    private NextPanel nextPanel;
    private ScorePanel scorePanel;
    private DifficultyPanel difficultyPanel;
    
    private final Font font = new Font("Comic Sans MS", Font.BOLD, 31);
    private final JLabel nextBlockLabel = new JLabel("Nächster Block:", JLabel.CENTER);
    private MainFrame mainFrame;
    
    public StatusPanel(MainFrame mainFrame) {
        init(mainFrame);
        fill();
    }
    
    private void fill() {
        this.add(this.nextBlockLabel);
        this.add(this.nextPanel);
        this.add(this.scorePanel);
        this.add(this.difficultyPanel);
    }
    
    private void init(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        this.nextPanel = new NextPanel();
        this.scorePanel = new ScorePanel();
        this.difficultyPanel = new DifficultyPanel(mainFrame);
        
        this.nextBlockLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        this.nextBlockLabel.setFont(this.font);
    }
    
    public NextPanel getNextPanel() {
        return this.nextPanel;
    }
    
    public ScorePanel getScorePanel() {
        return this.scorePanel;
    }
    
    public DifficultyPanel getDifficultyPanel() {
        return this.difficultyPanel;
    }

    public void restart() {
        scorePanel.reset();
    }
}
