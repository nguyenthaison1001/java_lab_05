package utility;

import client.AppClient;
import data.Coordinates;
import data.Difficulty;
import data.Discipline;
import exceptions.MustBeNotEmptyException;
import exceptions.NotInDeclaredLimitsException;



import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.NoSuchElementException;

/**
 * Operates questions to ask user about LabWork's value.
 */
public class LabAsker {
    private BufferedReader userReader;
    private boolean fileMode;

    public LabAsker(BufferedReader userReader) {
        this.userReader = userReader;
    }

    /**
     * Sets a reader to read user input.
     * @param userReader Scanner to set.
     */
    public void setUserReader(BufferedReader userReader) {
        this.userReader = userReader;
    }

    /**
     * @return userReader which uses for user input.
     */
    public BufferedReader getUserReader() {
        return userReader;
    }

    /**
     * Sets LabWork asker mode to 'File Mode'.
     */
    public void setFileMode() {
        fileMode = true;
    }

    /**
     * Sets LabWork asker mode to 'User Mode'.
     */
    public void setUserMode() {
        fileMode = false;
    }

    /**
     * Asks a user the LabWork's name.
     * @return LabWork's name.
     */
    public String askName()  {
        String name;
        while (true) {
            try {
                Outputer.println("Enter the name: ");
                Outputer.print(AppClient.PS2);
                name = userReader.readLine().trim();
                if (fileMode) Outputer.println(name);
                if (name.equals("")) throw new MustBeNotEmptyException();
                break;
            } catch (IOException exception) {
                Outputer.printError("Interrupted I/O operations!");
            } catch (NoSuchElementException exception) {
                Outputer.printError("Name not recognized!");
            } catch (MustBeNotEmptyException exception) {
                Outputer.printError("Name can't be empty!");
            } catch (IllegalStateException exception) {
                Outputer.printError("Unexpected error!");
                System.exit(0);
            }
        }
        return name;
    }

    /**
     * Asks a user the LabWork's X coordinate.
     * @return LabWork's X coordinate.
     */
    public Integer askX() {
        String strX;
        int x;
        while (true) {
            int x_BIGGER_THAN = -881;
            try {
                Outputer.println("Enter coordinate of X >" + x_BIGGER_THAN +": ");
                Outputer.print(AppClient.PS2);
                strX = userReader.readLine().trim();
                if (fileMode) Outputer.println(strX);
                if (strX.equals("")) throw new MustBeNotEmptyException();
                x = Integer.parseInt(strX);
                if (x <= x_BIGGER_THAN) throw new NotInDeclaredLimitsException();
                break;
            } catch (MustBeNotEmptyException exception) {
                Outputer.printError("X-coordinate can't be empty!");
            } catch (IOException exception) {
                Outputer.printError("Interrupted I/O operations!");
            } catch (NotInDeclaredLimitsException exception) {
                Outputer.printError("X-coordinate must be greater than " + x_BIGGER_THAN + "!");
            } catch (NumberFormatException exception) {
                Outputer.printError("X-coordinate must be a number!");
            } catch (NullPointerException | IllegalStateException exception) {
                Outputer.printError("Unexpected error!");
                System.exit(0);
            }
        }
        return x;
    }

    /**
     * Asks a user the LabWork's Y coordinate.
     * @return LabWork's Y coordinate.
     */
    public long askY() {
        String strY;
        long y;
        while (true) {
            try {
                Outputer.println("Enter coordinate of Y: ");
                Outputer.print(AppClient.PS2);
                strY = userReader.readLine().trim();
                if (fileMode) Outputer.println(strY);
                y = Long.parseLong(strY);
                break;
            } catch (IOException exception) {
                Outputer.printError("Interrupted I/O operations!");
            } catch (NumberFormatException exception) {
                Outputer.printError("Y-coordinate must be a number!");
            } catch (NullPointerException | IllegalStateException exception) {
                Outputer.printError("Unexpected error!");
                System.exit(0);
            }
        }
        return y;
    }

