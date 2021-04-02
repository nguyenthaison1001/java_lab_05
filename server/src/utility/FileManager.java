package utility;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.time.ZonedDateTime;
import java.util.LinkedList;

/**
 * Operates writing/reading collection to the file.
 */
public class FileManager {
    private final String fileName;
    private final LabCollection labCollection;
    private ZonedDateTime lastSaveTime;
    private ZonedDateTime lastInitTime;

    public FileManager(String fileName, LabCollection labCollection) {
        this.fileName = fileName;
        this.labCollection = labCollection;
    }

    /**
     * Writes collection to a file.
     */
    public void writeCollection(){
        if (fileName != null) {
            try {
                JAXBContext jaxbContext = JAXBContext.newInstance(LabCollection.class);
                Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
                jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
                BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
                jaxbMarshaller.marshal(labCollection, writer);
                lastSaveTime = ZonedDateTime.now();
            } catch (JAXBException e){
                ResponseOutputer.appendError("The collection has encountered an error");
            } catch (IOException e) {
                ResponseOutputer.appendError("File can't be opened!");
            }
        } else ResponseOutputer.appendError("File not found!");
    }

    /**
     * Reads collection from a file.
     * @return Loaded collection.
     */
    public LabCollection readCollection() {
        if (fileName != null) {
            try {
                JAXBContext jaxbContext = JAXBContext.newInstance(LabCollection.class);
                Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                FileInputStream fileInputStream = new FileInputStream(fileName);
                BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
                LabCollection labCollection = (LabCollection) jaxbUnmarshaller.unmarshal(bufferedInputStream);
                if (labCollection.isNull()) {
                    return new LabCollection(new LinkedList<>());
                }
                lastInitTime = ZonedDateTime.now();
                return labCollection;
            } catch (IOException exception) {
                ResponseOutputer.appendError("File not found!");
                System.exit(0);
            } catch (JAXBException exception) {
                ResponseOutputer.appendError("XML file structure error. File can't be read!");
                System.exit(0);
            }
        } else ResponseOutputer.appendError("File not found!");
        return new LabCollection(new LinkedList<>());
    }

    /**
     * Get saving time.
     * @return lastSaveTime.
     */
    public ZonedDateTime getLastSaveTime() {
        return lastSaveTime;
    }

    /**
     * Get initializing time.
     * @return lastInitTime.
     */
    public ZonedDateTime getLastInitTime() {
        return lastInitTime;
    }

    @Override
    public String toString() {
        return "FileManager (class for working with the boot file)";
    }
}
