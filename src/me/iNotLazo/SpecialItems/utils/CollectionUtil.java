package me.iNotLazo.SpecialItems.utils;

import com.google.common.base.*;
import java.util.*;
import java.util.stream.*;

public class CollectionUtil
{
    public static String andJoin(final Collection<String> collection, final boolean delimiterBeforeAnd) {
        return andJoin(collection, delimiterBeforeAnd, ", ");
    }
    
    public static String andJoin(final Collection<String> collection, final boolean delimiterBeforeAnd, final String delimiter) {
        Preconditions.checkNotNull((Object)collection, (Object)"Collection cannot be null");
        Preconditions.checkNotNull((Object)delimiter, (Object)"Delimiter cannot be null");
        final ArrayList<String> contents = new ArrayList<String>(collection);
        final String lastContent = contents.remove(contents.size() - 1);
        final StringBuilder stringBuilder = new StringBuilder(Joiner.on(delimiter).join((Iterable)contents));
        if (delimiterBeforeAnd) {
            stringBuilder.append(delimiter);
        }
        return stringBuilder.append(" and ").append(lastContent).toString();
    }
    
    public static List<String> getCompletions(final String[] arguments, final List<String> result) {
        return getCompletions(arguments, result, 80);
    }
    
    public static List<String> getCompletions(final String[] arguments, final List<String> result, final int limit) {
        Preconditions.checkNotNull((Object)arguments, (Object)"Argument cannot be null");
        Preconditions.checkArgument(arguments.length != 0);
        Preconditions.checkNotNull((Object)result, (Object)"List cannot be null");
        final String argument = arguments[arguments.length - 1];
        return result.stream().filter(string -> string.regionMatches(true, 0, argument, 0, argument.length())).limit(limit).collect(Collectors.toList());
    }
}