    /**
     * Asks a user the LabWork's coordinate.
     * @return LabWork's coordinate.
     */
    public Coordinates askCoordinates() {
        Integer x = askX();
        long y = askY();
        return new Coordinates(x, y);
    }

    /**
     * Asks a user the LabWork's MinimalPoint.
     * @return LabWork's MinimalPoint.
     */
    public long askMinimalPoint() {
        String strMinimalPoint;
        long minimalPoint;
        while (true) {
            long MINIMALPOINT_BIGGER_THAN = 0;
            try {
                Outputer.println("Enter minimal point > " + MINIMALPOINT_BIGGER_THAN +": ");
                Outputer.print(AppClient.PS2);
                strMinimalPoint = userReader.readLine().trim();
                if (fileMode) Outputer.println(strMinimalPoint);
                if (strMinimalPoint.equals("")) throw new MustBeNotEmptyException();
                minimalPoint = Long.parseLong(strMinimalPoint);
                if (minimalPoint <= MINIMALPOINT_BIGGER_THAN) throw new NotInDeclaredLimitsException();
                break;
            } catch (IOException exception) {
                Outputer.printError("Interrupted I/O operations!");
            } catch (MustBeNotEmptyException exception) {
                Outputer.printError("Minimal Point can't be empty!");
            } catch (NotInDeclaredLimitsException exception) {
                Outputer.printError("Minimal point must be bigger than " + MINIMALPOINT_BIGGER_THAN + "!");
            } catch (NumberFormatException exception) {
                Outputer.printError("Minimal point must be a number!");
            } catch (NullPointerException | IllegalStateException exception) {
                Outputer.printError("Unexpected error!");
                System.exit(0);
            }
        }
        return minimalPoint;
    }

    /**
     * Asks a user the LabWork's TunedInWorks.
     * @return LabWork's TunedInWorks.
     */
    public int askTunedInWorks() {
        String strTunedInWorks;
        int tunedInWorks;
        while (true) {
            try {
                Outputer.println("Enter tuned in works: ");
                Outputer.print(AppClient.PS2);
                strTunedInWorks = userReader.readLine().trim();
                if (fileMode) Outputer.println(strTunedInWorks);
                if (strTunedInWorks.equals("")) throw new MustBeNotEmptyException();
                tunedInWorks = Integer.parseInt(strTunedInWorks);
                break;
            } catch (IOException exception) {
                Outputer.printError("Interrupted I/O operations!");
            } catch (MustBeNotEmptyException exception) {
                Outputer.printError("Tuned in works can't be empty!");
            } catch (NumberFormatException exception) {
                Outputer.printError("Tuned in works must be a number!");
            } catch (NullPointerException | IllegalStateException exception) {
                Outputer.printError("Unexpected error!");
                System.out.println(Arrays.toString(exception.getStackTrace()));
                System.exit(0);
            }
        }
        return tunedInWorks;
    }

    /**
     * Asks a user the LabWork's AveragePoint.
     * @return LabWork's AveragePoint.
     */
    public int askAveragePoint() {
        String strAveragePoint;
        int averagePoint;
        while (true) {
            int AVERAGEPOINT_BIGGER_THAN = 0;
            try {
                Outputer.println("Enter average point > " + AVERAGEPOINT_BIGGER_THAN + ": ");
                Outputer.print(AppClient.PS2);
                strAveragePoint = userReader.readLine().trim();
                if (fileMode) Outputer.println(strAveragePoint);
                averagePoint = Integer.parseInt(strAveragePoint);
                if (averagePoint <= AVERAGEPOINT_BIGGER_THAN) throw new NotInDeclaredLimitsException();
                break;
            } catch (IOException exception) {
                Outputer.printError("Interrupted I/O operations!");
            } catch (NotInDeclaredLimitsException e) {
                Outputer.printError("Average point must be bigger than " + AVERAGEPOINT_BIGGER_THAN + "!");
            } catch (NumberFormatException exception) {
                Outputer.printError("Average point must be represented by number!");
            } catch (NullPointerException | IllegalStateException exception) {
                Outputer.printError("Unexpected error!");
                System.exit(0);
            }
        }
        return averagePoint;
    }

