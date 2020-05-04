import java.util.Random;
import java.awt.Point;

public class Shortterm extends Algorithm {

    private double epidemicRate;
    private double infectionRate;
    private double recoveryRate;
    private double travelRate;
    private int population;

    private int spreadSize;

    private static final int EMPTY = 0, HEALTHY = 1, SICK = 2;

    Random rnd = new Random(); 
    private static final int GRID_SIZE = Main.GRID_SIZE;
    private int[][] grid; 
    
    public Shortterm(String name, double epidemicRate, double infectionRate, double recoveryRate, double travelRate, double startFactor, int maxTime){
        super(name, maxTime);
        this.epidemicRate = epidemicRate;
        this.infectionRate = infectionRate;
        this.recoveryRate = recoveryRate;
        this.travelRate = travelRate;
        this.population = (int)(GRID_SIZE*GRID_SIZE*startFactor);
    }
    
    public int[][] init(){
        super.openOutputStream("size", "tick");
        grid = new int[GRID_SIZE][GRID_SIZE];
        spawnCells(population);
        return grid;
    }

    private void spawnCells(int numberOfCells){
        while(numberOfCells > 0){
            int x = rnd.nextInt(GRID_SIZE);
            int y = rnd.nextInt(GRID_SIZE);
            if(grid[x][y] == EMPTY){
                grid[x][y] = HEALTHY;
                numberOfCells--;               
            }
        }
        int x = rnd.nextInt(GRID_SIZE);
        int y = rnd.nextInt(GRID_SIZE);
        grid[x][y] = SICK;
    }
    private void update(){
        int[][] newGrid = new int[GRID_SIZE][GRID_SIZE];
        for(int x = 0; x < GRID_SIZE; x++){
            for(int y = 0; y < GRID_SIZE; y++){
                if(grid[x][y] == HEALTHY){
                    boolean infected = false;
                    for(int ox = x-1; ox <= x+1; ox++){
                        for(int oy = y-1; oy <= y+1; oy++){
                            if(!(oy == y && ox == x) && oy >= 0 && oy < GRID_SIZE && ox >= 0 && ox < GRID_SIZE ){
                                if(grid[ox][oy] == SICK){
                                    if (Math.random() < infectionRate)
                                        infected = true;
                                }
                            }
                        }
                    }
                    if(infected){
                        newGrid[x][y] = SICK;
                        spreadSize++;
                    }else{
                        newGrid[x][y] = HEALTHY;
                    }
                }
                else if(grid[x][y] == SICK){
                    newGrid[x][y] = Math.random() < recoveryRate ? HEALTHY : SICK;
                }
            }
        }
        for(int x = 0; x < GRID_SIZE; x++){
            for(int y = 0; y < GRID_SIZE; y++){
                if (Math.random() < travelRate){
                    Point dest = findEmptyCell(newGrid);
                    newGrid[dest.x][dest.y] = newGrid[x][y];
                    newGrid[x][y] = EMPTY;
                }
            }
        }
        for(int x = 0; x < GRID_SIZE; x++){
            for(int y = 0; y < GRID_SIZE; y++){
                grid[x][y] = newGrid[x][y];
            }
        }
    }
    private int cureAll(){
        int cured = 0;
        for(int x = 0; x < GRID_SIZE; x++){
            for(int y = 0; y < GRID_SIZE; y++){
                if (grid[x][y] == SICK){
                    grid[x][y] = HEALTHY;
                    cured++;
                }
            }
        }
        return cured;
    }

    private Point findEmptyCell(int[][] newGrid){
        while(true){
            int x = rnd.nextInt(GRID_SIZE);
            int y = rnd.nextInt(GRID_SIZE);
            if(newGrid[x][y] == EMPTY)
                return new Point(x,y);
        }
    }

    public void tick(){
        super.tick();
        update();
        if (++timePassed % epidemicRate == 0){
            if (spreadSize != 0)
                logOutput(spreadSize, timePassed);
            cureAll();
            spreadSize = 0;
            int x = rnd.nextInt(GRID_SIZE);
            int y = rnd.nextInt(GRID_SIZE);
            grid[x][y] = grid[x][y] == HEALTHY ? SICK:grid[x][y];
        }
    }
}