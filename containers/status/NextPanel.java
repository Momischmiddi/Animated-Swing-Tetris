package containers.status;

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
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import threads.Shared;

public class NextPanel extends JPanel {

    private Figure nextFigure;
    
    public NextPanel() {
        setMinimumSize(new Dimension(15 * Shared.blockSize, 15 * Shared.blockSize));
        setMaximumSize(new Dimension(15 * Shared.blockSize, 15 * Shared.blockSize));
    }
        
    @Override
    public void paintComponent(Graphics grahpics) {
        super.paintComponent(grahpics);
                
        Graphics2D g2d = (Graphics2D) grahpics;
        Stroke oldStroke = g2d.getStroke();
        
        int xOffset = 0;
        int yOffset = 0;
        
        if(nextFigure instanceof SquareFigure) {
            xOffset = (int) -(Shared.blockSize * 2.407407);
            yOffset = (int) (Shared.blockSize * 1.48148);
        } else if(nextFigure instanceof TFigure) {
            xOffset = (int) -(Shared.blockSize * 2.03703);
            yOffset = (int) (Shared.blockSize * 1.11111);
        } else if(nextFigure instanceof LFigure) {
            xOffset = (int) -(Shared.blockSize * 2.407407);
            yOffset = (int) (Shared.blockSize * 0.9259);
        } else if(nextFigure instanceof SnakeLeftFigure || nextFigure instanceof SnakeRightFigure) {
            xOffset = (int) -(Shared.blockSize * 2.22222);
            yOffset = (int) (Shared.blockSize * 1.48148);
        } else if(nextFigure instanceof IFigure) {
            xOffset = (int) -(Shared.blockSize * 2.03703);
            yOffset = (int) (Shared.blockSize * 0.55555);
        }
                
        for(Block block : nextFigure.getBlocks()) {
            g2d.setColor(block.getMovingColor());
            g2d.fillRect(block.getX() + xOffset, block.getY() + yOffset, Shared.blockSize, Shared.blockSize);
            
            g2d.setStroke(new BasicStroke(1));
            g2d.setColor(Color.LIGHT_GRAY);
            g2d.drawRect(block.getX() + xOffset, block.getY() + yOffset, Shared.blockSize, Shared.blockSize);
            g2d.setStroke(oldStroke);
        }
    }

    public void setNextFigure(Figure nextFigure) {
        this.nextFigure = nextFigure;
    }
}