    /**
     * Asks a user the LabWork's Difficulty.
     * @return LabWork's Difficulty.
     */
    public Difficulty askDifficulty() {
        String strDifficulty;
        Difficulty difficulty;
        while (true) {
            try {
                Outputer.println("List of difficulties: " + Difficulty.nameList());
                Outputer.println("Enter Difficulty: ");
                Outputer.print(AppClient.PS2);
                strDifficulty = userReader.readLine().trim();
                if (fileMode) Outputer.println(strDifficulty);
                difficulty = Difficulty.valueOf(strDifficulty.toUpperCase());
                break;
            } catch (IOException exception) {
                Outputer.printError("Interrupted I/O operations!");
            } catch (IllegalArgumentException exception) {
                Outputer.printError("This difficulty not listed!");
            } catch (NullPointerException | IllegalStateException exception) {
                Outputer.printError("Unexpected error!");
                System.exit(0);
            }
        }
        return difficulty;
    }

    /**
     * Asks a user the LabWork's NameOfDiscipline.
     * @return LabWork's NameOfDiscipline.
     */
    public String askNameOfDiscipline() {
        String nameDiscipline;
        while (true) {
            try {
                Outputer.println("Enter name of Discipline: ");
                Outputer.print(AppClient.PS2);
                nameDiscipline = userReader.readLine().trim();
                if (fileMode) Outputer.println(nameDiscipline);
                if (nameDiscipline.equals("")) throw new MustBeNotEmptyException();
                break;
            } catch (MustBeNotEmptyException exception) {
                Outputer.printError("Name of discipline can't be empty!");
            } catch (IOException exception) {
                Outputer.printError("Interrupted I/O operations!");
            } catch (IllegalArgumentException exception) {
                Outputer.printError("This difficulty not listed!");
            } catch (NullPointerException | IllegalStateException exception) {
                Outputer.printError("Unexpected error!");
                System.exit(0);
            }
        }
        return nameDiscipline;
    }

    /**
     * Asks a user the LabWork's MinimalPoint.
     * @return LabWork's MinimalPoint.
     */
    public int askLabsCountOfDiscipline() {
        String strLabsCountDiscipline;
        int labsCountDiscipline;
        while (true) {
            try {
                Outputer.println("Enter the number of labs of Discipline: ");
                Outputer.print(AppClient.PS2);
                strLabsCountDiscipline = userReader.readLine().trim();
                if (fileMode) Outputer.println(strLabsCountDiscipline);
//                if (strLabsCountDiscipline.equals("")) break;
                labsCountDiscipline = Integer.parseInt(strLabsCountDiscipline);
                break;
            } catch (NumberFormatException exception) {
                Outputer.printError("The number of labs must be a number!");
            } catch (IOException exception) {
                Outputer.printError("Interrupted I/O operations!");
            } catch (NullPointerException | IllegalStateException exception) {
                Outputer.printError("Unexpected error!");
                System.exit(0);
            }
        }
        return labsCountDiscipline;
    }

    /**
     * Asks a user the LabWork's Discipline.
     * @return LabWork's Discipline.
     */
    public Discipline askDiscipline() {
        String nameDiscipline = askNameOfDiscipline();
        int labsCount = askLabsCountOfDiscipline();
        return new Discipline(nameDiscipline, labsCount);
    }

    /**
     * Asks a user sure or not.
     * @return true/false.
     */
    public boolean areYouSure() {
        String answer;
        try {
            while (true) {
                Outputer.printWarning("Are you sure? (yes/no)");
                Outputer.print(AppClient.PS2);
                answer = userReader.readLine().trim();
                if (fileMode) Outputer.println(answer);
                switch (answer) {
                    case "yes":
                        return true;
                    case "no" :
                        return false;
                    default: Outputer.printWarning("Yes or no?");
                }
            }
        } catch (IOException exception) {
            Outputer.printError("Interrupted I/O operations!");
        } return false;
    }

    @Override
    public String toString() {
        return "LabAsker (class to ask the user about information of elements)";
    }
}
