package shop.utils;

import java.util.List;

public class CollectionUtils {
    public static <T extends  Comparable<T>> T findMax(List<T> list) {
        if (list.isEmpty()) {
            throw new IllegalArgumentException("Cannot find max in empty list");
        }

        T result = list.getFirst();
        for (T temp : list) {
            if (result.compareTo(temp) < 0) {
                result = temp;
            }
        }

         return result;
    }
}