public class Main {
    public static final int GRID_SIZE = 100;
    public static void main (String[] args) {
        Window.setLookAndFeel();
        new Window(
            new TestAlgorithm("Test", 100),
            new Longterm("Longterm linear","linear",  10, 0.5, 1, false, 0.2, 1000000),
            new Longterm("Longterm exponential","exponential",   5, 0.5, 1, false, 0.2, 10000),
            new Shortterm("Short Term Social distancing", 25, 0.8, 0.0, 0.01, 0.2, 1000000),
            new Shortterm("Short Term with movement", 25, 0.8, 0.5, 0.5, 0.2, 1000000),
            new AbelianSandpile("Abelian sandpile", 1000000)
        ).pack();
    }
}

//Longterm  (name, growthFunc,   epidemicRate,  infectionRate, mortalityRate, startEmpty,  startFactor, maxTime){
//Shortterm (name, epidemicRate, infectionRate, recoveryRate,  travelRate,    startFactor, maxTime)