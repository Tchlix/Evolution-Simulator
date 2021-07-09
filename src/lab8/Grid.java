package lab8;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Math.floorDiv;

public class Grid {
    private final int width;
    private final int height;
    private final int jungleRatio;
    private final int plantEnergy;
    private final int moveEnergy;
    private final int startEnergy;
    private int howManyDied = 0;
    private int ageSumOfDead = 0;
    private int cycle_nr = 0;
    HashMap<Position,AnimalCollection> AnimalsOnMap = new HashMap<>();
    ArrayList<Animal> animals = new ArrayList<>();
    HashMap<Position,Plant> PlantsOnMap = new HashMap<>();
    HashMap<Genes,Integer> GenesHistory = new HashMap<>();
    HashMap<Genes,Integer> GenesRightNow = new HashMap<>();

    public Grid(Map<String,Integer> parameters)
    {
        int startAnimals = parameters.get("startAnimals");
        this.width = parameters.get("width");
        this.height = parameters.get("height");
        this.startEnergy = parameters.get("startEnergy");
        this.moveEnergy = parameters.get("moveEnergy");
        this.plantEnergy = parameters.get("plantEnergy");
        this.jungleRatio = parameters.get("jungleRatio");
        for(int i = 0; i< startAnimals; i++) {
            animals.add(new Animal(this));
        }
        for (Animal animal : animals) {
            if (AnimalsOnMap.containsKey(animal.position))
                AnimalsOnMap.get(animal.position).put(animal);
            else
                AnimalsOnMap.put(animal.position, new AnimalCollection(animal));
            change_genes_add(animal.getGenes());
        }
    }
    private void change_genes_add(Genes genes)
    {
        int count = GenesHistory.getOrDefault(genes, 0);
        GenesHistory.put(genes,count+1);

        count = GenesRightNow.getOrDefault(genes, 0);
        GenesRightNow.put(genes,count+1);
    }
    private void change_genes_substract(Genes genes)
    {
        GenesRightNow.put(genes,GenesRightNow.get(genes)-1);
    }
    public void birth(Animal mother,Animal father)
    {
        Animal child = new Animal(mother,father);
        change_genes_add(child.getGenes());
        animals.add(child);

        father.setEnergy(3 * floorDiv(father.getEnergy(),4));
        mother.setEnergy(3 * floorDiv(mother.getEnergy(),4));

        father.setNr_of_children(father.getNr_of_children() + 1);
        mother.setNr_of_children(mother.getNr_of_children() + 1);
    }
    public void died_or_eaten(GridObject obj)
    {
        if(obj instanceof Animal) {
            howManyDied++;
            ageSumOfDead += ((Animal) obj).getAge();
            ((Animal) obj).setCycle_when_died(cycle_nr);
            change_genes_substract(((Animal) obj).getGenes());
            animals.remove(obj);
        }
        else
            PlantsOnMap.remove(obj.position);
    }
    public void growPlant()
    {
        //Jungle
        Position x = null;
        int minx = Math.floorDiv(width-jungleRatio,2);
        int maxx = Math.floorDiv(width-jungleRatio,2)+jungleRatio;
        int miny = Math.floorDiv(height-jungleRatio,2);
        int maxy = Math.floorDiv(height-jungleRatio,2)+jungleRatio;
        boolean foundPlace = false;
        for(int i = 0;i < jungleRatio*jungleRatio;i++)
        {
            x = new Position((int)(Math.random() * (maxx - minx) + minx),(int)(Math.random() * (maxy - miny) + miny));
            if(!PlantsOnMap.containsKey(x) && !AnimalsOnMap.containsKey(x)) {
                foundPlace = true;
                break;
            }
        }
        if(!foundPlace)
        {
            for(int i = minx;i < maxx;i++) {
                for (int j = miny; j < maxy; j++) {
                    x = new Position(i, j);
                    if (!PlantsOnMap.containsKey(x) && !AnimalsOnMap.containsKey(x)) {
                        foundPlace = true;
                        break;
                    }
                }
                if (foundPlace)
                    break;
            }
        }
        if(foundPlace)
            PlantsOnMap.put(x,new Plant(plantEnergy,x));
        //Rest of map
        if(AnimalsOnMap.size() == width * height || PlantsOnMap.size() == width * height || PlantsOnMap.size()+AnimalsOnMap.size() == width * height)
            return;
        x = new Position((int)(Math.random() * (this.getWidth())),(int)(Math.random() * (this.getHeight())));
        while (PlantsOnMap.containsKey(x) || AnimalsOnMap.containsKey(x))
            x.setPosition((int)(Math.random() * (this.getWidth())),(int)(Math.random() * (this.getHeight())));
        PlantsOnMap.put(x,new Plant(plantEnergy,x));
    }

    public void cycle()
    {
        cycle_nr++;
        ArrayList<GridObject> to_be_deleted = new ArrayList<>();
        AnimalsOnMap = new HashMap<>();
        growPlant();
        //If animals are alive, move them, otherwise add to 'to_be_deleted'
        for (Animal animal : animals) {
            if(animal.move())
            {
                if (AnimalsOnMap.containsKey(animal.position))
                    AnimalsOnMap.get(animal.position).put(animal);
                else
                    AnimalsOnMap.put(animal.position, new AnimalCollection(animal));
            }
            else to_be_deleted.add(animal);
        }
        //Deleting corpses
        while (!to_be_deleted.isEmpty()) {
            died_or_eaten(to_be_deleted.get(0));
            to_be_deleted.remove(0);
        }
        //Eating
        for(Plant plant:PlantsOnMap.values()) {
            if(AnimalsOnMap.containsKey(plant.position)) {
                AnimalsOnMap.get(plant.position).eat();
                to_be_deleted.add(plant);
            }
        }
        //Deleting eaten plants
        while (!to_be_deleted.isEmpty()) {
            died_or_eaten(to_be_deleted.get(0));
            to_be_deleted.remove(0);
        }
        //Reproduce
        for(AnimalCollection animalsOnPosition:AnimalsOnMap.values()){
            animalsOnPosition.reproduce();
        }
    }

    public int getStartEnergy() {
        return startEnergy;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getJungleRatio() {
        return jungleRatio;
    }

    public int getPlantEnergy() {
        return plantEnergy;
    }

    public int getMoveEnergy() {
        return moveEnergy;
    }

    public int getCycle_nr() {
        return cycle_nr;
    }

    public int getHowManyDied() {
        return howManyDied;
    }

    public int getAgeSumOfDead() {
        return ageSumOfDead;
    }
}
