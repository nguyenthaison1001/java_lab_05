package data;

import java.io.Serializable;
import java.util.Objects;

/**
 * X-Y coordinates.
 */
public class Coordinates implements Serializable {
    private Integer x; //Значение поля должно быть больше -881, Поле не может быть null
    private long y;

    public Coordinates() {
    }

    public Coordinates(Integer x, long y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Get X coordinates.
     */
    public Integer getX() {
        return x;
    }

    /**
     * Get Y coordinates.
     */
    public long getY() {
        return y;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public void setY(long y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinates that = (Coordinates) o;
        return y == that.y &&
                Objects.equals(x, that.x);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
