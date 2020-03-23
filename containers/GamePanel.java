package containers;

import threads.RenderThread;
import threads.Shared;
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
import java.awt.Image;
import java.awt.Point;
import java.awt.Stroke;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import resources.AudioHelpers;
import threads.EndingThread;

public class GamePanel extends JPanel {

    private final int LEFT = 37;
    private final int UP = 38;
    private final int RIGHT = 39;
    private final int SPACE = 32;
    
    private MainFrame mainFrame;
    private StatusPanel statusPanel;
    
    private RenderThread renderThread;
    
    private EndingThread endingThread = null;
    
    private LinkedList<Figure> figures = new LinkedList<>();
    
    private final Object toRemoveLock = new Object();
    private List<Point> toRemove = new ArrayList<>();

    public GamePanel(MainFrame mainFrame, StatusPanel statusPanel) {
        init(mainFrame, statusPanel);
    }
    
    @Override
    public void paintComponent(Graphics grahpics) {
        super.paintComponent(grahpics);
        
        Graphics2D g2d = (Graphics2D) grahpics;
        Stroke oldStroke = g2d.getStroke();
        
        drawBackgroundImage(g2d);
        drawBackgroundGrid(g2d, oldStroke);
        
        if(Shared.isGameOver()) {
            if(endingThread == null) {
                endingThread = new EndingThread(this);
                repaint();
            }
        }
        
        drawGame(g2d, oldStroke);
        drawHint(g2d, oldStroke);
        
        if(Shared.isGameOver()) {
            drawGameOver(g2d, oldStroke);
        }
    }

    public void handleKeyPressed(int keyCode) {
        boolean repaint = false;
        
        switch(keyCode) {
            case UP: repaint = handleRotation(); break;
            case LEFT: repaint = figures.getFirst().shift(Figure.Shift.LEFT); break;
            case RIGHT: repaint = figures.getFirst().shift(Figure.Shift.RIGHT); break;
            case SPACE: repaint = handleDrop(Shared.getMovingBlocks()); break;
        }
        
        if(repaint) {
            repaint();
        }
    }
    
    private boolean handleRotation() {
        if(!(figures.getFirst() instanceof SquareFigure)) {
            return figures.getFirst().rotate();   
        }
        
        return false;
    }
    
    private boolean handleDrop(List<Block> movingBlocks) {  
        List<Block> movedBlocks = new ArrayList<>();
        
        Figure movedFigure = new Figure(movedBlocks);
        
        for(Block block : movingBlocks) {
            movedBlocks.add(new Block(block.getX(), block.getY() + 1, block.getR(), block.getG(), block.getB(), block.isCenter()));
        }
        
        if(Figure.isVerticalOutOfBounds(movedBlocks) || Figure.collidesWithBlocks(Shared.getFixBlocks(), movedBlocks)) {
            List<Block> toRender = new ArrayList<>();
           
            for(Block block : movedBlocks) {
                toRender.add(new Block(block.getX(), block.getY() -1, block.getR(), block.getG(), block.getB(), block.isCenter()));
            }
            
            Shared.setMovingBlocks(toRender);
            return true;
        } else {
            this.figures.remove();
            this.figures.addFirst(movedFigure);
            return handleDrop(movedBlocks);
        }
    }
    
    private void validateFullRows() {
        List<Integer> rowsToSlice = new ArrayList<>();
        
        for(int i=0 ; i<20 ; i++) {
            int ctr = 0;
            for(Block block : Shared.getFixBlocks()) {
                if(block.getY()/40 == i) {
                    ctr++;
                }
            }
            
            if(ctr == 10) {
                rowsToSlice.add(i);
            }
        }
        
        sliceDown(rowsToSlice);
        
        statusPanel.getScorePanel().update(rowsToSlice.size());
        if(rowsToSlice.size() > 0) {
            try {
                AudioHelpers.playSound(getClass(), "slice_" + rowsToSlice.size() + ".wav", 1.0);
            } catch(Exception e) {
                e.printStackTrace();
            } 
        }
    }
    
    private void sliceDown(List<Integer> rowsToSlice) {
        List<Block> blocksToSlice = new ArrayList<>();
        List<Block> fixBlocks = Shared.getFixBlocks();
        
        for(Integer rowToSlice : rowsToSlice) {
            for(Block block : fixBlocks) {
                if(block.getY() / 40 == rowToSlice) {
                    blocksToSlice.add(block);
                } else if(block.getY() / 40 < rowToSlice) {
                    block.setY(block.getY() + 40);
                }
            }
        }
        
        fixBlocks.removeAll(blocksToSlice);
        Shared.setFixBlocks(fixBlocks);
    }

    public void restart() {
        endingThread = null;
        figures.clear();
        toRemove.clear();
        init(mainFrame, statusPanel);
    }
    
    private void init(MainFrame mainFrame, StatusPanel statusPanel) {
        this.mainFrame = mainFrame;
        this.statusPanel = statusPanel;
        setPreferredSize(new Dimension(400, 800));
        this.renderThread = new RenderThread(this);
        this.figures.add(createRandomFigure());
        this.figures.add(createRandomFigure());
        
        figures.getFirst().setActive();
        statusPanel.getNextPanel().update(this.figures.getLast());
        statusPanel.getNextPanel().repaint();
        statusPanel.getDifficultyPanel().enableDifficulty(false);
    }

