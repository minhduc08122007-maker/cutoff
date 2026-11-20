public class Merge {

    // Ngưỡng mặc định theo đề xuất của Sedgewick & Wayne
    private static final int DEFAULT_CUTOFF = 7;

    // Hàm gọi mặc định
    public static void sort(Comparable[] a) {
        sort(a, DEFAULT_CUTOFF);
    }

    // Hàm gọi cho phép truyền tham số cutoff tùy chỉnh
    public static void sort(Comparable[] a, int cutoff) {
        Comparable[] aux = new Comparable[a.length];
        sort(a, aux, 0, a.length - 1, cutoff);
    }

    // Đệ quy sắp xếp mảng con a[lo..hi]
    private static void sort(Comparable[] a, Comparable[] aux, int lo, int hi, int cutoff) {
        // Cải tiến 1: Dùng Insertion Sort cho mảng con có độ dài nhỏ hơn cutoff
        // Lưu ý: độ dài mảng con a[lo..hi] là (hi - lo + 1)
        if (hi - lo + 1 < cutoff) {
            Insertion.sort(a, lo, hi);
            return;
        }

        int mid = lo + (hi - lo) / 2;
        sort(a, aux, lo, mid, cutoff);
        sort(a, aux, mid + 1, hi, cutoff);

        // Cải tiến 2 (tùy chọn chuẩn sách): Bỏ qua merge nếu mảng đã có thứ tự
        if (!less(a[mid + 1], a[mid])) return;

        merge(a, aux, lo, mid, hi);
    }

    // Trộn hai mảng con đã sắp xếp a[lo..mid] và a[mid+1..hi]
    private static void merge(Comparable[] a, Comparable[] aux, int lo, int mid, int hi) {
        // Sao chép sang mảng phụ aux[]
        for (int k = lo; k <= hi; k++) {
            aux[k] = a[k];
        }

        // Trộn ngược lại vào mảng chính a[]
        int i = lo, j = mid + 1;
        for (int k = lo; k <= hi; k++) {
            if      (i > mid)              a[k] = aux[j++];
            else if (j > hi)               a[k] = aux[i++];
            else if (less(aux[j], aux[i])) a[k] = aux[j++];
            else                           a[k] = aux[i++];
        }
    }

    // Kiểm tra v < w ?
    private static boolean less(Comparable v, Comparable w) {
        return v.compareTo(w) < 0;
    }
}