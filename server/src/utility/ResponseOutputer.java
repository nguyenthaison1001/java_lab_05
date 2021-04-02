package utility;

public class ResponseOutputer {
    private static StringBuilder stringBuilder = new StringBuilder();

    public static void appendln(Object toOut) {
        stringBuilder.append(toOut + "\n");
    }

    public static void appendError(Object toOut) {
        stringBuilder.append("error: " + toOut + "\n");
    }

    public static void appendTable(Object element1, Object element2) {
        stringBuilder.append(String.format("%-40s%-1s%n", element1, element2));
    }

    public static void appendWarning(Object toOut) {
        stringBuilder.append("warning: " + toOut + "\n");
    }

    public static String getAndClearBuffer() {
        String toReturn = stringBuilder.toString();
        stringBuilder.delete(0, stringBuilder.length());
        return toReturn;
    }

}