    public void moveDown() {
        List<Block> movingBlocks = Shared.getMovingBlocks();
        
        for(Block block : movingBlocks) {
            int movedY = block.getY() + 1;
            block.setY(movedY);
        }
        
        Shared.setMovingBlocks(movingBlocks);

        if(Figure.isVerticalOutOfBounds(Shared.getMovingBlocks()) || Figure.collidesWithBlocks(Shared.getFixBlocks(), Shared.getMovingBlocks())) {
            Shared.addMovingToFix();
            validateFullRows();
            
            figures.removeFirst();
            figures.add(createRandomFigure());
            figures.getFirst().setActive();
            
            statusPanel.getNextPanel().update(figures.getLast());
            
            if(Figure.collidesWithBlocks(Shared.getFixBlocks(), Shared.getMovingBlocks())) {
                Shared.setGameOver(true);
            }
        }
        
        this.repaint();
    }
    
    private void drawGame(Graphics2D g2d, Stroke oldStroke) {
        for(Block block : Shared.getMovingBlocks()) {
            g2d.setStroke(new BasicStroke(2));
            g2d.setColor(Color.BLACK);
            g2d.drawRect(block.getX(), block.getY(), 40, 40);
            g2d.setStroke(oldStroke);
            
            g2d.setColor(block.getMovingColor());
            g2d.fillRect(block.getX(), block.getY(), 40, 40);
        }

        for(Block block : Shared.getFixBlocks()) {
            g2d.setStroke(new BasicStroke(2));
            g2d.setColor(Color.BLACK);
            g2d.drawRect(block.getX(), block.getY(), 40, 40);
            g2d.setStroke(oldStroke);
            
            g2d.setColor(block.getFixColor());
            g2d.fillRect(block.getX(), block.getY(), 40, 40);
        }

    }
    
    private Color getRandomColor() {
        switch(ThreadLocalRandom.current().nextInt(0, 7)) {
            case 0: return Color.RED;
            case 1: return Color.GRAY;
            case 2: return Color.BLUE;
            case 3: return Color.YELLOW;
            case 4: return Color.WHITE;
            case 5: return Color.LIGHT_GRAY;
            case 6: return Color.GREEN;
            default: return Color.BLACK;
        }
    }

    public void addToRemove(Point point) {
        synchronized(toRemoveLock) {
            toRemove.add(point);
        }
    }

    private List<Point> getToRemove() {
        List<Point> result = new ArrayList<>();
        
        synchronized(toRemoveLock) {
            for(Point point : toRemove) {
                result.add(new Point(point.x, point.y));
            }
        }
        
        return result;
    }
    
    private Figure createRandomFigure() {
        Figure result;
        
        switch(ThreadLocalRandom.current().nextInt(0, 6)) {
            case 0: result = new LFigure(); break;
            case 1: result = new SnakeLeftFigure(); break;
            case 2: result = new SquareFigure(); break;
            case 3: result = new TFigure(); break;
            case 4: result = new SnakeRightFigure(); break;
            default: result = new IFigure(); break;
        }
                
        return result;
    }

    public DifficultyPanel getDifficultyPanel() {
        return this.statusPanel.getDifficultyPanel();
    }

    private void drawHint(Graphics2D g2d, Stroke oldStroke) {
        List<Block> movingBlocks = Shared.getMovingBlocks();
        
        if(movingBlocks.size() > 0) {
            List<Block> hintBlocks = fakeDrop(movingBlocks);
        
            for(Block block : hintBlocks) {
                g2d.setStroke(new BasicStroke(2));
                g2d.setColor(new Color(1.0f, 1.0f, 1.0f, 0.1f));
                g2d.drawRect(block.getX(), block.getY(), 40, 40);
                g2d.setStroke(oldStroke);

                g2d.setColor(new Color(0.0f, 1.0f, 0.0f, 0.1f));
                g2d.fillRect(block.getX(), block.getY(), 40, 40);
            }
        }
    }
        
    private List<Block> fakeDrop(List<Block> movingBlocks) {  
        List<Block> movedBlocks = new ArrayList<>();
        
        Figure movedFigure = new Figure(movedBlocks);
        
        for(Block block : movingBlocks) {

            movedBlocks.add(new Block(block.getX(), block.getY() + 1, block.getR(), block.getG(), block.getB(), block.isCenter()));
        }
        
        if(Figure.isVerticalOutOfBounds(movedBlocks) || Figure.collidesWithBlocks(Shared.getFixBlocks(), movedBlocks)) {
            List<Block> toRender = new ArrayList<>();
           
            for(Block block : movedBlocks) {
                toRender.add(new Block(block.getX(), block.getY() -1, block.getR(), block.getG(), block.getB(), block.isCenter()));
            }
            
            return toRender;
        } else {
            this.figures.remove();
            this.figures.addFirst(movedFigure);
            return fakeDrop(movedBlocks);
        }
    }

    private void drawBackgroundGrid(Graphics2D g2d, Stroke oldStroke) {
        for(int y=0 ; y<20 ; y++) {
            for(int x=0 ; x<10 ; x++) {
                g2d.setStroke(new BasicStroke(1));
                g2d.setColor(Color.LIGHT_GRAY);
                g2d.drawRect(x*40, y*40, 40, 40);
                g2d.setStroke(oldStroke);
            }
        }
    }

    private void drawBackgroundImage(Graphics2D g2d) {
        try {
            g2d.setColor(new Color(225, 225, 225));
            g2d.fillRect(0, 0, 10*40, 20*40);
            Image image = ImageIO.read(getClass().getResource("/resources/name.png"));
            g2d.drawImage(image, 177, 10, null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void drawGameOver(Graphics2D g2d, Stroke oldStroke) {
        List<Point> toRemoveSync = getToRemove();
        for(Point point : toRemoveSync) {
            g2d.setColor(new Color(225, 225, 225));
            g2d.fillRect(point.x* 40, point.y * 40, 40, 40);
            
            g2d.setStroke(new BasicStroke(1));
            g2d.setColor(Color.LIGHT_GRAY);
            g2d.drawRect(point.x * 40, point.y * 40, 40, 40);
            g2d.setStroke(oldStroke);
        }
    }
}
