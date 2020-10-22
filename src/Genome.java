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
    public static int GLOBALINNOVATIONNUMBER; //what innovation comes next
    private Map<Integer, ConnectionGene> connections; //What connection gene the Genome has
    private Map<Integer, NodeGene> nodes; //What node genes the Genome has
    private double weight = 0.04; //What is the weight
    private double INITIALNEWWEIGHT = 1; //
    private double adjustedFitness;


    //Instantiate the Genome object
    public Genome(){
        connections = new HashMap<>();
        nodes = new HashMap<>();
    }

    //Set the adjusted fitness (Full Explanation -> Pool)
    public void setAdjustedFitness(int x){
        adjustedFitness = x;
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
        if(node1.getType() == NodeGene.TYPE.HIDDEN && node2.getType() == NodeGene.TYPE.INPUT){
          reverse = true;
        }else if(node1.getType() == NodeGene.TYPE.OUTPUT && node2.getType() == NodeGene.TYPE.HIDDEN){
            reverse = true;
        }else if(node1.getType() == NodeGene.TYPE.OUTPUT && node2.getType() == NodeGene.TYPE.INPUT){
            reverse = true;
        }

        /*
         * Check whether the connection already exists within the genome
         */
        boolean conExists = false;
        for(ConnectionGene connection : connections.values()){
            if(connection.getIn_node() == node1.getId() && connection.getOut_node() == node2.getId()){
                conExists = true;
                break; //connectionAlreadyExists
            }else if(connection.getIn_node() == node2.getId() && connection.getOut_node() == node1.getId()){
                conExists = true;
                break;//ConnectionAlreadyExists
            }
        }

        /*
         * If the connection doesn't exist, we can create the connection in order,
         * depending on whether it needs to be reversed or not
         */
        if(!conExists){
            ConnectionGene newCon = new ConnectionGene(reverse ? node2.getId() : node1.getId(), reverse ? node1.getId() : node2.getId(),
                    weight, true, 0);
            connections.put(newCon.getInnovation(), newCon);
        }
    }



    /*
     * Add a node between a connection, in the process we disbale the existing connection and create 2 new nodes
     */

    public void addNode(){
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
        ConnectionGene inputToMiddle = new ConnectionGene(inNode.getId(), middle.getId(), INITIALNEWWEIGHT, true, 0);
        ConnectionGene middleToOutput = new ConnectionGene(middle.getId(), outNode.getId(), con.getWeight(), true, 0);

        //Add the node
        nodes.put(middle.getId(), middle);

        //Add the connection
        connections.put(inputToMiddle.getInnovation(), inputToMiddle);
        connections.put(middleToOutput.getInnovation(), middleToOutput);
    }





}
