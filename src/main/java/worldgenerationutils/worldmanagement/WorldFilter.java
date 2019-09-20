package worldgenerationutils.worldmanagement;

import java.io.File;
import java.util.function.BiPredicate;

@FunctionalInterface
public interface WorldFilter extends BiPredicate<File, Boolean>
{
    @Override
    boolean test(File worldName, Boolean hasGenerator);
}
