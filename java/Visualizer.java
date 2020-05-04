import javax.swing.*;
import java.awt.*;

public class Visualizer extends JPanel {

    private volatile boolean showSimulation = true;
    private volatile boolean running;
    private volatile Thread algorithmThread;
    private volatile boolean paused;
    private volatile int tickIntervall = 500;
    private int[][] grid;

    private static final int GRID_SIZE = Main.GRID_SIZE;
    private static final int DIMENSION = 800;

    public Visualizer() {
        setLayout(null);
        setSize(DIMENSION, DIMENSION);
        setMinimumSize(new Dimension(DIMENSION, DIMENSION));
        setMaximumSize(new Dimension(DIMENSION, DIMENSION));
        setPreferredSize(new Dimension(DIMENSION, DIMENSION));
        setBackground(new Color(1f, 1f, 1f));
    }

    public void start(Algorithm algorithm) {
        if (algorithmThread == null) {
            algorithmThread = new Thread(() -> {
                try {
                    grid = algorithm.init();
                    running = true;
                    while (running) {
                        if (!paused)
                            algorithm.tick();
                        if (showSimulation){
                            repaint();
                            Thread.sleep(tickIntervall);
                        }
                        if(algorithm.isDone())
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("Stopped");
                algorithm.closeStream();
            });
            algorithmThread.start();
        }
    }

    public void stop() {
        running = false;
        algorithmThread = null;
        grid = null;
        paused = false;
        repaint();
    }
    public void showSimulation(boolean flag){
        showSimulation = flag;
    }
    public boolean isShowingSimulation(){
        return showSimulation;
    }

    public void setTickIntervall(int intervall){
        tickIntervall = intervall;
    }

    public void pause() {
        paused = true;
    }
    public void resume() {
        paused = false;
    }
    public boolean isPaused(){
        return paused;
    }
    private Color getColor(int c) {
        Color color;
        if(c == 1)
            color = Color.BLUE;
        else if(c == 2)
            color = Color.RED;
        else if(c == 3)
            color = Color.MAGENTA.darker();
        else if(c == 4)
            color = Color.BLACK;
        else
            color = Color.WHITE;
        return color;
    }
  
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        int tileWidth = (getWidth() / GRID_SIZE);
        int tileHeight = (getHeight() / GRID_SIZE);
        if(running){
            for(int x = 0; x < GRID_SIZE; x++){
                for(int y = 0; y < GRID_SIZE; y++){
                    graphics.setColor(getColor(grid[x][y]));
                    graphics.fillRect(x*tileWidth, y*tileHeight, tileWidth, tileHeight);
                }
            }
        }

        //Rendering grid overlay
        graphics.setColor(new Color(0f,0f,0f,0.5f));        
        for(int x = 0; x < GRID_SIZE; x++) {
            graphics.drawLine(x*tileWidth, 0, x*tileWidth, getHeight());
        }
        for(int y = 0; y < GRID_SIZE; y++) {
            graphics.drawLine(0, y*tileHeight, getWidth(), y*tileHeight);
        }
    }
}