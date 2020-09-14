package word_map;

import java.io.File;
import java.util.Comparator;

public class PartialFileComparator implements Comparator<File> {
	@Override
	public int compare(File o1, File o2) {
		return Integer.parseInt(o1.getName().split("_")[0]) - Integer.parseInt(o2.getName().split("_")[0]);
	}
}
