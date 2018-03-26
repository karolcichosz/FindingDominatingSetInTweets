package util;

import java.util.Iterator;
import java.util.Random;
import java.util.Set;
//@author Karol Cichosz
public class RandSet {
	public static <T> T getRandomElement(Set<T> set) {
		Random random = new Random(System.currentTimeMillis());
		
		int selectedNum = random.nextInt(set.size());
		
		Iterator<T> setInterator = set.iterator();
		
		for (int i=0;i<selectedNum-1;i++) {
			setInterator.next();
		}
		
		return setInterator.next();
	}

	
	
}
