package kz.kazgeowarning.authgateway.util.functions;

import org.springframework.data.domain.Sort;

public class CommonFunctions {

    public static Sort sortTable(String ordering){
        Sort sorting = new Sort(Sort.Direction.ASC, "id");

        if (ordering != null) {
            sorting = ordering.charAt(0) != '-'
                    // NOTE: ordering.equals('attr') --> ASC by 'attr'
                    ? new Sort(Sort.Direction.ASC, ordering)
                    // NOTE: ordering.equals('-attr') --> DESC by 'attr'
                    : new Sort(Sort.Direction.DESC, ordering.substring(1));
        }

        return sorting;
    }

    public static Sort sortTable(String defaultProp, Sort.Direction direction, String ordering){
        Sort sorting = new Sort(direction, defaultProp);

        if (ordering != null) {
            sorting = ordering.charAt(0) != '-'
                    // NOTE: ordering.equals('attr') --> ASC by 'attr'
                    ? new Sort(Sort.Direction.ASC, ordering)
                    // NOTE: ordering.equals('-attr') --> DESC by 'attr'
                    : new Sort(Sort.Direction.DESC, ordering.substring(1));
        }

        return sorting;
    }

    public static int countWordsUsingSplit(String input) {
        if (input == null || input.isEmpty()) {
            return 0;
        }

        String[] words = input.split("\\s+");
        return words.length;
    }
}
