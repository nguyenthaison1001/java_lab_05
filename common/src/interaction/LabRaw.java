package interaction;

import data.Coordinates;
import data.Difficulty;
import data.Discipline;
import utility.ZonedDateTimeXMLAdapter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * Class operates object 'LabWork'.
 */
@XmlRootElement
public class LabRaw implements Serializable {
    private Integer id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private ZonedDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private Long minimalPoint; //Значение поля должно быть больше 0
    private Integer tunedInWorks;
    private Integer averagePoint; //Значение поля должно быть больше 0
    private Difficulty difficulty; //Поле может быть null
    private Discipline discipline; //Поле не может быть null

    public LabRaw() {}

    public LabRaw(String name, Coordinates coordinates, Long minimalPoint, Integer tunedInWorks, Integer averagePoint, Difficulty difficulty, Discipline discipline) {
        this.name = name;
        this.coordinates = coordinates;
        creationDate = ZonedDateTime.now();
        this.minimalPoint = minimalPoint;
        this.tunedInWorks = tunedInWorks;
        this.averagePoint = averagePoint;
        this.difficulty = difficulty;
        this.discipline = discipline;
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
    public Long getMinimalPoint() {
        return minimalPoint;
    }

    /**
     * @return TunedInWork of the LabWork.
     */
    @XmlElement
    public Integer getTunedInWorks() {
        return tunedInWorks;
    }

    /**
     * @return AveragePoint of the LabWork.
     */
    @XmlElement
    public Integer getAveragePoint() {
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
                ",\n\t name: '" + name + '\'' +
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
        data.LabWork labWork = (data.LabWork) o;
        return id.equals(labWork.getId()) &&
                name.equals(labWork.getName()) &&
                coordinates.equals(labWork.getCoordinates());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, coordinates);
    }
}

