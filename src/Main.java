import NEAT.ConnectionGene;
import NEAT.Genome;
import NEAT.Pool;
import NEAT.Species;


public class Main {

    public static void main(String[] args) {
        Genome g1 = new Genome();
        Genome g2 = new Genome();

        System.out.println("########################");
       double weight = 0.4;

        System.out.println(Pool.globalInnovationNumber);

        System.out.println(Pool.globalInnovationNumber);
        System.out.println("hi");
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
         //   System.out.println(key);
        }

        System.out.println("##################");
        for (Integer key : child.getConnectionGenes().keySet()) {
           // System.out.println(key);
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




