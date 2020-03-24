package containers.status;

import containers.MainFrame;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import threads.Shared;

public class StatusPanel extends JPanel {
    
    private NextPanelContainer nextPanelContainer;
    private ScorePanel scorePanel;
    private DifficultyPanel difficultyPanel;
    
    private MainFrame mainFrame;
    
    public StatusPanel(MainFrame mainFrame) {
        init(mainFrame);
        fill();
    }
    
    private void fill() {
        this.add(this.nextPanelContainer);
        this.add(this.scorePanel);
        this.add(this.difficultyPanel);
    }
    
    private void init(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        
        this.nextPanelContainer = new NextPanelContainer();
        this.scorePanel = new ScorePanel(this);
        this.difficultyPanel = new DifficultyPanel(mainFrame);
    }
    
    public NextPanelContainer getNextPanelContainer() {
        return this.nextPanelContainer;
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
