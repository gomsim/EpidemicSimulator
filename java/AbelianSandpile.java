import java.awt.*;
import java.util.*;

public class AbelianSandpile extends Algorithm{

    private Random rnd = new Random();

    public AbelianSandpile(String name, int maxTime){
        super(name, maxTime);
    }

    int[][] grid;

    public int[][] init(){
        super.openOutputStream("size", "tick");
        return grid = new int[Main.GRID_SIZE][Main.GRID_SIZE];
    }

    public void tick() {
        super.tick();
        //Replace with Main.GRID_SIZE/2 to always drop grain in centre of grid.
        int x = /*Main.GRID_SIZE/2;*/rnd.nextInt(Main.GRID_SIZE-1);
        int y = /*Main.GRID_SIZE/2;*/rnd.nextInt(Main.GRID_SIZE-1);

        LinkedList<Point> queue = new LinkedList<>();
        queue.add(new Point(x,y));
        int rasSize = 0;
        while(!queue.isEmpty()){
            Point current;
            do{
                current = queue.remove();
            }while(!queue.isEmpty() && (current.x < 0 || current.x >= Main.GRID_SIZE || current.y < 0 || current.y >= Main.GRID_SIZE));
            if ((current.x < 0 || current.x >= Main.GRID_SIZE || current.y < 0 || current.y >= Main.GRID_SIZE))
                continue;
            rasSize++;
            if(++grid[current.x][current.y] >= 4){
                grid[current.x][current.y] -= 4;
                queue.add(new Point(current.x, current.y-1));
                queue.add(new Point(current.x, current.y+1));
                queue.add(new Point(current.x-1, current.y));
                queue.add(new Point(current.x+1, current.y));
            }
        }
        logOutput(rasSize,timePassed);
    }
}