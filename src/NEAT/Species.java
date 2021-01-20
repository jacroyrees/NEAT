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
    double portionToCull;

    private double adjustedFitness = 0;


    public Species(){

    }

    public double getTotalAdjustedFitness(){
        double totalFitness = 0;
        for(Genome genome : genome){
            totalFitness += genome.getAdjustedFitness();
        }
        return totalFitness;
    }


    public double getAdjustedFitness(){
        double totalFitness = 0;
        for(Genome genome : genome){
            totalFitness += genome.getAdjustedFitness();
        }
        adjustedFitness = (totalFitness / genome.size());
        return adjustedFitness;
    }

    public Genome getRepresentative(){
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
        boolean parent1Bigger;
        Genome child = new Genome();
        //Flip the parents around
        if (parent1.getFitness() < parent2.getFitness()) {
            Genome temp = parent1;
            parent1 = parent2;
            parent2 = temp;
        }
        //Case of equal fitness -> Take nodes from the parent with more nodes
        if (parent1.getFitness() == parent2.getFitness()) {
            //Copy in every node (will overwrite)
            for (NodeGene parent2Node : parent2.getNodes().values()) {
                child.addNodeGene(parent2Node.copy());
            }
            for (NodeGene parent1Node : parent1.getNodes().values()) {
                child.addNodeGene(parent1Node.copy());
            }
            //Add both connection genes
            for (ConnectionGene connectionGene : parent1.getConnectionGenes().values()) {
                if (parent2.getConnectionGenes().containsKey(connectionGene.getInnovation())) {
                    child.addConnectionGene(rand.nextBoolean() ? connectionGene : parent2.getConnectionGenes().get(connectionGene.getInnovation()));
                    child.getConnectionGenes().get(connectionGene.getInnovation()).setEnabled(disabledGeneProb(connectionGene));
                } else {
                    child.addConnectionGene(connectionGene);
                    child.getConnectionGenes().get(connectionGene.getInnovation()).setEnabled(disabledGeneProb(connectionGene));
                }
            }
            for (ConnectionGene connectionGene : parent2.getConnectionGenes().values()) {
                if (parent1.getConnectionGenes().containsKey(connectionGene.getInnovation())) {
                    child.addConnectionGene(rand.nextBoolean() ? connectionGene : parent1.getConnectionGenes().get(connectionGene.getInnovation()));
                    child.getConnectionGenes().get(connectionGene.getInnovation()).setEnabled(disabledGeneProb(connectionGene));
                } else {
                    child.addConnectionGene(connectionGene);
                    child.getConnectionGenes().get(connectionGene.getInnovation()).setEnabled(disabledGeneProb(connectionGene));
                }
            }
        } else {
            //Parent 1 fitter
            for (NodeGene parent1Node : parent1.getNodes().values()) {
                child.addNodeGene(parent1Node.copy());
            }
            for (ConnectionGene connectionGene : parent1.getConnectionGenes().values()) {
                if (parent2.getConnectionGenes().containsKey(connectionGene.getInnovation())) {
                    child.addConnectionGene(rand.nextBoolean() ? connectionGene : parent2.getConnectionGenes().get(connectionGene.getInnovation()));
                } else {
                    child.addConnectionGene(connectionGene);
                }
            }
        }

        mutate(child);
        genome.add(child);
        return child;
    }

    public void setRepresentative(){
        representative = genome.get(rand.nextInt(genome.size()));
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
        List<Genome> newGeneration = aSexualReproduction();


        if(genome.get(0).getNodes().size() > 5){
            newGeneration.add(genome.get(0));
            genome.remove(0);
        }

        getGenome().sort(comparing(Genome::getAdjustedFitness));

        cullGenomes();
        for(int i = 0; i < populationAssigned;i++){

            Genome randomGenome1 = getGenome().get(rand.nextInt(getGenome().size()));
            Genome randomGenome2 = getGenome().get(rand.nextInt(getGenome().size()));

            while(randomGenome1 == randomGenome2){
                randomGenome1 = getGenome().get(rand.nextInt(getGenome().size()));
                randomGenome2 = getGenome().get(rand.nextInt(getGenome().size()));
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
    }


    private List<Genome> aSexualReproduction(){
        List<Genome> newGeneration = new ArrayList();
        int numberOfASexualCrossOver = (int)Math.floor(genome.size() * NEAT_CONFIGURATIONS.ASEXUAL_CROSSOVER_PROB);
        for(int i = 0; i < numberOfASexualCrossOver;i++){

            //Get a genome for Asexual reproduction
            Genome g1 = getGenome().get(i);

            getGenome().remove(i);


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

        return aSexualReproduction();
    }


}




