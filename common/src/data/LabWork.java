package data;

import interaction.User;
import utility.ZonedDateTimeXMLAdapter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * Class operates object 'LabWork'.
 */
@XmlRootElement
public class LabWork implements Comparable<LabWork> {
    private Integer id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private ZonedDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private long minimalPoint; //Значение поля должно быть больше 0
    private int tunedInWorks;
    private Integer averagePoint; //Значение поля должно быть больше 0
    private Difficulty difficulty; //Поле может быть null
    private Discipline discipline; //Поле не может быть null
    private User owner;

    public LabWork() {}

    public LabWork(Integer id, String name, Coordinates coordinates, ZonedDateTime creationDate, long minimalPoint, int tunedInWorks,
                   int averagePoint, Difficulty difficulty, Discipline discipline, User owner) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.minimalPoint = minimalPoint;
        this.tunedInWorks = tunedInWorks;
        this.averagePoint = averagePoint;
        this.difficulty = difficulty;
        this.discipline = discipline;
        this.owner = owner;
    }

    /**
     * @return ID of the LabWork.
     */
    @XmlElement
    public Integer getId() {
        return id;
    }

    /**
     * @return Name of the LabWork.
     */
    @XmlElement
    public String getName() {
        return name;
    }

    /**
     * @return Coordinates of the LabWork.
     */
    @XmlElement
    public Coordinates getCoordinates() {
        return coordinates;
    }

    /**
     * @return Time of the LabWork.
     */
    @XmlElement
    @XmlJavaTypeAdapter(ZonedDateTimeXMLAdapter.class)
    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    /**
     * @return MinimalPoint of the LabWork.
     */
    @XmlElement
    public long getMinimalPoint() {
        return minimalPoint;
    }

    /**
     * @return TunedInWork of the LabWork.
     */
    @XmlElement
    public int getTunedInWorks() {
        return tunedInWorks;
    }

    /**
     * @return AveragePoint of the LabWork.
     */
    @XmlElement
    public int getAveragePoint() {
        return averagePoint;
    }

    /**
     * @return Difficulty of the LabWork.
     */
    @XmlElement
    public Difficulty getDifficulty() {
        return difficulty;
    }

    /**
     * @return Discipline of the LabWork.
     */
    @XmlElement
    public Discipline getDiscipline() {
        return discipline;
    }

    /**
     * @return Owner of the marine.
     */
    public User getOwner() {
        return owner;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public void setMinimalPoint(long minimalPoint) {
        this.minimalPoint = minimalPoint;
    }

    public void setTunedInWorks(int tunedInWorks) {
        this.tunedInWorks = tunedInWorks;
    }

    public void setAveragePoint(int averagePoint) {
        this.averagePoint = averagePoint;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public void setDiscipline(Discipline discipline) {
        this.discipline = discipline;
    }

    @Override
    public String toString() {
        return "LabWork{" +
                "\n\t id: " + id +
                ",\n\t user: '" + owner.getUsername() +
                ",\n\t name: '" + name  +
                ",\n\t coordinates: " + coordinates +
                ",\n\t creationDate: " + creationDate +
                ",\n\t minimalPoint: " + minimalPoint +
                ",\n\t tunedInWorks: " + tunedInWorks +
                ",\n\t averagePoint: " + averagePoint +
                ",\n\t difficulty: " + difficulty +
                ",\n\t discipline: " + discipline +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LabWork labWork = (LabWork) o;
        return id.equals(labWork.id) &&
                name.equals(labWork.name) &&
                coordinates.equals(labWork.coordinates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, coordinates);
    }

    @Override
    public int compareTo(LabWork labOther) {
        return averagePoint.compareTo(labOther.getAveragePoint());
    }
}
