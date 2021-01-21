package NEAT;

import java.util.*;

import static java.util.Comparator.comparing;

public class Species {


    public void setRepresentative(Genome representative) {
        this.representative = representative;
    }

    /*Instantiate a genome which is one of the genomes in the species - this is to compare the genome to new genomes to
     *dictate whether the genome should be placed in this species, or another species
     */
    private Genome representative;

    Random rand = new Random();


    private double adjustedFitness = 0;


    public Species() {

    }

    public double getTotalAdjustedFitness() {
        double totalFitness = 0;
        for (Genome genome : genome) {
            totalFitness += genome.getAdjustedFitness();
        }
        return totalFitness;
    }


    public double getAdjustedFitness() {
        double totalFitness = 0;
        for (Genome genome : genome) {
            totalFitness += genome.getAdjustedFitness();
        }
        adjustedFitness = (totalFitness / genome.size());
        return adjustedFitness;
    }

    public Genome getRepresentative() {
        return representative;
    }


    public List<Genome> getGenome() {
        return genome;
    }


    public void setGenome(List<Genome> genome) {
        this.genome = genome;
    }


    private List<Genome> genome = new ArrayList<>();


    /*
     * Cross over two parent genomes to create offspring with traits from both,
     * and mutated genes taken from the fitter parent
     */
    public Genome crossOver(Genome parent1, Genome parent2) {
        Genome child = new Genome();
        //Case if parent1 is best
        if (parent1.getAdjustedFitness() < parent2.getAdjustedFitness()) {
            Genome temp = parent1;
            parent1 = parent2;
            parent2 = temp;
        }
        if (!(parent1.getAdjustedFitness() == parent2.getAdjustedFitness())) {

            for(NodeGene gene : parent1.getNodes().values()){
                if(parent2.getNodes().containsValue(gene.getId())){
                    child.addNodeGene(rand.nextBoolean() ? gene.copy() : parent2.getNodes().get(gene.getId()).copy());

                }else{
                    child.addNodeGene(gene.copy());
                }

            }

            for(ConnectionGene gene : parent1.getConnectionGenes().values()){
                if(parent2.getConnectionGenes().containsValue(gene.getInnovation())){
                    child.addConnectionGene(rand.nextBoolean() ? gene.copy() : parent2.getConnectionGenes().get(gene.getInnovation()).copy());
                }else{
                    child.addConnectionGene(gene.copy());
                }
            }




        } else{

            //case if theyre identical fitnesses

            if (parent1.getConnectionGenes().size() < parent2.getConnectionGenes().size()) {
                Genome temp = parent1;
                parent1 = parent2;
                parent2 = temp;
            }
            for (NodeGene gene : parent1.getNodes().values()) {
                if (parent2.getNodes().containsValue(gene.getId())) {
                    child.addNodeGene(rand.nextBoolean() ? gene.copy() : parent2.getNodes().get(gene.getId()).copy());
                } else {
                    if (Math.random() > 0.5) {
                        child.addNodeGene(gene.copy());
                    }
                }
            }
            for (NodeGene gene : parent2.getNodes().values()) {
                if (!parent1.getNodes().containsValue(gene.getId())) {
                    if (Math.random() > 0.5) {
                        child.addNodeGene(gene.copy());
                    }
                }
            }

            for (ConnectionGene gene : parent1.getConnectionGenes().values()) {
                if (parent2.getConnectionGenes().containsValue(gene.getInnovation())) {
                    child.addConnectionGene(rand.nextBoolean() ? gene.copy() :  parent2.getConnectionGenes().get(gene.getInnovation()).copy());
                } else {
                    if (Math.random() > 0.5) {
                        child.addConnectionGene(gene.copy());
                    }
                }
            }

            for (ConnectionGene gene : parent2.getConnectionGenes().values()) {
                if (!parent1.getConnectionGenes().containsValue(gene.getInnovation())) {
                    if (Math.random() > 0.5) {
                        child.addConnectionGene(gene.copy());
                    }
                }
            }

        }
        mutate(child);
        return child;
    }




    public void setRepresentative(){
        representative = genome.get(rand.nextInt(genome.size()-1)+1);
    }

    public boolean disabledGeneProb(ConnectionGene connectionGene){
        if(!connectionGene.isEnabled()){
            if(Math.random() > 0.75){
                return true;
            }
        }
        return false;
    }


    /*
     * Method is called from the pool where it iterates through the species and calls this
     * to iterate through the list of genomes within the species to compare it's current fitness with the species on a
     * whole: this is done to ensure one species doesn't take over by simply having 1 extremely proficient genome and
     * the rest are not so proficient
     */
    public void adjustedFitness(){
        for(Genome g : genome){
            g.setAdjustedFitness(g.getFitness() / genome.size());
        }
    }



    public void reproduce(int populationAssigned){
        List<Genome> newGeneration = new ArrayList<>();


        if(genome.get(0).getNodes().size() > 5){
            newGeneration.add(genome.get(0));
            genome.remove(0);
        }
         newGeneration.addAll(aSexualReproduction());

        getGenome().sort(comparing(Genome::getAdjustedFitness));

        cullGenomes();
        for(int i = 0; i < populationAssigned;i++){

            Genome randomGenome1 = getGenome().get(rand.nextInt(getGenome().size()-1)+1);
            Genome randomGenome2 = getGenome().get(rand.nextInt(getGenome().size()-1)+1);

            while(randomGenome1 == randomGenome2){
                randomGenome1 = getGenome().get(rand.nextInt(getGenome().size()-1)+1);
                randomGenome2 = getGenome().get(rand.nextInt(getGenome().size()-1)+1);
            }
            Genome child = crossOver(randomGenome1, randomGenome2);
            newGeneration.add(child);
        }


        genome = newGeneration;
    }

    public void cullGenomes(){
        int numberToCull = (int)Math.floor(getGenome().size() * NEAT_CONFIGURATIONS.CULL_PROB);

        getGenome().sort(comparing(Genome::getAdjustedFitness));

        for(int i = getGenome().size()-numberToCull;i < getGenome().size();i++){
            getGenome().remove(i);
        }
    }

    private void mutate(Genome genome){

        for(ConnectionGene gene : genome.getConnectionGenes().values()){
            if (Math.random() > 0.2) {

                if (Math.random() > 0.1) {

                    gene.setWeight(rand.nextBoolean() ? gene.getWeight() - 0.02 : gene.getWeight() + 0.02);
                }
                if (Math.random() > 0.9) {
                    gene.setWeight(Math.random());
                }
            }
        }
        if(Math.random() <= 0.03){
          genome.addNode();
        }if(Math.random() <= 0.05){
            genome.addConnection();
        }
    }


    private List<Genome> aSexualReproduction(){
        List<Genome> newGeneration = new ArrayList();
        int numberOfASexualCrossOver = (int)Math.floor(genome.size() * NEAT_CONFIGURATIONS.ASEXUAL_CROSSOVER_PROB);
        for(int i = 0; i < numberOfASexualCrossOver;i++){

            //Get a genome for Asexual reproduction
            int randomGenome = rand.nextInt(getGenome().size()-1)+1;
            Genome g1 = getGenome().get(randomGenome);

            getGenome().remove(randomGenome);


            if(Math.random() < 0.25){
                g1.addConnection();
            }else if(Math.random() < 50){
                g1.addNode();
            }else{
                g1.addNode();
                g1.addConnection();
            }

        newGeneration.add(g1);
        }

        return newGeneration;
    }


}




