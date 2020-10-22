import java.util.Iterator;

public class Main {

    public static void main(String[] args) {
        Genome g1 = new Genome();
        Genome g2 = new Genome();
        double weight = 0.04;
        g1.addNodeGene(new NodeGene(NodeGene.TYPE.INPUT, 1));
        g1.addNodeGene(new NodeGene(NodeGene.TYPE.INPUT, 2));
        g1.addNodeGene(new NodeGene(NodeGene.TYPE.INPUT, 3));
        g1.addNodeGene(new NodeGene(NodeGene.TYPE.HIDDEN, 5));
        g1.addNodeGene(new NodeGene(NodeGene.TYPE.OUTPUT, 4));
    /*    for (int i = 0; i < g1.getNodes().size() + 1; i++) {
            if (g1.getNodes().get(i) != null) {
                System.out.println(g1.getNodes().get(i).getId());
            }
        }*/
        System.out.println("########################");
        g2.addNodeGene(new NodeGene(NodeGene.TYPE.INPUT, 1));
        g2.addNodeGene(new NodeGene(NodeGene.TYPE.INPUT, 2));
        g2.addNodeGene(new NodeGene(NodeGene.TYPE.INPUT, 3));
        g2.addNodeGene(new NodeGene(NodeGene.TYPE.OUTPUT, 4));
        g2.addNodeGene(new NodeGene(NodeGene.TYPE.HIDDEN, 5));
        g2.addNodeGene(new NodeGene(NodeGene.TYPE.HIDDEN, 6));


        g1.addConnectionGene(new ConnectionGene(1, 4, weight, true, 1));
        g1.addConnectionGene(new ConnectionGene(2, 4, weight, false, 2));
        g1.addConnectionGene(new ConnectionGene(3, 4, weight, true, 3));
        g1.addConnectionGene(new ConnectionGene(2, 5, weight, true, 4));
        g1.addConnectionGene(new ConnectionGene(5, 4, weight, true, 5));
        g1.addConnectionGene(new ConnectionGene(1, 5, weight, true, 8));

        g2.addConnectionGene(new ConnectionGene(1, 4, weight, true, 1));
        g2.addConnectionGene(new ConnectionGene(2, 4, weight, false, 2));
        g2.addConnectionGene(new ConnectionGene(3, 4, weight, true, 3));
        g2.addConnectionGene(new ConnectionGene(2, 5, weight, true, 4));
        g2.addConnectionGene(new ConnectionGene(5, 4, weight, false, 5));
        g2.addConnectionGene(new ConnectionGene(5, 6, weight, true, 6));
        g2.addConnectionGene(new ConnectionGene(6, 4, weight, true, 7));
        g2.addConnectionGene(new ConnectionGene(3, 5, weight, true, 9));
        g2.addConnectionGene(new ConnectionGene(1, 6, weight, true, 10));

        g1.setFitness(700);
        g2.setFitness(500);

        Species species = new Species();
        Genome child = species.crossOver(g1, g2);

        for (Integer key : child.getNodes().keySet()) {
            System.out.println(key);
        }

        System.out.println("##################");
        for (Integer key : child.getConnectionGenes().keySet()) {
            System.out.println(key);
        }



      /*  System.out.println("#######");
        for (int i = 0; i < child.getNodes().values().size()+1; i++) {
            if (child.getNodes().get(i) != null) {
                System.out.println(child.getNodes().get(i).getId());


            }
        }

        System.out.println("#######");

        for (int i = 0; i < child.getConnectionGenes().values().size()+1; i++) {
            if (child.getConnectionGenes().get(i) != null) {
                System.out.println(child.getConnectionGenes().get(i).getIn_node());
                System.out.println(child.getConnectionGenes().get(i).getOut_node());
                System.out.println(child.getConnectionGenes().get(i).isEnabled());
                System.out.println("_____________________");
            }
        }

    }*/
    }
    }




