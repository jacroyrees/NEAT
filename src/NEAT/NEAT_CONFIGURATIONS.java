package NEAT;

public class NEAT_CONFIGURATIONS {


    public static final double C1 = 1, C2 = 1, C3 = 1;

    public static final int DELTA_THRESHOLD = 3;

    public static final int POPULATION = 50;

    public static final double CULL_PROB = 0.2;

    public static final double ASEXUAL_CROSSOVER_PROB = 0.25;

    private final float SIGMOID_CONSTANT = 4.9f;


    static float sigmoid(float x){
        final float SIGMOID_CONSTANT = 4.9f;
        return (float)(1/(1+Math.exp(-SIGMOID_CONSTANT * x)));
    }

}
