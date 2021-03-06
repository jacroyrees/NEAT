package NEAT;

import java.util.*;

import static java.util.Comparator.comparing;

public class Pool {

    private int currentTotalGenomes = 0;
    private int previousTotalGenomes = 0;

    Random random = new Random();
    public static int globalInnovationNumber = 0;
    public static int globalAdjustedFitness = 0;
    //Weights for the disjoint function

    int timeSinceBetterFitness = 0;

    public static List<ConnectionGene> totalConnectionsMade = new ArrayList<ConnectionGene>();

    public List<Species> getSpecies() {
        return speciesList;
    }

    private List<Species> speciesList = new ArrayList<>();

    //Instantiate a threshold to dictate whether a genome is part of a specific species

    private int generation = 0;
    private int poolStaleness = 0;

    /*
     * A function to compare the current genome to the representatives of each species - this allows us to dictate
     * whether he needs to be placed within a new species altogether, or to be placed within a current species
     */
    public void assignSpecies(Genome genome){
        Boolean done = false;
        //Iterate through the representatives
       for(Species species : speciesList){
           //Add it to existing species if its result of comparison is below the threshold
          if(compatibilityDistance(species.getRepresentative(), genome) < NEAT_CONFIGURATIONS.DELTA_THRESHOLD){
              species.getGenome().add(genome);
              done = true;
              break;
          }
        }
       if(!done){
            //Create a new species
               Species newSpecies = new Species();
               this.speciesList.add(newSpecies);
               newSpecies.getGenome().add(genome);
       }

    }

    public Genome randomGenome(){
        Species randomSpecies = speciesList.get(random.nextInt(speciesList.size()-1)+1);
        Genome randomGenome = randomSpecies.getGenome().get(random.nextInt(randomSpecies.getGenome().size()-1)+1);
        return randomGenome;
    }

    public double globalTotalAdjustedFitness(){
        double totalFitness = 0;
        for(Species species : speciesList){
            totalFitness += species.getAdjustedFitness();
        }
        return (totalFitness);
    }

    public double globalAdjustedFitness(){
        double totalFitness = 0;
        for(Species species : speciesList){
            totalFitness += species.getAdjustedFitness();
        }
        return (totalFitness / NEAT_CONFIGURATIONS.POPULATION);
    }

    /*
     * Calculate the number of disjoint genes, where disjoint is the amount of different genes within the two genomes
     * which occur before the end of either of the genomes
     */
    private int disjoint(Genome parent1, Genome parent2) {
        int disjoint =0;
        boolean smaller;
        if(parent1.getConnectionGenes().values().size() < parent2.getConnectionGenes().values().size()) {
            smaller = true;
        }else {
            smaller = false;
        }
        if(smaller){
            for(ConnectionGene gene : parent1.getConnectionGenes().values()){
                if(!parent2.getConnectionGenes().containsKey(gene.getInnovation())){
                    disjoint++;
                }
            }
        }else{
            for(ConnectionGene gene : parent2.getConnectionGenes().values()){
                if(!parent1.getConnectionGenes().containsKey(gene.getInnovation())){
                    disjoint++;
                }
            }
        }

        return disjoint;
    }


    /*
     * Calculate the number of excess genes, where excess is the amount of different genes within the two genomes
     * which occur after the end of either of the genomes
     */
    private int excess(Genome parent1, Genome parent2) {
        int excess = 0;
        boolean smaller; //Which genome is smaller
        if(parent1.getConnectionGenes().values().size() < parent2.getConnectionGenes().values().size()) {
            smaller = true;
        }else {
            smaller = false;
        }if(smaller) {
            excess = parent2.getConnectionGenes().values().size() - parent1.getConnectionGenes().values().size();
        }else{
            excess = parent1.getConnectionGenes().values().size() - parent2.getConnectionGenes().values().size();
        }
        return excess;
    }


