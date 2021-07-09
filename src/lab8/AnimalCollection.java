package lab8;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;

public class AnimalCollection {
    Color color;
    ArrayList<Animal> animals = new ArrayList<>();
    public AnimalCollection(Animal animal)
    {
        animals.add(animal);
        setColor(animal);
    }
    public void put(Animal animal)
    {
        animals.add(animal);
        setColor(animal);
    }
    public void eat()//Only the strongest animals in a given field eat
    {
        Collections.sort(animals);
        int share = 1;
        while (share < animals.size() && animals.get(share).getEnergy() == animals.get(0).getEnergy())
            share += 1;
        for(int i = 0;i < share;i++)
            animals.get(i).eat(share);
    }
    private void setColor(Animal animal)//Color cells by energy of strongest animal
    {
        if(animal.getEnergy() >= animal.getMap().getStartEnergy())
            color = Color.BLUE;
        else if(color != Color.BLUE && animal.getEnergy() >= animal.getMap().getStartEnergy() / 2)
            color = Color.ORANGE;
        else color = Color.RED;
    }
    public void reproduce()
    {
        if(animals.size() >= 2 && animals.get(1).getEnergy() >= animals.get(1).getMap().getStartEnergy() / 2)
            animals.get(0).getMap().birth(animals.get(0),animals.get(1));
    }
    public Animal strongest()
    {
        return animals.get(0);
    }
}
