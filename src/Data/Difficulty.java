package Data;

/**
 * Enum of Difficulty
 */
public enum Difficulty {
    NORMAL,
    HARD,
    VERY_HARD,
    HOPELESS,
    TERRIBLE;

    /**
     * @return List of Difficulty
     */
    public static String nameList() {
        StringBuilder nameList = new StringBuilder();
        for (Difficulty difficulty : values()) {
            nameList.append(difficulty.name()).append(", ");
        }
        return nameList.substring(0, nameList.length()-2);
    }
}
