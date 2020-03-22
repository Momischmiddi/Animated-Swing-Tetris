package containers;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import threads.Shared;

public class DifficultyPanel extends JPanel {

    private final Font font = new Font("Comic Sans MS", Font.BOLD, 31);
    private final JLabel speedLabel = new JLabel("Geschwindigkeit:", JLabel.CENTER);
    private final JSlider difficultySlider = new JSlider(JSlider.HORIZONTAL);
    
    private final JButton startButton = new JButton("Start");
    private MainFrame mainFrame;
    
    public DifficultyPanel(MainFrame mainFrame) {
        init(mainFrame);
        fill();
    }

    private void fill() {
        add(speedLabel);
        add(difficultySlider);
        add(startButton);
    }

    private void init(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(275, 250));
        this.speedLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        this.startButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
        difficultySlider.setFocusable(false);
        startButton.setFocusable(false);
        this.speedLabel.setFont(font);
        this.startButton.setFont(font);
                
        difficultySlider.setMinimum(1);
        difficultySlider.setMaximum(5);
        difficultySlider.setValue(2);
        difficultySlider.setMajorTickSpacing(1);
        difficultySlider.setPaintTicks(true);
        difficultySlider.setPaintLabels(true);
        difficultySlider.setPaintTrack(true);
        
        startButton.addActionListener((ActionEvent e) -> {
            Shared.setSpeed(difficultySlider.getValue());
            mainFrame.restart();
        });
    }

    public void enableDifficulty(boolean enabled) {
        this.startButton.setEnabled(enabled);
    }
    
}
