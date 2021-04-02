package utility;

import data.LabWork;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Comparator;
import java.util.LinkedList;

@XmlRootElement(name = "LabCollection")
@XmlAccessorType(XmlAccessType.FIELD)
public class LabCollection {
    @XmlElement(name = "LabWork")
    private LinkedList<LabWork> labCollection;

    public LabCollection() {}

    public LabCollection(LinkedList<LabWork> labCollection) {
        this.labCollection = labCollection;
    }

    public LinkedList<LabWork> getLabCollection() {
        return labCollection;
    }

    public void setLabCollection(LinkedList<LabWork> labCollection) {
        this.labCollection = labCollection;
    }

    public boolean isNull() {
        return labCollection == null;
    }

    public Integer generateID() {
        if (labCollection.isEmpty()) return 1;
        labCollection.sort(Comparator.comparingInt(LabWork::getId));
        return labCollection.getLast().getId() + 1;
    }

    public void sortByID() {
        labCollection.sort(Comparator.comparingInt(LabWork::getId));
    }

    public void sortByName() {
        labCollection.sort(Comparator.comparing(LabWork::getName));
    }

    public LabWork getFirst() {
        return labCollection.stream().findFirst().orElse(null);
    }

    public LabWork getLabByID(Integer id) {
        return labCollection.stream().
                filter(labWork -> labWork.getId().equals(id)).findFirst().orElse(null);
    }

    public long getSumMiniPoint() {
        return labCollection.stream()
                .reduce(0L, (sum, l) -> sum += l.getMinimalPoint(), Long::sum);
    }

    public String filterStartWithName(String name) {
        return labCollection.stream().filter(labWork -> labWork.getName().startsWith(name))
                .reduce("", (sum, l) -> sum += l + "\n\n", String::concat).trim();
    }

    public String showCollection() {
        if (labCollection.isEmpty()) return "The collection is empty!";
        return labCollection.stream()
                .reduce("", (sum, m) -> sum += m + "\n\n", String::concat).trim();
    }
}
