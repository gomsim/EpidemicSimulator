public class TestAlgorithm extends Algorithm{

    public TestAlgorithm(String name, int maxTime){
        super(name, maxTime);
    }
    private static final int GRID_SIZE = Main.GRID_SIZE;
    private int[][] grid = new int[GRID_SIZE][GRID_SIZE];
    private int tick = 0;
    public int[][] init() {
        for(int x = 0; x < GRID_SIZE; x++){
            for(int y = 0; y < GRID_SIZE; y++){
                grid[x][y] = 0;
            }
        }
        return grid;
    }
    public void tick() {
        super.tick();
        tick += 1;
        for(int x = 0; x < GRID_SIZE; x++){
            for(int y = 0; y < GRID_SIZE; y++){
                grid[x][y] = tick%3;
            }
        }
    }
}