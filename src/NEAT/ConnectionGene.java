package NEAT;

public class ConnectionGene {


    private int in_node; //which node goes into the connection
    private int out_node; //Which node goes out of the connection
    private double weight; //The weight of the connection
    private boolean isEnabled; //Whether the connection is enabled
    private int innovation; //The innovation number of the node


    //Instantiate the connection
    public ConnectionGene(int in_node, int out_node, double weight, boolean isEnabled, int innovation){
        this.in_node = in_node;
        this.out_node = out_node;
        this.weight = weight;
        this.isEnabled = isEnabled;
        this.innovation = innovation;
    }


    public ConnectionGene copy(){
        return new ConnectionGene(this.in_node, this.out_node, this.weight, this.isEnabled, this.innovation);
    }
    //get which in_node corresponds to the connection
    public int getIn_node() {
        return in_node;
    }

    public void setIn_node(int in_node) {
        this.in_node = in_node;
    }

    //get which out_node corresponds to the connection
    public int getOut_node() {
        return out_node;
    }

    public void setOut_node(int out_node) {
        this.out_node = out_node;
    }

    //Get the weight of the connection
    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    //Set the connection to true/false whether it impacts the network
    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    //Get the innovation number
    public int getInnovation() {
        return innovation;
    }

    public void setInnovation(int innovation) {
        this.innovation = innovation;
    }




}