    //Calculate the value N of the compatibiliy function, where if the genomes have less than
    //20 genes, the value of N = 1
    private int largerGenome(Genome parent1, Genome parent2){
        if(parent1.getConnectionGenes().values().size() < 20 && parent2.getConnectionGenes().values().size() < 20){
            //Both hold less than 20 therefore normalise to zero
            return 1;
        }
        if(parent1.getConnectionGenes().values().size() > parent2.getConnectionGenes().values().size()){
            //return parent1 size since is largest
            return parent1.getConnectionGenes().values().size();
        }else{
            //return parent2 size since is largest
            return parent2.getConnectionGenes().values().size();
        }
    }



    //Calculate the over all weight difference of both the genomes
    private double weightDiff(Genome g1, Genome g2){
        double weightDiff = 0;
        for(ConnectionGene con : g1.getConnectionGenes().values()){
            if(g2.getConnectionGenes().containsValue(con.getInnovation())){
                double weight = con.getWeight() - g2.getConnectionGenes().get(con.getInnovation()).getWeight();
                weightDiff += weight;
            }
        }
        return weightDiff;
    }


    //Function to check whether the two genomes are part of the same species (e.g. sexually compatible)
    public double compatibilityDistance(Genome g1, Genome g2){
        int N = largerGenome(g1, g2);
        int excess = excess(g1,g2);
        int disjoint = disjoint(g1, g2);
        double weightDiff = weightDiff(g1, g2);
        double delta = (NEAT_CONFIGURATIONS.C1 * excess)/N + (NEAT_CONFIGURATIONS.C2 * disjoint)/N + NEAT_CONFIGURATIONS.C3 * weightDiff;
        return delta;
    }



    public void adjustedFitness(){
        for(Species species : speciesList){
            species.adjustedFitness();
        }
    }

    public int totalGenomes(){
        int totalGenomes = 0;
        for(Species species : speciesList){
            totalGenomes+= species.getGenome().size();
        }
        previousTotalGenomes = currentTotalGenomes;
        currentTotalGenomes = totalGenomes;
        return totalGenomes;
    }


    public void breedNewGeneration(){

            for(Species species : speciesList) {




                species.reproduce(10);

            }

           if(Math.random() <= NEAT_CONFIGURATIONS.INTERSPECIESMATING){
                Species randomSpecies1 = speciesList.get(random.nextInt(speciesList.size()-1)+1);
                Genome randomGenome1 = randomSpecies1.getGenome().get(random.nextInt(randomSpecies1.getGenome().size()-1)+1);
                Species randomSpecies2 = speciesList.get(random.nextInt(speciesList.size()-1)+1);
                Genome randomGenome2 = randomSpecies2.getGenome().get(random.nextInt(randomSpecies2.getGenome().size()-1)+1);

                //Place child in both
                Genome child = randomSpecies1.crossOver(randomGenome1, randomGenome2);
                child.setAdjustedFitness(random.nextInt(1000));
                randomSpecies2.getGenome().add(child);

            }
            if(totalGenomes() < previousTotalGenomes){
                int newGenomes = previousTotalGenomes - totalGenomes();
                int i = 0;
                while(i < newGenomes){
                    Genome genome = new Genome();
                    assignSpecies(genome);
                    i++;
                }
            }


            for( Species species : speciesList){
                species.setRepresentative(species.getGenome().get(random.nextInt(species.getGenome().size()-1)+1));
            }

    }

    public void initialisePool(){
        for(int i = 0; i < NEAT_CONFIGURATIONS.INITIAL_SPECIES_NUMBER;i++){
            Species species = new Species();
            speciesList.add(species);
            for(int j = 0; j < NEAT_CONFIGURATIONS.INITIAL_SPECIES_GENOME_NUMBER;j++){
                speciesList.get(i).getGenome().add(new Genome());

            }
        }
    }


    public void speciesStaleness(){
        speciesList.sort(comparing(Species::getAdjustedFitness).reversed());
        if(timeSinceBetterFitness == 20){
            for(int i = 0; i <speciesList.size()-2;i++){
                speciesList.remove(i);
            }
        }
    }






}
