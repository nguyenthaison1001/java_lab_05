package utility;

import client.AppClient;
import data.Coordinates;
import data.Difficulty;
import data.Discipline;
import data.LabWork;
import exceptions.IncorrectInputInScriptException;
import exceptions.MustBeNotEmptyException;
import exceptions.NotInDeclaredLimitsException;

import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Operates questions to ask user about LabWork's value.
 */
public class LabAsker {
    private final Scanner userScanner;

    public LabAsker(Scanner userScanner) {
        this.userScanner = userScanner;
    }

    /**
     * @return userReader which uses for user input.
     */
    public Scanner getUserReader() {
        return userScanner;
    }

    /**
     * Asks a user the LabWork's name.
     * @return LabWork's name.
     */
    public String askName() throws IncorrectInputInScriptException {
        String name;
        try {
            Outputer.println("EnterName");
            Outputer.print(AppClient.PS2);
            name = userScanner.nextLine().trim();
            Outputer.println(name);
            if (name.equals("")) throw new MustBeNotEmptyException();
            return name;
        } catch (NoSuchElementException exception) {
            Outputer.printError("NameNotIdentifiedException");
        } catch (MustBeNotEmptyException exception) {
            Outputer.printError("NameEmptyException");
        } catch (IllegalStateException exception) {
            Outputer.printError("UnexpectedException");
            OutputerUI.error("UnexpectedException");
            System.exit(0);
        }
        throw new IncorrectInputInScriptException();
    }

    /**
     * Asks a user the LabWork's X coordinate.
     * @return LabWork's X coordinate.
     */
    public Integer askX() throws IncorrectInputInScriptException {
        String strX;
        int x;
        try {
            Outputer.println("EnterX", String.valueOf(LabWork.MIN_X));
            Outputer.print(AppClient.PS2);
            strX = userScanner.nextLine().trim();
            Outputer.println(strX);
            if (strX.equals("")) throw new MustBeNotEmptyException();
            x = Integer.parseInt(strX);
            if (x <= LabWork.MIN_X) throw new NotInDeclaredLimitsException();
            return x;
        } catch (NoSuchElementException exception) {
            Outputer.printError("XNotIdentifiedException");
        } catch (MustBeNotEmptyException exception) {
            Outputer.printError("XEmptyException");
        } catch (NotInDeclaredLimitsException exception) {
            Outputer.printError("XMustBeGreaterException", String.valueOf(LabWork.MIN_X));
        } catch (NumberFormatException exception) {
            Outputer.printError("XMustBeNumberException");
        } catch (NullPointerException | IllegalStateException exception) {
            Outputer.printError("UnexpectedException");
            OutputerUI.error("UnexpectedException");
            System.exit(0);
        }
        throw new IncorrectInputInScriptException();
    }

    /**
     * Asks a user the LabWork's Y coordinate.
     * @return LabWork's Y coordinate.
     */
    public long askY() throws IncorrectInputInScriptException {
        String strY;
        long y;
        try {
            Outputer.println("EnterY");
            Outputer.print(AppClient.PS2);
            strY = userScanner.nextLine().trim();
            Outputer.println(strY);
            y = Long.parseLong(strY);
            return y;
        } catch (NoSuchElementException exception) {
            Outputer.printError("YNotIdentifiedException");
        } catch (NumberFormatException exception) {
            Outputer.printError("YMustBeNumberException");
        } catch (NullPointerException | IllegalStateException exception) {
            Outputer.printError("UnexpectedException");
            OutputerUI.error("UnexpectedException");
            System.exit(0);
        }
        throw new IncorrectInputInScriptException();
    }

    /**
     * Asks a user the LabWork's coordinate.
     * @return LabWork's coordinate.
     */
    public Coordinates askCoordinates() throws IncorrectInputInScriptException {
        Integer x = askX();
        long y = askY();
        return new Coordinates(x, y);
    }

