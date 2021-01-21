import NEAT.*;
import Tests.GenomePrinter;

import java.util.Random;


public class Main{

    public static void main(String[] args) {
       Genome g1 = new Genome();
        Genome g2 = new Genome();


        g1.setAdjustedFitness(700);
        g2.setAdjustedFitness(600);


g1.addNode();
g1.addNode();
        System.out.println(g2.getNodeGenes().size());
     System.out.println("##################");System.out.println("##################");

        GenomePrinter genomePrinter = new GenomePrinter();
        genomePrinter.showGenome(g1, "1");
        genomePrinter.showGenome(g2, "2");

        Species species = new Species();
        species.getGenome().add(g2);
        species.getGenome().add(g1);

        Genome child = species.crossOver(g1, g2);
        System.out.println(child.getConnectionGenes().size());
        genomePrinter.showGenome(child, "3");
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
   /*  GenomePrinter genomePrinter = new GenomePrinter();
     Thread thread = new Thread();
     Pool pool = new Pool();
     boolean running = true;
     pool.initialisePool();
     int i = 0;
     while(i < 10){

      Random random = new Random();

      pool.breedNewGeneration();
      Species randomSpeces = pool.getSpecies().get(1);
      Genome randomGenome = randomSpeces.getGenome().get(randomSpeces.getGenome().size());
      GenomePrinter printer = new GenomePrinter();
      printer.showGenome(randomGenome, String.valueOf(i));
      i++;
     }*/






 }
}




