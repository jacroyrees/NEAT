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
    private int nodeNumber;
    private int maxInnovationNumber;
    public LinkedHashSet<Integer> nodesNumbers = new LinkedHashSet<>();
    public LinkedHashSet<Integer> connectionNumbers = new LinkedHashSet<>();

    //Instantiate the NEAT.Genome object
    public Genome(){
        connections = new HashMap<>();
        nodes = new HashMap<>();
        this.setAdjustedFitness(rand.nextInt(1000));
        initialStructure();
        this.nodeNumber = 7;
        this.maxInnovationNumber = NEAT_CONFIGURATIONS.globalInnovationNumber;

    }

    private void initialStructure(){
        nodes.put(1, new NodeGene(NodeGene.TYPE.INPUT, 1));
        nodes.put(2, new NodeGene(NodeGene.TYPE.INPUT, 2));
        nodes.put(3, new NodeGene(NodeGene.TYPE.INPUT, 3));
        nodes.put(4, new NodeGene(NodeGene.TYPE.OUTPUT, 4));
        nodes.put(5, new NodeGene(NodeGene.TYPE.OUTPUT, 5));
        nodes.put(6, new NodeGene(NodeGene.TYPE.OUTPUT, 6));
        nodes.put(7, new NodeGene(NodeGene.TYPE.OUTPUT, 7));


        nodesNumbers.add(1);
        nodesNumbers.add(2);
        nodesNumbers.add(3);
        nodesNumbers.add(4);nodesNumbers.add(5);
        nodesNumbers.add(6);
        nodesNumbers.add(7);


        connections.put(1, new ConnectionGene(getNodes().get(1).getId(), getNodes().get(4).getId(), Math.random(), true, 1));
        connections.put(2, new ConnectionGene(getNodes().get(1).getId(), getNodes().get(5).getId(), Math.random(), true, 2));
        connections.put(3, new ConnectionGene(getNodes().get(1).getId(), getNodes().get(6).getId(), Math.random(), true, 3));
        connections.put(4, new ConnectionGene(getNodes().get(1).getId(), getNodes().get(7).getId(), Math.random(), true, 4));

        connections.put(5,new ConnectionGene(getNodes().get(2).getId(), getNodes().get(4).getId(), Math.random(), true, 5));
        connections.put(6,new ConnectionGene(getNodes().get(2).getId(), getNodes().get(5).getId(), Math.random(), true, 6));
        connections.put(7, new ConnectionGene(getNodes().get(2).getId(), getNodes().get(6).getId(), Math.random(), true, 7));
        connections.put(8, new ConnectionGene(getNodes().get(2).getId(), getNodes().get(7).getId(), Math.random(), true, 8));

        connections.put(9, new ConnectionGene(getNodes().get(3).getId(), getNodes().get(4).getId(), Math.random(), true, 9));
        connections.put(10, new ConnectionGene(getNodes().get(3).getId(), getNodes().get(5).getId(), Math.random(), true, 10));
        connections.put(11, new ConnectionGene(getNodes().get(3).getId(), getNodes().get(6).getId(), Math.random(), true, 11));
        connections.put(12, new ConnectionGene(getNodes().get(3).getId(), getNodes().get(7).getId(), Math.random(), true, 12));


        connectionNumbers.add(1);
        connectionNumbers.add(2);
        connectionNumbers.add(3);connectionNumbers.add(4);connectionNumbers.add(5);connectionNumbers.add(6);connectionNumbers.add(7);
        connectionNumbers.add(8);connectionNumbers.add(9);connectionNumbers.add(10);connectionNumbers.add(11);connectionNumbers.add(12);





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
            List<Integer> nodesList = new ArrayList(nodesNumbers);


            int node1ind = nodesList.get(rand.nextInt(nodesList.size()-1)+1);
            int node2ind = nodesList.get(rand.nextInt(nodesList.size()-1)+1);
            NodeGene node1 = nodes.get(node1ind);
            NodeGene node2 = nodes.get(node2ind);
            while(node1.getType() == NodeGene.TYPE.OUTPUT && node2.getType() == NodeGene.TYPE.OUTPUT ||
                    node1.getType() == NodeGene.TYPE.INPUT && node2.getType() == NodeGene.TYPE.INPUT){
                node1ind = nodesList.get(rand.nextInt(nodesList.size()-1)+1);
                node1 = nodes.get(node1ind);
            }

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
            //Cant add connection if all possible connections are taken
            if(getNodes().size() > NEAT_CONFIGURATIONS.MINIMUMNUMBEROFNODES){


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

                    //Check if it exists in all genomes of this generation
                    for (ConnectionGene connection : Pool.totalConnectionsMade) {

                        if ((connection.getIn_node() == node1.getId() && connection.getOut_node() == node2.getId()) ||
                                connection.getIn_node() == node2.getId() && connection.getOut_node() == node1.getId()) {
                            alreadyExists = true;

                            ConnectionGene newCon = new ConnectionGene(reverse ? node2.getId() : node1.getId(), reverse ? node1.getId() : node2.getId(),
                                    randomWeight, true, connection.getInnovation());
                            Pool.totalConnectionsMade.add(newCon);
                            connections.put(newCon.getInnovation(), newCon);
                            connectionNumbers.add(newCon.getInnovation());
                            break;
                        }
                    }
                    if (!alreadyExists) {

                        ConnectionGene newCon = new ConnectionGene(reverse ? node2.getId() : node1.getId(), reverse ? node1.getId() : node2.getId(),
                                randomWeight, true, NEAT_CONFIGURATIONS.globalInnovationNumber++);
                        Pool.totalConnectionsMade.add(newCon);
                        connections.put(newCon.getInnovation(), newCon);
                        connectionNumbers.add(newCon.getInnovation());

                    }


                }
            }
    }





    /*
     * Add a node between a connection, in the process we disable the existing connection and create 2 new nodes
     */

    public void addNode() {



            //get the connection
            List<Integer> connectionList = new ArrayList(connectionNumbers);
            int randomConnection = connectionList.get(rand.nextInt(connectionNumbers.size()-1)+1);
            ConnectionGene con = connections.get(randomConnection);

            //get the node on each of the connection
            NodeGene inNode = nodes.get(con.getIn_node());
            NodeGene outNode = nodes.get(con.getOut_node());
            //set the current connection to false
            con.setEnabled(false);

            //create new middle node

            this.nodeNumber+=1;
            NodeGene middle = new NodeGene(NodeGene.TYPE.HIDDEN, this.nodeNumber);

            //Set the input to join middle
            ConnectionGene inputToMiddle = new ConnectionGene(inNode.getId(), middle.getId(), INITIALNEWWEIGHT, true, NEAT_CONFIGURATIONS.globalInnovationNumber++);
            Pool.totalConnectionsMade.add(inputToMiddle);
            connections.put(inputToMiddle.getInnovation(), inputToMiddle);
            connectionNumbers.add(inputToMiddle.getInnovation());
            ConnectionGene middleToOutput = new ConnectionGene(middle.getId(), outNode.getId(), con.getWeight(), true, NEAT_CONFIGURATIONS.globalInnovationNumber++);
            connections.put(middleToOutput.getInnovation(), middleToOutput);
            connectionNumbers.add(middleToOutput.getInnovation());
            Pool.totalConnectionsMade.add(middleToOutput);

            //Add the node
            nodes.put(this.nodeNumber, middle);
            nodesNumbers.add(this.nodeNumber);




        }





}
