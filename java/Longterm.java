import java.util.HashSet;
import java.util.Random;
import java.util.function.*;
import java.awt.Point;
import java.util.LinkedList;
import java.util.ArrayList;

public class Longterm extends Algorithm {
    
    private boolean startEmpty;
    private Function<Integer,Integer> growthFunc;

    private double infectionRate;
    private double mortalityRate;
    private double startFactor;
    private int epidemicRate;
    
    private int population;

    private ArrayList<Point> toDie = new ArrayList<>();
    private ArrayList<Point> toLive = new ArrayList<>();

    private static final int EMPTY = 0, HEALTHY = 1, SICK = 2;

    Random rnd = new Random(); 
    private static final int GRID_SIZE = Main.GRID_SIZE;
    private int[][] grid = new int[GRID_SIZE][GRID_SIZE];
    
    public Longterm(String name, String growthFunc, int epidemicRate, double infectionRate, double mortalityRate, boolean startEmpty, double startFactor, int maxTime){
        super(name, maxTime);
        this.epidemicRate = epidemicRate;
        this.infectionRate = infectionRate;
        this.mortalityRate = mortalityRate;
        this.startFactor = startFactor;
        this.startEmpty = startEmpty;
        switch(growthFunc){
            case "linear": this.growthFunc = (pop) -> 10; break;
            case "exponential": this.growthFunc = (pop) -> {return pop == 0 ? 20 : (int)(pop*0.005);}; break;
        }
    }
    
    public int[][] init(){
        super.openOutputStream("size", "tick");
        if (startEmpty){
            for(int x = 0; x < GRID_SIZE; x++){
                for(int y = 0; y < GRID_SIZE; y++){
                    grid[x][y] = EMPTY;
                }
            }
        }else {
            spawnCells((int)(GRID_SIZE*GRID_SIZE*startFactor));
        }
        return grid;
    }

    private void spawnCells(int numberOfCells){
        numberOfCells = numberOfCells > GRID_SIZE*GRID_SIZE - population ? GRID_SIZE*GRID_SIZE - population: numberOfCells; 
        population += numberOfCells;
        //System.out.println("Spawning cells: " + numberOfCells);
        while(numberOfCells > 0){
            int x = rnd.nextInt(GRID_SIZE);
            int y = rnd.nextInt(GRID_SIZE);
            if(grid[x][y] == EMPTY){
                grid[x][y] = HEALTHY;
                numberOfCells--;               
            }
        }
    }
    private void resolveEpidemic(){
        int x = rnd.nextInt(GRID_SIZE);
        int y = rnd.nextInt(GRID_SIZE);

        HashSet<Point> visited = new HashSet<>();
        LinkedList<Point> infected = new LinkedList<>();
        infected.add(new Point(x,y));
        while(!infected.isEmpty()){
            Point current = infected.remove();
            if(!visited.contains(current)){
                visited.add(current);
                infect(current.x, current.y-1, toDie, infected);
                infect(current.x, current.y+1, toDie, infected);
                infect(current.x-1, current.y, toDie, infected);
                infect(current.x+1, current.y, toDie, infected);

                infect(current.x-1, current.y-1, toDie, infected);
                infect(current.x+1, current.y+1, toDie, infected);
                infect(current.x-1, current.y+1, toDie, infected);
                infect(current.x+1, current.y-1, toDie, infected);
            }
        }
    }

    private void infect(int x, int y, ArrayList<Point> toDie,LinkedList<Point> infected){
        if(x < GRID_SIZE && x >= 0 && y < GRID_SIZE && y >= 0){
            if(grid[x][y] == HEALTHY && Math.random() < infectionRate){
                grid[x][y] = SICK;
                Point sickCell = new Point(x, y);
                infected.add(sickCell);
                if(Math.random() < mortalityRate)
                    toDie.add(sickCell);
                else
                    toLive.add(sickCell);
            }
        }
    }

    public void kill(){
        population -= toDie.size();
        for(Point p : toDie)
            grid[p.x][p.y] = EMPTY;
        toDie.clear();
    }
    public void cure(){
        for(Point p : toLive)
            grid[p.x][p.y] = HEALTHY;
        toLive.clear();
    }

    public void tick(){
        super.tick();
        if (!toDie.isEmpty() || !toLive.isEmpty()){
            logOutput(toDie.size() + toLive.size(), timePassed);
            kill();
            cure();
        }
        int growth = growthFunc.apply(population);
        spawnCells(growth);
        if (timePassed % epidemicRate == 0){
            resolveEpidemic();
        }
    }
}