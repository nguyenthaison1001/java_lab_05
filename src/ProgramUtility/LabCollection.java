package ProgramUtility;

import Data.LabWork;

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
        return labCollection.getLast().getId() + 1;
    }

    public LabWork getLabByID(Integer id) {
        for (LabWork labWork : labCollection) {
            if (labWork.getId().equals(id)) return labWork;
        }
        return null;
    }

    public void sortByID() {
        labCollection.sort(Comparator.comparingInt(LabWork::getId));
    }

    @Override
    public String toString() {
        if (labCollection.isEmpty()) return "The collection is empty!";
        StringBuilder info = new StringBuilder();
        for (LabWork labWork : labCollection) {
            info.append(labWork).append("\n");
        }
        return info.toString();
    }
}
