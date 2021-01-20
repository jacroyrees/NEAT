package NEAT;

import java.util.*;

public class Genome {


    static Random rand = new Random();


    //get the fitness of the genome
    public int getFitness() {
        return fitness;
    }

    //set the fitness
    public void setFitness(int fitness) {
        this.fitness = fitness;
    }

    private int fitness;

    private Map<Integer, ConnectionGene> connections; //What connection gene the NEAT.Genome has
    private Map<Integer, NodeGene> nodes; //What node genes the NEAT.Genome has
    private double weight = 0.04; //What is the weight
    private double INITIALNEWWEIGHT = 1; //
    private double adjustedFitness = 0;





    //Instantiate the NEAT.Genome object
    public Genome(){
        connections = new HashMap<>();
        nodes = new HashMap<>();
        this.fitness = 0;
        initialStructure();


    }

    private void initialStructure(){
        nodes.put(0, new NodeGene(NodeGene.TYPE.INPUT, 999));
        nodes.put(1, new NodeGene(NodeGene.TYPE.INPUT, 999));
        nodes.put(2, new NodeGene(NodeGene.TYPE.INPUT, 999));
        nodes.put(3, new NodeGene(NodeGene.TYPE.OUTPUT, 1000));
        nodes.put(4, new NodeGene(NodeGene.TYPE.OUTPUT, 1000));
        nodes.put(5, new NodeGene(NodeGene.TYPE.OUTPUT, 1000));
        nodes.put(6, new NodeGene(NodeGene.TYPE.OUTPUT, 1000));
        connections.put(0, new ConnectionGene(getNodes().get(0).getId(), getNodes().get(3).getId(), Math.random(), true, 0));
        connections.put(1, new ConnectionGene(getNodes().get(0).getId(), getNodes().get(4).getId(), Math.random(), true, 0));
        connections.put(2, new ConnectionGene(getNodes().get(0).getId(), getNodes().get(5).getId(), Math.random(), true, 0));
        connections.put(3, new ConnectionGene(getNodes().get(0).getId(), getNodes().get(6).getId(), Math.random(), true, 0));

        connections.put(4,new ConnectionGene(getNodes().get(1).getId(), getNodes().get(3).getId(), Math.random(), true, 0));
        connections.put(5,new ConnectionGene(getNodes().get(1).getId(), getNodes().get(4).getId(), Math.random(), true, 0));
        connections.put(6, new ConnectionGene(getNodes().get(1).getId(), getNodes().get(5).getId(), Math.random(), true, 0));
        connections.put(7, new ConnectionGene(getNodes().get(1).getId(), getNodes().get(6).getId(), Math.random(), true, 0));

        connections.put(8, new ConnectionGene(getNodes().get(2).getId(), getNodes().get(3).getId(), Math.random(), true, 0));
        connections.put(9, new ConnectionGene(getNodes().get(2).getId(), getNodes().get(4).getId(), Math.random(), true, 0));
        connections.put(10, new ConnectionGene(getNodes().get(2).getId(), getNodes().get(5).getId(), Math.random(), true, 0));
        connections.put(11, new ConnectionGene(getNodes().get(2).getId(), getNodes().get(6).getId(), Math.random(), true, 0));
    }

    //Set the adjusted fitness (Full Explanation -> NEAT.Pool)
    public void setAdjustedFitness(int x){
        adjustedFitness = x;
    }

    public double getAdjustedFitness(){
        return adjustedFitness;
    }

    public Map<Integer, ConnectionGene> getConnectionGenes(){
        return connections;
    }

    public Map<Integer, NodeGene> getNodeGenes(){
        return nodes;
    }


    public void setConnections(Map<Integer, ConnectionGene> connections) {
        this.connections = connections;
    }

    public Map<Integer, NodeGene> getNodes() {
        return nodes;
    }

    public void setNodes(Map<Integer, NodeGene> nodes) {
        this.nodes = nodes;
    }

    public void addNodeGene(NodeGene gene){
        nodes.put(gene.getId(), gene);
    }

    public void addConnectionGene(ConnectionGene gene){
        connections.put(gene.getInnovation(), gene);
    }



