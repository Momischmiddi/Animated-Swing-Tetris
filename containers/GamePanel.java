package containers;

import containers.status.StatusPanel;
import containers.status.DifficultyPanel;
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
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import resources.AudioHelpers;
import threads.BackgroundAudioThread;
import threads.EndingThread;

public class GamePanel extends JPanel {

    private final int LEFT = 37;
    private final int UP = 38;
    private final int RIGHT = 39;
    private final int DOWN = 40;
    private final int SPACE = 32;
    
    private MainFrame mainFrame;
    private StatusPanel statusPanel;
    
    private RenderThread renderThread = null;
    private EndingThread endingThread = null;
    private WinThread winThread = null;
    
    private LinkedList<Figure> figures = new LinkedList<>();
    
    private final Object toRemoveLock = new Object();
    private List<Point> toRemove = new ArrayList<>();
    private List<Point> snakeToRemove = new ArrayList<>();

    public GamePanel(MainFrame mainFrame, StatusPanel statusPanel) {
        init(mainFrame, statusPanel);
    }
    
    @Override
    public void paintComponent(Graphics grahpics) {
        super.paintComponent(grahpics);
        
        Graphics2D g2d = (Graphics2D) grahpics;
        Stroke oldStroke = g2d.getStroke();
        
        if(snakeToRemove.size() == 200) {
            drawBackgroundGrid(g2d, oldStroke);
            return;
        }
        
        if(Shared.gameWon && this.winThread != null) {
            drawBackgroundGrid(g2d, oldStroke);
            drawSnake(g2d, oldStroke);
        } else {
            drawBackgroundImage(g2d);
            drawBackgroundGrid(g2d, oldStroke);

            if(Shared.isGameOver() || Shared.gameWon) {
                if(endingThread == null) {
                    endingThread = new EndingThread(this);
                    repaint();
                }
            }

            drawGame(g2d, oldStroke);
            drawHint(g2d, oldStroke);

            if(Shared.isGameOver() || Shared.gameWon) {
                drawGameOver(g2d, oldStroke);
            }
        }
    }

    public void handleKeyPressed(int keyCode) {
        if(!Shared.isGameOver() && !Shared.gameWon) {
            boolean repaint = false;

            switch(keyCode) {
                case UP: repaint = handleRotation(); break;
                case DOWN: repaint = handleMoveDown(); break;
                case LEFT: repaint = figures.get(0).shift(Figure.Shift.LEFT); break;
                case RIGHT: repaint = figures.get(0).shift(Figure.Shift.RIGHT); break;
                case SPACE: repaint = handleDrop(Shared.getMovingBlocks()); break;
            }

            if(repaint) {
                repaint();
            }
        }
    }
    
    private boolean handleRotation() {
        if(!(figures.get(0) instanceof SquareFigure)) {
            return figures.get(0).rotate();
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
        
        for(int i=0 ; i<Shared.Y_SIZE ; i++) {
            int ctr = 0;
            for(Block block : Shared.getFixBlocks()) {
                if(block.getY()/Shared.blockSize == i) {
                    ctr++;
                }
            }
            
            if(ctr == Shared.X_SIZE) {
                rowsToSlice.add(i);
            }
        }
        
        sliceDown(rowsToSlice);
        
        statusPanel.getScorePanel().updateScore(rowsToSlice.size());
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
                if(block.getY() / Shared.blockSize == rowToSlice) {
                    blocksToSlice.add(block);
                } else if(block.getY() / Shared.blockSize < rowToSlice) {
                    block.setY(block.getY() + Shared.blockSize);
                }
            }
        }
        
