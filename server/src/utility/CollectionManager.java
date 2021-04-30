package utility;

import data.LabWork;
import exceptions.DatabaseHandlingException;
import interaction.LabRaw;
import interaction.User;
import server.AppServer;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.NavigableSet;
import java.util.TreeSet;

public class CollectionManager {
    private LinkedList<LabWork> labCollection;
    private ZonedDateTime lastInitTime;
    private DatabaseCollectionManager databaseCollectionManager;

    public CollectionManager(DatabaseCollectionManager databaseCollectionManager) {
        this.databaseCollectionManager = databaseCollectionManager;

        loadCollection();
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

    public ZonedDateTime getLastInitTime() {
        return lastInitTime;
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

    public LinkedList<LabWork> getGreater(LabWork labToCompare) {
        return labCollection.stream().filter(labWork -> labWork.compareTo(labToCompare) > 0).collect(
                LinkedList::new,
                LinkedList::add,
                LinkedList::addAll
        );
    }

    public void removeFromCollection(LabWork labWork) {
        labCollection.remove(labWork);
    }

    private void loadCollection() {
        try {
            labCollection = databaseCollectionManager.getCollection();
            lastInitTime = ZonedDateTime.now();
            Outputer.println("Коллекция загружена.");
            AppServer.LOGGER.info("Коллекция загружена.");
        } catch (DatabaseHandlingException exception) {
            labCollection = new LinkedList<>();
            Outputer.printError("Коллекция не может быть загружена!");
            AppServer.LOGGER.severe("Коллекция не может быть загружена!");
        }
    }
}
