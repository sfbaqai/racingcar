package solo;

import java.util.Comparator;


public class Arrays {
	public static void quicksort(double[] a) {
		quicksort(a, 0, a.length - 1);
	}

	// quicksort a[left] to a[right]
	public static void quicksort(double[] a, int left, int right) {
		if (right <= left) return;
		int i = partition(a, left, right);
		quicksort(a, left, i-1);
		quicksort(a, i+1, right);
	}

	// partition a[left] to a[right], assumes left < right
	private static int partition(double[] a, int left, int right) {
		int i = left - 1;
		int j = right;
		while (true) {
			while (a[++i]< a[right])      // find item on left to swap
				;                               // a[right] acts as sentinel
			while (a[right]< a[--j])      // find item on right to swap
				if (j == left) break;           // don't go out-of-bounds
			if (i >= j) break;                  // check if pointers cross            
			double tmp = a[i];
			a[i] = a[j];
			a[j] =tmp;
		}
		double tmp = a[i];
		a[i] = a[right];
		a[right] =tmp;
		return i;
	}

//	public static void quicksort(double[] a,Swapper swapper) {
//		quicksort(a, 0, a.length - 1,swapper);
//	}
//
//	// quicksort a[left] to a[right]
//	public static void quicksort(double[] a, int left, int right,Swapper swapper) {
//		if (right <= left) return;
//		int i = partition(a, left, right,swapper);
//		quicksort(a, left, i-1,swapper);
//		quicksort(a, i+1, right,swapper);
//	}
//
//	// partition a[left] to a[right], assumes left < right
//	private static int partition(double[] a, int left, int right,Swapper swapper) {
//		int i = left - 1;
//		int j = right;
//		while (true) {
//			while (a[++i]< a[right])      // find item on left to swap
//				;                               // a[right] acts as sentinel
//			while (a[right]< a[--j])      // find item on right to swap
//				if (j == left) break;           // don't go out-of-bounds
//			if (i >= j) break;                  // check if pointers cross            
//			swapper.swap(i, j);
//		}
//		swapper.swap(i, right);
//		return i;
//	}


	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static int compare(Object a, Object b,Comparator c){
		if (c!=null) return c.compare(a, b);
		if (a instanceof Comparable)
			return ((Comparable) a).compareTo(b);
		if (b instanceof Comparable)
			return -((Comparable) b).compareTo(a);
		return 0;
	}

	@SuppressWarnings({ "rawtypes" })
	public static void quicksort(Object[] a,Comparator c) {
		quicksort(a, 0, a.length - 1,c);
	}

	// quicksort a[left] to a[right]	
	@SuppressWarnings("rawtypes")
	public static void quicksort(Object[] a, int left, int right,Comparator c) {
		if (right <= left) return;
		int i = partition(a, left, right,c);
		quicksort(a, left, i-1,c);
		quicksort(a, i+1, right,c);
	}

	// partition a[left] to a[right], assumes left < right	
	@SuppressWarnings("rawtypes")
	private static int partition(Object[] a, int left, int right,Comparator c) {
		int i = left - 1;
		int j = right;
		while (true) {
			while (compare(a[++i],a[right],c)<0)      // find item on left to swap
				;                               // a[right] acts as sentinel
			while (compare(a[right],a[--j],c)<0)      // find item on right to swap
				if (j == left) break;           // don't go out-of-bounds
			if (i >= j) break;                  // check if pointers cross            
			Object tmp = a[i];
			a[i] = a[j];
			a[j] =tmp;
		}
		Object tmp = a[i];
		a[i] = a[right];
		a[right] =tmp;
		return i;
	}

	
//	@SuppressWarnings("rawtypes")
//	public static void quicksort(Object[] a,Swapper swapper,Comparator c) {
//		quicksort(a, 0, a.length - 1,swapper,c);
//	}
//
//	// quicksort a[left] to a[right]	
//	@SuppressWarnings("rawtypes")
//	public static void quicksort(Object[] a, int left, int right,Swapper swapper,Comparator c) {
//		if (right <= left) return;
//		int i = partition(a, left, right,swapper,c);
//		quicksort(a, left, i-1,swapper,c);
//		quicksort(a, i+1, right,swapper,c);
//	}
//
//	// partition a[left] to a[right], assumes left < right	
//	@SuppressWarnings("rawtypes")
//	private static int partition(Object[] a, int left, int right,Swapper swapper,Comparator c) {
//		int i = left - 1;
//		int j = right;
//		while (true) {
//			while (compare(a[++i],a[right],c)<0)      // find item on left to swap
//				;                               // a[right] acts as sentinel
//			while (compare(a[right], a[--j],c)<0)      // find item on right to swap
//				if (j == left) break;           // don't go out-of-bounds
//			if (i >= j) break;                  // check if pointers cross            
//			swapper.swap(i, j);
//		}
//		swapper.swap(i, right);
//		return i;
//	}

}
