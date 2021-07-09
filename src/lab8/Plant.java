package lab8;

public class Plant extends GridObject {
    int energy_worth;
    public Plant(int energy_worth,Position position)
    {
        this.energy_worth = energy_worth;
        this.position = position;
    }
}
