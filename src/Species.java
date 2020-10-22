import java.util.*;

public class Species {


    public void setRepresentative(Genome representative) {
        this.representative = representative;
    }

    /*Instantiate a genome which is one of the genomes in the species - this is to compare the genome to new genomes to
     *dictate whether the genome should be placed in this species, or another species
     */
    private Genome representative;

    Random rand = new Random();

    private double cullRatio = 0.4;


    public Species(){

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
                } else {
                    child.addConnectionGene(connectionGene);
                }
            }
            for (ConnectionGene connectionGene : parent2.getConnectionGenes().values()) {
                if (parent1.getConnectionGenes().containsKey(connectionGene.getInnovation())) {
                    child.addConnectionGene(rand.nextBoolean() ? connectionGene : parent1.getConnectionGenes().get(connectionGene.getInnovation()));
                } else {
                    child.addConnectionGene(connectionGene);
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

        genome.add(child);
        return child;
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







}