    /**
     * Asks a user the LabWork's MinimalPoint.
     * @return LabWork's MinimalPoint.
     */
    public long askMinimalPoint() throws IncorrectInputInScriptException {
        String strMinimalPoint;
        long minimalPoint;
        try {
            Outputer.println("EnterMinimalPoint", String.valueOf(LabWork.MINIMALPOINT_BIGGER_THAN + 1));
            Outputer.print(AppClient.PS2);
            strMinimalPoint = userScanner.nextLine().trim();
            Outputer.println(strMinimalPoint);
            if (strMinimalPoint.equals("")) throw new MustBeNotEmptyException();
            minimalPoint = Long.parseLong(strMinimalPoint);
            if (minimalPoint <= LabWork.MINIMALPOINT_BIGGER_THAN) throw new NotInDeclaredLimitsException();
            return minimalPoint;
        } catch (MustBeNotEmptyException exception) {
            Outputer.printError("MinimalPointMustBeNotEmptyException");
        } catch (NotInDeclaredLimitsException exception) {
            Outputer.printError("MinimalPointMustBeBiggerException", String.valueOf(LabWork.MINIMALPOINT_BIGGER_THAN));
        } catch (NumberFormatException exception) {
            Outputer.printError("MinimalPointMustBeNumberException");
        } catch (NullPointerException | IllegalStateException exception) {
            Outputer.printError("UnexpectedException");
            OutputerUI.error("UnexpectedException");
            System.exit(0);
        }
        throw new IncorrectInputInScriptException();
    }

    /**
     * Asks a user the LabWork's TunedInWorks.
     * @return LabWork's TunedInWorks.
     */
    public int askTunedInWorks() throws IncorrectInputInScriptException {
        String strTunedInWorks;
        int tunedInWorks;
        try {
            Outputer.println("EnterTunedInWorks");
            Outputer.print(AppClient.PS2);
            strTunedInWorks = userScanner.nextLine().trim();
            Outputer.println(strTunedInWorks);
            if (strTunedInWorks.equals("")) throw new MustBeNotEmptyException();
            tunedInWorks = Integer.parseInt(strTunedInWorks);
            return tunedInWorks;
        } catch (NoSuchElementException exception) {
            Outputer.printError("TunedInWorksNotIdentifiedException");
        } catch (MustBeNotEmptyException exception) {
            Outputer.printError("TunedInWorksMustBeNotEmptyException");
        } catch (NumberFormatException exception) {
            Outputer.printError("TunedInWorksMustBeNumberException");
        } catch (NullPointerException | IllegalStateException exception) {
            Outputer.printError("UnexpectedException");
            OutputerUI.error("UnexpectedException");
            System.exit(0);
        }
        throw new IncorrectInputInScriptException();
    }

    /**
     * Asks a user the LabWork's AveragePoint.
     * @return LabWork's AveragePoint.
     */
    public int askAveragePoint() throws IncorrectInputInScriptException {
        String strAveragePoint;
        int averagePoint;
        try {
            Outputer.println("EnterAveragePoint", String.valueOf(LabWork.AVERAGEPOINT_BIGGER_THAN + 1));
            Outputer.print(AppClient.PS2);
            strAveragePoint = userScanner.nextLine().trim();
            Outputer.println(strAveragePoint);
            averagePoint = Integer.parseInt(strAveragePoint);
            if (averagePoint <= LabWork.AVERAGEPOINT_BIGGER_THAN) throw new NotInDeclaredLimitsException();
            return averagePoint;
        } catch (NoSuchElementException exception) {
            Outputer.printError("AveragePointNotIdentifiedException");
        } catch (NotInDeclaredLimitsException e) {
            Outputer.printError("AveragePointMustBeBiggerException", String.valueOf(LabWork.AVERAGEPOINT_BIGGER_THAN));
        } catch (NumberFormatException exception) {
            Outputer.printError("AveragePointMustBeNumberException");
        } catch (NullPointerException | IllegalStateException exception) {
            Outputer.printError("UnexpectedException");
            OutputerUI.error("UnexpectedException");
            System.exit(0);
        }
        throw new IncorrectInputInScriptException();
    }

