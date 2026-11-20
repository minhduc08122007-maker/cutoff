public class Insertion {

    // Sắp xếp toàn bộ mảng
    public static void sort(Comparable[] a) {
        int n = a.length;
        sort(a, 0, n - 1);
    }

    // Sắp xếp đoạn mảng con a[lo..hi] (dùng cho cutoff của MergeSort)
    public static void sort(Comparable[] a, int lo, int hi) {
        for (int i = lo + 1; i <= hi; i++) {
            for (int j = i; j > lo && less(a[j], a[j - 1]); j--) {
                exch(a, j, j - 1);
            }
        }
    }

    // Kiểm tra v < w ?
    private static boolean less(Comparable v, Comparable w) {
        return v.compareTo(w) < 0;
    }

    // Hoán đổi a[i] và a[j]
    private static void exch(Object[] a, int i, int j) {
        Object swap = a[i];
        a[i] = a[j];
        a[j] = swap;
    }
}