package DataStructures;

import java.util.LinkedList;

public class MinMaxSortedList<T extends Comparable<T>> extends LinkedList<T> {
	private static final long serialVersionUID = 5367323191633958708L;

	public MinMaxSortedList () {
		super();
	}
	
	public boolean add (T t) {
		if (this.size()==0) return super.add(t);
		else {
			int position=0;
			for (;position<this.size();position++) {
				if (t.compareTo(get(position))<0) {
					break;
				}
			}
			super.add(position,t);
			return true;
		}
	}

}