    /*
     * Add a connection to a a tuple of 2 random nodes which arent connected
     */
    public void addConnection(){


            //Randomly get 2 nodes from the network
            NodeGene node1 = nodes.get(rand.nextInt(nodes.size()));
            NodeGene node2 = nodes.get(rand.nextInt(nodes.size()));
            /*Boolean which dictates whether the node is viable
             * checks whether it is HIDDEN -> IN / OUT -> HIDDEN / OUT -> IN
             * returns true if the connection needs to go in reverse
             */
            boolean reverse = false;
            if (node1.getType() == NodeGene.TYPE.HIDDEN && node2.getType() == NodeGene.TYPE.INPUT) {
                reverse = true;
            } else if (node1.getType() == NodeGene.TYPE.OUTPUT && node2.getType() == NodeGene.TYPE.HIDDEN) {
                reverse = true;
            } else if (node1.getType() == NodeGene.TYPE.OUTPUT && node2.getType() == NodeGene.TYPE.INPUT) {
                reverse = true;
            }
            /*
             * Check whether the connection already exists within the genome
             */
            boolean conExists = false;
            for (ConnectionGene connection : connections.values()) {
                if (connection.getIn_node() == node1.getId() && connection.getOut_node() == node2.getId()) {
                    conExists = true;
                    break; //connectionAlreadyExists
                } else if (connection.getIn_node() == node2.getId() && connection.getOut_node() == node1.getId()) {
                    conExists = true;
                    break;//ConnectionAlreadyExists
                }
            }
            /*
             * If the connection doesn't exist in this genome, we can create the connection in order,
             * depending on whether it needs to be reversed or not
             */
            if (!conExists) {
                boolean alreadyExists = false;
                double randomWeight = Math.random();
                for(ConnectionGene connection : connections.values()) {

                    if ((connection.getIn_node() == node1.getId() && connection.getOut_node() == node2.getId()) ||
                            connection.getIn_node() == node2.getId() && connection.getOut_node() == node1.getId()) {
                        alreadyExists = true;

                        ConnectionGene newCon = new ConnectionGene(reverse ? node2.getId() : node1.getId(), reverse ? node1.getId() : node2.getId(),
                                randomWeight, true, connection.getInnovation());
                        Pool.totalConnectionsMade.add(newCon);
                        connections.put(newCon.getInnovation(), newCon);
                    }
                }
                if(!alreadyExists){

                        ConnectionGene newCon = new ConnectionGene(reverse ? node2.getId() : node1.getId(), reverse ? node1.getId() : node2.getId(),
                                randomWeight, true, Pool.globalInnovationNumber++);
                        Pool.totalConnectionsMade.add(newCon);
                    Pool.totalConnectionsMade.add(newCon);
                    connections.put(newCon.getInnovation(), newCon);

                }



            }

    }



    /*
     * Add a node between a connection, in the process we disable the existing connection and create 2 new nodes
     */

    public void addNode() {

        //Add a node if a random double between 0 and 1 is greater than 1 divided by nodes.size() -> Use this as the probability

            //get the connection
            ConnectionGene con = connections.get(rand.nextInt(connections.size()));
            //get the node on each of the connection
            NodeGene inNode = nodes.get(con.getIn_node());
            NodeGene outNode = nodes.get(con.getOut_node());
            //set the current connection to false
            con.setEnabled(false);

            //create new middle node
            NodeGene middle = new NodeGene(NodeGene.TYPE.HIDDEN, nodes.size());
            //Set the input to join middle
            ConnectionGene inputToMiddle = new ConnectionGene(inNode.getId(), middle.getId(), INITIALNEWWEIGHT, true,Pool.globalInnovationNumber);
            Pool.totalConnectionsMade.add(inputToMiddle);
            ConnectionGene middleToOutput = new ConnectionGene(middle.getId(), outNode.getId(), con.getWeight(), true, Pool.globalInnovationNumber);
            Pool.totalConnectionsMade.add(middleToOutput);

            //Add the node
            nodes.put(middle.getId(), middle);

            //Add the connection
            connections.put(inputToMiddle.getInnovation(), inputToMiddle);
            connections.put(middleToOutput.getInnovation(), middleToOutput);
        }





}
