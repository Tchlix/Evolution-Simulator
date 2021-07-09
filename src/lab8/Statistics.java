package lab8;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class Statistics {
    private final Window window;
    private float allTime_EnergySum = 0;
    private float allTime_NumberOfAnimals = 0;
    private float allTime_NumberOfPlants = 0;
    private float allTime_NrOfChildren = 0;
    int statson;
    public Statistics(Window window,int statson)
    {
        this.window = window;
        this.statson = statson;
    }
    private int EnergySum()
    {
        int sum = 0;
        for(Animal animal: window.grid.animals)
            sum += animal.getEnergy();
        return sum;
    }
    private int NumberOfAnimals()
    {
        return window.grid.animals.size();
    }
    private int Cycle_nr()
    {
        return window.grid.getCycle_nr();
    }
    private int NumberOfPlants()
    {
        return window.grid.PlantsOnMap.size();
    }
    private float MeanNrOfChildren()
    {
        float sum = 0;
        for(Animal animal: window.grid.animals)
            sum += animal.getNr_of_children();
        return sum / (2 * NumberOfAnimals());
    }
    private float AverageLifespan()
    {
        if(window.grid.getHowManyDied() == 0)
            return 0;
        return (float)window.grid.getAgeSumOfDead() / window.grid.getHowManyDied();
    }
    public Genes Most_common_genes(HashMap<Genes,Integer> Genes)
    {
        Genes most_common = null;
        int count = 0;
        for(Genes genes:Genes.keySet())
            if(Genes.get(genes) > count)
            {
                most_common = genes;
                count = Genes.get(genes);
            }
        return most_common;
    }
    private void allTime_StatisticsToFile()
    {
        int tmp = statson + 1;

        try {
            File myObj = new File("statistics_"+window.name()+".txt");
            myObj.createNewFile();
            FileWriter myWriter = new FileWriter("statistics_"+window.name()+".txt");
            String text=String.format("average number of animals %f\n" +
                            "average energy %f\n" +
                            "average number of plants %f\n" +
                            "average number of children %f\n" +
                            "average live lenght of animals that died %f\n" +
                            "most popular genome %s",
                            allTime_NumberOfAnimals/tmp,
                            allTime_EnergySum/tmp,
                            allTime_NumberOfPlants/tmp,
                            allTime_NrOfChildren/tmp,
                            AverageLifespan(),
                            Most_common_genes(window.grid.GenesHistory));
            myWriter.write(text);
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void add_allTimeStatistics()
    {
        if(NumberOfAnimals() > 0)
            allTime_EnergySum += EnergySum()/NumberOfAnimals();
        allTime_NumberOfAnimals += NumberOfAnimals();
        allTime_NumberOfPlants += NumberOfPlants();
        allTime_NrOfChildren += MeanNrOfChildren();
    }
    public String toString()
    {
        int animalsnmbr = NumberOfAnimals();

        if (Cycle_nr() <= statson) {
            add_allTimeStatistics();
            if(Cycle_nr() == statson)
                allTime_StatisticsToFile();
        }

        if(animalsnmbr == 0)
            return "All animals died\tplants " + NumberOfPlants();

        if(window.tracked_animal != null)
            return String.format("Cycle %d \t" +
                            "Animals %d\t" +
                            "Average energy %d\t" +
                            "Plants %d\t" +
                            "Average children %f\t" +
                            "Average lifespan %f\t" +
                            "Most popular genome %s \n %s",
                            Cycle_nr(),
                            animalsnmbr,
                            EnergySum()/animalsnmbr,
                            NumberOfPlants(),
                            MeanNrOfChildren(),
                            AverageLifespan(),
                            Most_common_genes(window.grid.GenesRightNow),window.tracked_animal);
        else
            return String.format("Cycle %d \t" +
                            "Animals %d\t" +
                            "Average energy %d\t" +
                            "Plants %d\t" +
                            "Average children %f\t" +
                            "Average lifespan %f\t" +
                            "Most popular genome %s",
                            Cycle_nr(),
                            animalsnmbr,
                            EnergySum()/animalsnmbr,
                            NumberOfPlants(),
                            MeanNrOfChildren(),
                            AverageLifespan(),
                            Most_common_genes(window.grid.GenesRightNow));
    }
}
