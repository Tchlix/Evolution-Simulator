package lab8;

public class Animal extends GridObject implements Comparable<Animal>{
    private Genes genes;
    private int energy;
    private int age = 0;
    private int direction = (int)(Math.random() * (8));
    private int nr_of_children = 0;
    private int cycle_when_died;
    private final Grid grid;
    public Animal(Grid grid)
    {
        this.grid = grid;
        this.setGenes(new Genes());
        this.energy = grid.getStartEnergy();
        this.position = new Position((int)(Math.random() * (grid.getWidth())),(int)(Math.random() * (grid.getHeight())));
    }
    public Animal(Animal father,Animal mother)
    {
        this.setGenes(new Genes(father.getGenes(), mother.getGenes()));
        this.grid = father.grid;
        this.position = new Position(father.position.x,father.position.y);
        this.energy = Math.floorDiv(father.getEnergy(),4) + Math.floorDiv(mother.getEnergy(),4);
    }
    public void eat(int share)
    {
        this.energy += Math.floorDiv(this.grid.getPlantEnergy(),share);
    }
    @Override
    public String toString() {
        if(energy == 0)
            return "Animal{"+ genes +
                    ", died on cycle=" + cycle_when_died +
                    ", age=" + age +
                    ", nr of children=" + nr_of_children +
                    "}\n";
        return "Animal{"+ genes +
                ", energy=" + energy +
                ", age=" + age +
                ", nr of children=" + nr_of_children +
                "}\n";
    }
    @Override
    public int compareTo(Animal o) {
        return Integer.compare((o.getEnergy()), (this.getEnergy()));
    }

    public boolean move()
    {
        energy -= grid.getMoveEnergy();
        if (energy <= 0)
            return false;
        else {
            age++;
            switch (direction) {//Move forward in current direction
                case 0 -> position.setPosition(position.x, (position.y + 1) % grid.getHeight());
                case 1 -> position.setPosition((position.x + 1) % grid.getWidth(), (position.y + 1) % grid.getHeight());
                case 2 -> position.setPosition((position.x + 1) % grid.getWidth(), position.y);
                case 3 -> {
                    if (position.y - 1 < 0)
                        position.y = grid.getHeight();
                    position.setPosition((position.x + 1) % grid.getWidth(), position.y - 1);
                }
                case 4 -> {
                    if (position.y - 1 < 0)
                        position.y = grid.getHeight();
                    position.setPosition(position.x, position.y - 1);
                }
                case 5 -> {
                    if (position.y - 1 < 0)
                        position.y = grid.getHeight();
                    if (position.x - 1 < 0)
                        position.x = grid.getWidth();
                    position.setPosition(position.x - 1, position.y - 1);
                }
                case 6 -> {
                    if (position.x - 1 < 0)
                        position.x = grid.getWidth();
                    position.setPosition(position.x - 1, position.y);
                }
                case 7 -> {
                    if (position.x - 1 < 0)
                        position.x = grid.getWidth();
                    position.setPosition(position.x - 1, (position.y + 1) % grid.getHeight());
                }
            }
            direction = (direction+genes.rotate()) % 8; //Rotate animal
            return true;
        }
    }

    public int getAge(){return age;}
    public Genes getGenes() {
        return genes;
    }

    public void setGenes(Genes genes) {
        this.genes = genes;
    }

    public int getEnergy() {
        return energy;
    }

    public Grid getMap() {
        return grid;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public int getNr_of_children() {
        return nr_of_children;
    }

    public void setNr_of_children(int nr_of_childrens) {
        this.nr_of_children = nr_of_childrens;
    }

    public void setCycle_when_died(int cycle_when_died) {
        this.cycle_when_died = cycle_when_died;
    }
}
