package ProgramUtility;

import Data.Coordinates;
import Data.Difficulty;
import Data.Discipline;
import Exceptions.MustBeNotEmptyException;
import Exceptions.NotInDeclaredLimitsException;
import Run.App;

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
                Console.println("Enter the name: ");
                Console.print(App.comPrompt);
                name = userReader.readLine().trim();
                if (fileMode) Console.println(name);
                if (name.equals("")) throw new MustBeNotEmptyException();
                break;
            } catch (IOException exception) {
                Console.printError("Interrupted I/O operations!");
            } catch (NoSuchElementException exception) {
                Console.printError("Name not recognized!");
            } catch (MustBeNotEmptyException exception) {
                Console.printError("Name can't be empty!");
            } catch (IllegalStateException exception) {
                Console.printError("Unexpected error!");
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
        Integer x;
        while (true) {
            Integer x_BIGGER_THAN = -881;
            try {
                Console.println("Enter coordinate of X >" + x_BIGGER_THAN +": ");
                Console.print(App.comPrompt);
                strX = userReader.readLine().trim();
                if (fileMode) Console.println(strX);
                if (strX.equals("")) throw new MustBeNotEmptyException();
                x = Integer.parseInt(strX);
                if (x <= x_BIGGER_THAN) throw new NotInDeclaredLimitsException();
                break;
            } catch (MustBeNotEmptyException exception) {
                Console.printError("X-coordinate can't be empty!");
            } catch (IOException exception) {
                Console.printError("Interrupted I/O operations!");
            } catch (NotInDeclaredLimitsException exception) {
                Console.printError("X-coordinate must be greater than " + x_BIGGER_THAN + "!");
            } catch (NumberFormatException exception) {
                Console.printError("X-coordinate must be a number!");
            } catch (NullPointerException | IllegalStateException exception) {
                Console.printError("Unexpected error!");
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
                Console.println("Enter coordinate of Y: ");
                Console.print(App.comPrompt);
                strY = userReader.readLine().trim();
                if (fileMode) Console.println(strY);
                y = Long.parseLong(strY);
                break;
            } catch (IOException exception) {
                Console.printError("Interrupted I/O operations!");
            } catch (NumberFormatException exception) {
                Console.printError("Y-coordinate must be a number!");
            } catch (NullPointerException | IllegalStateException exception) {
                Console.printError("Unexpected error!");
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
                Console.println("Enter minimal point > " + MINIMALPOINT_BIGGER_THAN +": ");
                Console.print(App.comPrompt);
                strMinimalPoint = userReader.readLine().trim();
                if (fileMode) Console.println(strMinimalPoint);
                if (strMinimalPoint.equals("")) throw new MustBeNotEmptyException();
                minimalPoint = Long.parseLong(strMinimalPoint);
                if (minimalPoint <= MINIMALPOINT_BIGGER_THAN) throw new NotInDeclaredLimitsException();
                break;
            } catch (IOException exception) {
                Console.printError("Interrupted I/O operations!");
            } catch (MustBeNotEmptyException exception) {
                Console.printError("Minimal Point can't be empty!");
            } catch (NotInDeclaredLimitsException exception) {
                Console.printError("Minimal point must be bigger than " + MINIMALPOINT_BIGGER_THAN + "!");
            } catch (NumberFormatException exception) {
                Console.printError("Minimal point must be a number!");
            } catch (NullPointerException | IllegalStateException exception) {
                Console.printError("Unexpected error!");
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
                Console.println("Enter tuned in works: ");
                Console.print(App.comPrompt);
                strTunedInWorks = userReader.readLine().trim();
                if (fileMode) Console.println(strTunedInWorks);
                if (strTunedInWorks.equals("")) throw new MustBeNotEmptyException();
                tunedInWorks = Integer.parseInt(strTunedInWorks);
                break;
            } catch (IOException exception) {
                Console.printError("Interrupted I/O operations!");
            } catch (MustBeNotEmptyException exception) {
                Console.printError("Tuned in works can't be empty!");
            } catch (NumberFormatException exception) {
                Console.printError("Tuned in works must be a number!");
            } catch (NullPointerException | IllegalStateException exception) {
                Console.printError("Unexpected error!");
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
                Console.println("Enter average point > " + AVERAGEPOINT_BIGGER_THAN + ": ");
                Console.print(App.comPrompt);
                strAveragePoint = userReader.readLine().trim();
                if (fileMode) Console.println(strAveragePoint);
                averagePoint = Integer.parseInt(strAveragePoint);
                if (averagePoint <= AVERAGEPOINT_BIGGER_THAN) throw new NotInDeclaredLimitsException();
                break;
            } catch (IOException exception) {
                Console.printError("Interrupted I/O operations!");
            } catch (NotInDeclaredLimitsException e) {
                Console.printError("Average point must be bigger than " + AVERAGEPOINT_BIGGER_THAN + "!");
            } catch (NumberFormatException exception) {
                Console.printError("Average point must be represented by number!");
            } catch (NullPointerException | IllegalStateException exception) {
                Console.printError("Unexpected error!");
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
                Console.println("List of difficulties: " + Difficulty.nameList());
                Console.println("Enter Difficulty: ");
                Console.print(App.comPrompt);
                strDifficulty = userReader.readLine().trim();
                if (fileMode) Console.println(strDifficulty);
                difficulty = Difficulty.valueOf(strDifficulty.toUpperCase());
                break;
            } catch (IOException exception) {
                Console.printError("Interrupted I/O operations!");
            } catch (IllegalArgumentException exception) {
                Console.printError("This difficulty not listed!");
            } catch (NullPointerException | IllegalStateException exception) {
                Console.printError("Unexpected error!");
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
                Console.println("Enter name of Discipline: ");
                Console.print(App.comPrompt);
                nameDiscipline = userReader.readLine().trim();
                if (fileMode) Console.println(nameDiscipline);
                if (nameDiscipline.equals("")) throw new MustBeNotEmptyException();
                break;
            } catch (MustBeNotEmptyException exception) {
                Console.printError("Name of discipline can't be empty!");
            } catch (IOException exception) {
                Console.printError("Interrupted I/O operations!");
            } catch (IllegalArgumentException exception) {
                Console.printError("This difficulty not listed!");
            } catch (NullPointerException | IllegalStateException exception) {
                Console.printError("Unexpected error!");
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
                Console.println("Enter the number of labs of Discipline: ");
                Console.print(App.comPrompt);
                strLabsCountDiscipline = userReader.readLine().trim();
                if (fileMode) Console.println(strLabsCountDiscipline);
//                if (strLabsCountDiscipline.equals("")) break;
                labsCountDiscipline = Integer.parseInt(strLabsCountDiscipline);
                break;
            } catch (NumberFormatException exception) {
                Console.printError("The number of labs must be a number!");
            } catch (IOException exception) {
                Console.printError("Interrupted I/O operations!");
            } catch (NullPointerException | IllegalStateException exception) {
                Console.printError("Unexpected error!");
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
                Console.printWarning("Are you sure? (yes/no)");
                Console.print(App.comPrompt);
                answer = userReader.readLine().trim();
                if (fileMode) Console.println(answer);
                switch (answer) {
                    case "yes":
                        return true;
                    case "no" :
                        return false;
                    default: Console.printWarning("Yes or no?");
                }
            }
        } catch (IOException exception) {
            Console.printError("Interrupted I/O operations!");
        } return false;
    }

    @Override
    public String toString() {
        return "LabAsker (class to ask the user about information of elements)";
    }
}
