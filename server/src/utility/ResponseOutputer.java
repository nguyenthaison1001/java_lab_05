package utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ResponseOutputer {
    private static final StringBuilder stringBuilder = new StringBuilder();
    private static final List<String> argList = new ArrayList<>();

    public static void append(Object toOut) {
        stringBuilder.append(toOut);
    }

    public static void appendError(Object toOut) {
        stringBuilder.append("error: ").append(toOut);
    }

    public static void appendArgs(String... args) {
        argList.addAll(Arrays.asList(args));
    }

    public static void appendTable(Object element1, Object element2) {
        stringBuilder.append(String.format("%-40s%-1s%n", element1, element2));
    }

    public static void appendWarning(Object toOut) {
        stringBuilder.append("warning: ").append(toOut);
    }

    public static String getAndClearBuffer() {
        String toReturn = stringBuilder.toString();
        stringBuilder.delete(0, stringBuilder.length());
        return toReturn;
    }

    public static String[] getArgsAndClear() {
        String[] argsAsArray = new String[argList.size()];
        argsAsArray = argList.toArray(argsAsArray);
        argList.clear();
        return argsAsArray;
    }
}
