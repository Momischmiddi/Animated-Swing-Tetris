package containers;

import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import threads.Shared;

public class ScorePanel extends JPanel {
    
    private final Font font = new Font("Comic Sans MS", Font.BOLD, 31);
    private final JLabel textLabel = new JLabel("Erzielte Punkte:", JLabel.CENTER);
    private final JLabel scoreLabel = new JLabel("0", JLabel.CENTER);
    private long score = 0;
    
    public ScorePanel() {
        init();
        fill();
    }

    private void init() {
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.textLabel.setFont(font);
        this.scoreLabel.setFont(font);
        
        this.scoreLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        this.textLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        
        setPreferredSize(new Dimension(275, 250));
    }

    private void fill() {
        add(textLabel);
        add(scoreLabel);
    }

    public void update(int slicedRows) {
        score += calcScore(slicedRows);
        scoreLabel.setText(String.valueOf(score));
    }
    
    private long calcScore(int slicedRows) {
        long result = 0;
        
        int scoreMult = 7;
        
        for(int i=0 ; i<slicedRows ; i++) {
            result += scoreMult * --scoreMult;
        }
        
        return result * Shared.getSpeed();
    }
    
    public void reset() {
        this.score = 0;
    }
}
