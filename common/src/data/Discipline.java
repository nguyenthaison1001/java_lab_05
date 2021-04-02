package data;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Objects;

/**
 * Class operates Discipline.
 */
@XmlRootElement
public class Discipline implements Serializable {
    private String name; //Поле не может быть null, Строка не может быть пустой
    private int labsCount;

    public Discipline() {}

    public Discipline(String name, int labsCount) {
        this.name = name;
        this.labsCount = labsCount;
    }

    /**
     * Get name of Discipline.
     */
    @XmlElement
    public String getName() {
        return name;
    }

    /**
     * Get the number of labs.
     */
    @XmlElement
    public int getLabsCount() {
        return labsCount;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLabsCount(int labsCount) {
        this.labsCount = labsCount;
    }

    @Override
    public String toString() {
        return "{" +
                "name='" + name + '\'' +
                ", labsCount=" + labsCount +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Discipline that = (Discipline) o;
        return labsCount == that.labsCount &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, labsCount);
    }
}
