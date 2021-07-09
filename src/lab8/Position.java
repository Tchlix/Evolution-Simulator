package lab8;

import java.util.Objects;

public class Position {
    int x;
    int y;
    public Position(int x,int y)
    {
        this.x = x;
        this.y = y;
    }
    public void setPosition(int x,int y)
    {
        this.x = x;
        this.y = y;
    }
    public boolean equals(Object o)
    {
        if(o instanceof Position)
            return equals((Position) o);
        return false;
    }
    public boolean equals(Position o)
    {
        return o.x == this.x && o.y == this.y;
    }
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "Position{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}