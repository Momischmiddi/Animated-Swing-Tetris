package containers;

import figure.Block;
import figure.Figure;
import figure.IFigure;
import figure.LFigure;
import figure.SnakeLeftFigure;
import figure.SnakeRightFigure;
import figure.SquareFigure;
import figure.TFigure;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import javax.swing.JPanel;

public class NextPanel extends JPanel {

    private Figure nextFigure;
    
    public NextPanel() {
        init();
    }
    
    @Override
    public void paintComponent(Graphics grahpics) {
        super.paintComponent(grahpics);
        
        Graphics2D g2d = (Graphics2D) grahpics;
        Stroke oldStroke = g2d.getStroke();
        
        int xOffset = 0;
        
        if(nextFigure instanceof SquareFigure) {
            xOffset = -75;
        } else if(nextFigure instanceof TFigure) {
            xOffset = -40;
        } else if(nextFigure instanceof LFigure) {
            xOffset = -60;
        } else if(nextFigure instanceof SnakeLeftFigure || nextFigure instanceof SnakeRightFigure || nextFigure instanceof IFigure) {
            xOffset = -50;
        }
        
        for(Block block : nextFigure.getBlocks()) {
            g2d.setColor(block.getMovingColor());
            g2d.fillRect(block.getX() + xOffset, block.getY() + 60, 40, 40);
            
            g2d.setStroke(new BasicStroke(1));
            g2d.setColor(Color.LIGHT_GRAY);
            g2d.drawRect(block.getX() + xOffset, block.getY() + 60, 40, 40);
            g2d.setStroke(oldStroke);
        }
    }
    
    private void init() {
        setPreferredSize(new Dimension(275, 250));
    }

    public void update(Figure nextFigure) {
        this.nextFigure = nextFigure;
        repaint();
    }
}
