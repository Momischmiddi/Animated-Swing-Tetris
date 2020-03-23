package containers.status;

import figure.Figure;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import threads.Shared;

public class NextPanelContainer extends JPanel {
    
    private final JLabel nextBlockLabel = new JLabel("Nächster Block:", JLabel.CENTER);
    private NextPanel nextPanel;
    
    public NextPanelContainer() {
        init();
        fill();
    }

    private void init() {
        this.nextPanel = new NextPanel();
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.nextBlockLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        this.nextBlockLabel.setFont(Shared.statusFont);
    }

    private void fill() {
        this.add(nextBlockLabel, BorderLayout.NORTH);
        this.add(nextPanel, BorderLayout.SOUTH);
    }
    
    public void update(Figure nextFigure) {
        this.nextPanel.setNextFigure(nextFigure);
        this.nextPanel.repaint();
    }
}