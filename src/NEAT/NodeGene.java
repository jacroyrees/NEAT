package NEAT;

public class NodeGene {
    private TYPE type;
    private int id;

    /*
     *Initialise the NEAT.NodeGene, when the first nodes are created they are one of
     * two types of nodes: INPUT / OUTPUT, every other node is then of type HIDDEN
     */
    public NodeGene(TYPE type, int id){
        this.type = type;
        this.id = id;
    }


    //ID of the node shows which node is connected to which
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    //Declare the three different types
    public enum TYPE{
        INPUT,
        HIDDEN,
        OUTPUT

    }

    /*
     *Get the type, due to random node creation - we must ensure that it is a HIDDEN-OUTPUT/HIDDEN-HIDDEN/INPUT-HIDDEN
     */
    public TYPE getType() {
        return type;
    }




    /*
     *We are copying the node to place in new connections, the reason we don't just parse it through
     *is that it's a new gene altogether, even though it has identical characteristics
     */
    public NodeGene copy(){

        return new NodeGene(type, id);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    private int x;
    private int y;


}
