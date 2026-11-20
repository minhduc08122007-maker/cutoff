import java.util.Arrays;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // 1. Nhập các tham số cấu hình bộ test
        System.out.print("Nhập số lượng test case cần sinh: ");
        int numTests = scanner.nextInt();

        System.out.print("Nhập số lượng phần tử tối thiểu (minSize): ");
        int minSize = scanner.nextInt();

        System.out.print("Nhập số lượng phần tử tối đa (maxSize): ");
        int maxSize = scanner.nextInt();

        System.out.print("Nhập giá trị nhỏ nhất của phần tử (minVal): ");
        int minVal = scanner.nextInt();

        System.out.print("Nhập giá trị lớn nhất của phần tử (maxVal): ");
        int maxVal = scanner.nextInt();

        System.out.print("Nhập ngưỡng cutoff cho MergeSort (vd: 7): ");
        int cutoff = scanner.nextInt();

        scanner.close();

        // 2. Khởi tạo TestGenerator và sinh đúng numTests bộ test
        TestGenerator generator = new TestGenerator();
        Integer[][] testCases = generator.generateBatch(
                numTests, minSize, maxSize, minVal, maxVal, TestGenerator.CaseType.RANDOM
        );

        // Làm nóng JVM (Warm-up) để tránh sai số thời gian ở test case đầu tiên
        warmUp();

        // 3. Chạy và đo thời gian từng test case
        System.out.println("\n=================================================================================");
        System.out.printf("%-8s | %-10s | %-18s | %-18s | %-10s%n",
                "Test #", "Kích thước", "MergeSort (ms)", "InsertionSort (ms)", "Trạng thái");
        System.out.println("---------------------------------------------------------------------------------");

        double totalMergeTimeMs = 0;
        double totalInsertionTimeMs = 0;
        int passedCount = 0;

        for (int i = 0; i < numTests; i++) {
            Integer[] original = testCases[i];

            // BẮT BUỘC sao chép ra 2 mảng độc lập vì cả hai thuật toán đều sắp xếp tại chỗ (in-place)
            Integer[] arrForMerge = Arrays.copyOf(original, original.length);
            Integer[] arrForInsertion = Arrays.copyOf(original, original.length);

            // Chạy MergeSort với tham số cutoff
            long startMerge = System.nanoTime();
            Merge.sort(arrForMerge, cutoff);
            double mergeTimeMs = (System.nanoTime() - startMerge) / 1_000_000.0;

            // Chạy InsertionSort
            long startInsertion = System.nanoTime();
            Insertion.sort(arrForInsertion);
            double insertionTimeMs = (System.nanoTime() - startInsertion) / 1_000_000.0;

            // Kiểm tra tính đúng đắn của kết quả
            boolean isValid = isSorted(arrForMerge)
                    && isSorted(arrForInsertion)
                    && Arrays.equals(arrForMerge, arrForInsertion);

            if (isValid) passedCount++;
            totalMergeTimeMs += mergeTimeMs;
            totalInsertionTimeMs += insertionTimeMs;

            System.out.printf("%-8d | %-10d | %-18.4f | %-18.4f | %-10s%n",
                    (i + 1), original.length, mergeTimeMs, insertionTimeMs, (isValid ? "PASS" : "FAIL"));
        }

        // 4. Thống kê tổng hợp
        System.out.println("=================================================================================");
        System.out.printf("Kết quả kiểm thử: %d/%d test cases hợp lệ.%n", passedCount, numTests);
        if (numTests > 0) {
            System.out.printf("Thời gian trung bình MergeSort (cutoff=%d): %.4f ms%n",
                    cutoff, totalMergeTimeMs / numTests);
            System.out.printf("Thời gian trung bình InsertionSort:          %.4f ms%n",
                    totalInsertionTimeMs / numTests);
        }
    }

    // Hàm kiểm tra mảng đã được sắp xếp tăng dần hay chưa (chuẩn Sedgewick & Wayne)
    private static boolean isSorted(Comparable[] a) {
        for (int i = 1; i < a.length; i++) {
            if (a[i].compareTo(a[i - 1]) < 0) {
                return false;
            }
        }
        return true;
    }

    // Chạy nháp trên mảng nhỏ để JVM nạp sẵn bytecode trước khi đo thời gian thực
    private static void warmUp() {
        Integer[] dummy = { 5, 2, 9, 1, 5, 6 };
        Merge.sort(Arrays.copyOf(dummy, dummy.length), 7);
        Insertion.sort(Arrays.copyOf(dummy, dummy.length));
    }
}
