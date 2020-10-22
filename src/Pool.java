import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Pool {

Random random = new Random();


    //Weights for the disjoint function
    private final double C1 = 1, C2 = 1, C3 = 1;

    private List<Species> species = new ArrayList<>();

    //Instantiate a threshold to dictate whether a genome is part of a specific species
    public final int DELTA_THRESHOLD = 1;


    /*
     * A function to compare the current genome to the representatives of each species - this allows us to dictate
     * whether he needs to be placed within a new species altogether, or to be placed within a current species
     */
    public void assignSpecies(Genome genome){
        Boolean done = false;
        //Iterate through the representatives
       for(Species species : species){
           //Add it to existing species if its result of comparison is below the threshold
          if(compatibilityDistance(species.getRepresentative(), genome) < DELTA_THRESHOLD){
              species.getGenome().add(genome);
              done = true;
              break;
          }
        }
       if(!done){
            //Create a new species
               Species newSpecies = new Species();
               this.species.add(newSpecies);
               newSpecies.getGenome().add(genome);
       }

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


    //Calculate the value N of the compatibiliy function, where if the genoems have less than
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
        double delta = (C1 * excess)/N + (C2 * disjoint)/N + C3 * weightDiff;
        return delta;
    }



    public void adjustedFitness(){
        for(Species species : species){
            species.adjustedFitness();
        }
    }




}
