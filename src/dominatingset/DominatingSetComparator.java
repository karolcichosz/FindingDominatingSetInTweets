package dominatingset;

import java.util.Comparator;
import java.util.Set;

// @author Karol Cichosz

public class DominatingSetComparator implements Comparator<Set<Integer>> {
	@Override
	//used for PriorityQueue to calculate the priority of particular dominating sets
    public int compare(Set<Integer> x, Set<Integer> y)
    {
        if (x.size() < y.size())
        {
            return -1;
        }
        if (x.size() > y.size())
        {
            return 1;
        }
        return 0;
    }
}
