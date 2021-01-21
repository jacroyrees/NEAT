package NEAT;

public class NEAT_CONFIGURATIONS {


    public static final double C1 = 1, C2 = 1, C3 = 1;

    public static final int DELTA_THRESHOLD = 3;

    public static final int POPULATION = 50;

    public static final double CULL_PROB = 0.2;

    public static final double ASEXUAL_CROSSOVER_PROB = 0.25;

    private final float SIGMOID_CONSTANT = 4.9f;

    public static final double INTERSPECIESMATING = 0.001;

    public static final int MINIMUMNUMBEROFNODES = 7;

    public static final int INITIAL_SPECIES_NUMBER = 10;

    public static final int INITIAL_SPECIES_GENOME_NUMBER = 10;

    public static int globalInnovationNumber = 12;

    static float sigmoid(float x){
        final float SIGMOID_CONSTANT = 4.9f;
        return (float)(1/(1+Math.exp(-SIGMOID_CONSTANT * x)));
    }

}