        fixBlocks.removeAll(blocksToSlice);
        Shared.setFixBlocks(fixBlocks);
    }

    public void restart() {
        endingThread = null;
        winThread = null;
        Shared.setGameOver(false);
        Shared.gameWon = false;
        figures.clear();
        toRemove.clear();
        snakeToRemove.clear();
        init(mainFrame, statusPanel);
    }
    
    private void init(MainFrame mainFrame, StatusPanel statusPanel) {
        this.mainFrame = mainFrame;
        this.statusPanel = statusPanel;
        setPreferredSize(new Dimension(Shared.blockSize*Shared.X_SIZE, Shared.blockSize*Shared.Y_SIZE));
        this.renderThread = new RenderThread(this);
        this.figures.add(createRandomFigure());
        this.figures.add(createRandomFigure());
        
        figures.get(0).setActive();
        statusPanel.getNextPanelContainer().update(this.figures.getLast());
        statusPanel.getDifficultyPanel().enableDifficulty(false);
    }

    public void moveDown() {
        synchronized(downLock) {
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
                figures.get(0).setActive();

                statusPanel.getNextPanelContainer().update(figures.getLast());

                if(Figure.collidesWithBlocks(Shared.getFixBlocks(), Shared.getMovingBlocks())) {
                    Shared.setGameOver(true);
                    statusPanel.getScorePanel().reset();
                    statusPanel.getDifficultyPanel().reset();
                    renderThread.interrupt();
                }
            }

            this.repaint();
        }
    }
    
    private void drawGame(Graphics2D g2d, Stroke oldStroke) {
        for(Block block : Shared.getMovingBlocks()) {
            g2d.setStroke(new BasicStroke(2));
            g2d.setColor(Color.BLACK);
            g2d.drawRect(block.getX(), block.getY(), Shared.blockSize, Shared.blockSize);
            g2d.setStroke(oldStroke);
            
            g2d.setColor(block.getMovingColor());
            g2d.fillRect(block.getX(), block.getY(), Shared.blockSize, Shared.blockSize);
        }

        for(Block block : Shared.getFixBlocks()) {
            g2d.setStroke(new BasicStroke(2));
            g2d.setColor(Color.BLACK);
            g2d.drawRect(block.getX(), block.getY(), Shared.blockSize, Shared.blockSize);
            g2d.setStroke(oldStroke);
            
            g2d.setColor(block.getFixColor());
            g2d.fillRect(block.getX(), block.getY(), Shared.blockSize, Shared.blockSize);
        }

    }

    public void addSnakeToRemove(Point point) {
        synchronized(toRemoveLock) {
            snakeToRemove.add(point);
        }
    }
    
    public void addToRemove(Point point) {
        synchronized(toRemoveLock) {
            toRemove.add(point);
        }
    }

    private List<Point> getToSnakeRemove() {
        List<Point> result = new ArrayList<>();
        
        synchronized(toRemoveLock) {
            for(Point point : snakeToRemove) {
                result.add(new Point(point.x, point.y));
            }
        }
        
        return result;
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
                g2d.drawRect(block.getX(), block.getY(), Shared.blockSize, Shared.blockSize);
                g2d.setStroke(oldStroke);

                g2d.setColor(new Color(0.0f, 1.0f, 0.0f, 0.1f));
                g2d.fillRect(block.getX(), block.getY(), Shared.blockSize, Shared.blockSize);
            }
        }
    }
        
    private List<Block> fakeDrop(List<Block> movingBlocks) {  
        List<Block> movedBlocks = new ArrayList<>();
                
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
            return fakeDrop(movedBlocks);
        }
    }

    private void drawBackgroundGrid(Graphics2D g2d, Stroke oldStroke) {
        for(int y=0 ; y<Shared.Y_SIZE ; y++) {
            for(int x=0 ; x<Shared.X_SIZE ; x++) {
                g2d.setStroke(new BasicStroke(1));
                g2d.setColor(Color.LIGHT_GRAY);
                g2d.drawRect(x*Shared.blockSize, y*Shared.blockSize, Shared.blockSize, Shared.blockSize);
                g2d.setStroke(oldStroke);
            }
        }
    }

    private void drawBackgroundImage(Graphics2D g2d) {
        try {
            g2d.setColor(new Color(225, 225, 225));
            g2d.fillRect(0, 0, Shared.X_SIZE*Shared.blockSize, Shared.Y_SIZE*Shared.blockSize);
                        
            Image image = ImageIO.read(getClass().getResource("/resources/name.png"));
            
            int imgX = image.getWidth(null);
            int imgY = image.getHeight(null);
            
            int totalWidth = Shared.X_SIZE * Shared.blockSize;
            
            double resizeFactor = ((double) totalWidth) / ((double) imgX);
            
            
            imgX = (int) (imgX * resizeFactor);
            imgY = (int) (imgY * resizeFactor);
                        
            int xLoc = Shared.blockSize * Shared.X_SIZE / 2 - (imgX / 2);
            int yLoc = Shared.blockSize * 3;
            
            g2d.drawImage(image, xLoc, yLoc, imgX, imgY, null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void drawGameOver(Graphics2D g2d, Stroke oldStroke) {
        List<Point> toRemoveSync = getToRemove();
        for(Point point : toRemoveSync) {
            g2d.setColor(new Color(225, 225, 225));
            g2d.fillRect(point.x* Shared.blockSize, point.y * Shared.blockSize, Shared.blockSize, Shared.blockSize);
            
            g2d.setStroke(new BasicStroke(1));
            g2d.setColor(Color.LIGHT_GRAY);
            g2d.drawRect(point.x * Shared.blockSize, point.y * Shared.blockSize, Shared.blockSize, Shared.blockSize);
            g2d.setStroke(oldStroke);
        }
        
        if(toRemoveSync.size() == 200 && Shared.gameWon) {
            winThread = new WinThread(this);
        }
    }

    public BackgroundAudioThread getBackgroundAudioThread() {
        return mainFrame.getBackgroundAudioThread();
    }

    public void showWinningScreen() {
        System.out.println("Showing Winning");
    }

    private void drawSnake(Graphics2D g2d, Stroke oldStroke) {
        List<Point> snakeToRemove = getToSnakeRemove();
        
        for(Point point : snakeToRemove) {
            g2d.setColor(new Color(225, 225, 225));
            g2d.fillRect(point.x* Shared.blockSize, point.y * Shared.blockSize, Shared.blockSize, Shared.blockSize);
            
            g2d.setStroke(new BasicStroke(1));
            g2d.setColor(Color.GREEN);
            g2d.drawRect(point.x * Shared.blockSize, point.y * Shared.blockSize, Shared.blockSize, Shared.blockSize);
            g2d.setStroke(oldStroke);
        }
    }

    public MainFrame getMainFrame() {
        return mainFrame;
    }

    public StatusPanel getStatusPanel() {
        return statusPanel;
    }

    private Object downLock = new Object();
    
    private boolean handleMoveDown() {
        boolean result = false;
        
        synchronized(downLock) {
            List<Block> movingBlocks = Shared.getMovingBlocks();

            for(Block block : movingBlocks) {
                int movedY = block.getY() + 15;
                block.setY(movedY);
            }

            if(!Figure.isVerticalOutOfBounds(movingBlocks) && !Figure.collidesWithBlocks(Shared.getFixBlocks(), movingBlocks)) {
                Shared.setMovingBlocks(movingBlocks);
                result = true;
            }
        }
        
        return result;
    }
}
