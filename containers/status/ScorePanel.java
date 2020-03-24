package containers.status;

import java.awt.Dimension;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import threads.Shared;

public class ScorePanel extends JPanel {
    
    private final JLabel textLabel = new JLabel("Punkte:", JLabel.CENTER);
    private final JLabel scoreLabel = new JLabel("0", JLabel.CENTER);
    private long score = 0;
    private StatusPanel statusPanel;
    
    public ScorePanel(StatusPanel statusPanel) {
        init(statusPanel);
        fill();
    }

    private void init(StatusPanel statusPanel) {
        this.statusPanel = statusPanel;
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.textLabel.setFont(Shared.statusFont);
        this.scoreLabel.setFont(Shared.statusFont);
        
        this.scoreLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        this.textLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        setMinimumSize(new Dimension(5 * Shared.blockSize, 5 * Shared.statusFont.getSize()));
        setMaximumSize(new Dimension(5 * Shared.blockSize, 5 * Shared.statusFont.getSize()));
    }

    private void fill() {
        add(textLabel);
        add(scoreLabel);
    }
    
    public void updateScore(int slicedRows) {
        long gainedScore = calcScore(slicedRows);
        
        score += gainedScore;
        scoreLabel.setText(String.valueOf(score));

        if(Shared.getSpeed()-1 < score / 500) {
            if(statusPanel.getDifficultyPanel().incLevel()) {
                Shared.gameWon = true;
            }
        }
    }
    
    private long calcScore(int slicedRows) {
        long result = 0;
        
        int scoreMult = 7;
        
        for(int i=0 ; i<slicedRows ; i++) {
            result += scoreMult * --scoreMult;
        }
        
        return result;
    }
    
    public void reset() {
        this.score = 0;
    }
}