    /**
     * Asks a user the LabWork's Difficulty.
     * @return LabWork's Difficulty.
     */
    public Difficulty askDifficulty() throws IncorrectInputInScriptException {
        String strDifficulty;
        Difficulty difficulty;
        try {
            Outputer.println("DifficultyList" + Difficulty.nameList());
            Outputer.println("EnterDifficulty");
            Outputer.print(AppClient.PS2);
            strDifficulty = userScanner.nextLine().trim();
            Outputer.println(strDifficulty);
            difficulty = Difficulty.valueOf(strDifficulty.toUpperCase());
            return difficulty;
        } catch (NoSuchElementException exception) {
            Outputer.printError("DifficultyNotIdentifiedException");
        } catch (IllegalArgumentException exception) {
            Outputer.printError("NoSuchDifficultyException");
        } catch (NullPointerException | IllegalStateException exception) {
            Outputer.printError("UnexpectedException");
            OutputerUI.error("UnexpectedException");
            System.exit(0);
        }
        throw new IncorrectInputInScriptException();
    }

    /**
     * Asks a user the LabWork's NameOfDiscipline.
     * @return LabWork's NameOfDiscipline.
     */
    public String askDisciplineName() throws IncorrectInputInScriptException {
        String disciplineName;
        try {
            Outputer.println("EnterDisciplineName");
            Outputer.print(AppClient.PS2);
            disciplineName = userScanner.nextLine().trim();
            Outputer.println();
            if (disciplineName.equals("")) throw new MustBeNotEmptyException();
            return disciplineName;
        } catch (MustBeNotEmptyException exception) {
            Outputer.printError("DisciplineNameMustBeNotEmptyException");
        } catch (NoSuchElementException exception) {
            Outputer.printError("DisciplineNameNotIdentifiedException");
        } catch (NullPointerException | IllegalStateException exception) {
            Outputer.printError("UnexpectedException");
            OutputerUI.error("UnexpectedException");
            System.exit(0);
        }
        throw new IncorrectInputInScriptException();
    }

    /**
     * Asks a user the LabWork's MinimalPoint.
     * @return LabWork's MinimalPoint.
     */
    public int askLabsCountOfDiscipline() throws IncorrectInputInScriptException {
        String strLabsCountDiscipline;
        int labsCountDiscipline;
        try {
            Outputer.println("EnterLabsCount");
            Outputer.print(AppClient.PS2);
            strLabsCountDiscipline = userScanner.nextLine().trim();
            Outputer.println(strLabsCountDiscipline);
            labsCountDiscipline = Integer.parseInt(strLabsCountDiscipline);
            return labsCountDiscipline;
        } catch (NoSuchElementException exception) {
            Outputer.printError("LabsCountNotIdentifiedException");
        } catch (NumberFormatException exception) {
            Outputer.printError("LabsCountMustBeNumberException");
        } catch (NullPointerException | IllegalStateException exception) {
            Outputer.printError("UnexpectedException");
            OutputerUI.error("UnexpectedException");
            System.exit(0);
        }
        throw new IncorrectInputInScriptException();
}

    /**
     * Asks a user the LabWork's Discipline.
     * @return LabWork's Discipline.
     */
    public Discipline askDiscipline() throws IncorrectInputInScriptException {
        String nameDiscipline = askDisciplineName();
        int labsCount = askLabsCountOfDiscipline();
        return new Discipline(nameDiscipline, labsCount);
    }

    /**
     * Asks a user sure or not.
     * @return true/false.
     */
    public boolean areYouSure() {
        String answer;
        while (true) {
            Outputer.printWarning("Are you sure? (yes/no)");
            Outputer.print(AppClient.PS2);
            answer = userScanner.nextLine().trim();
            switch (answer) {
                case "yes":
                    return true;
                case "no" :
                    return false;
                default: Outputer.printWarning("Yes or no?");
            }
        }
    }

    @Override
    public String toString() {
        return "LabAsker (class to ask the user about information of elements)";
    }
}
