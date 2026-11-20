import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class TestGenerator {

    // Các kịch bản dữ liệu đầu vào phổ biến để kiểm thử thuật toán sắp xếp
    public enum CaseType {
        RANDOM,          // Ngẫu nhiên hoàn toàn
        SORTED,          // Đã sắp xếp tăng dần (Best-case cho Insertion Sort / Merge cải tiến)
        REVERSE_SORTED,  // Sắp xếp giảm dần (Worst-case cho Insertion Sort)
        NEARLY_SORTED,   // Gần như đã sắp xếp (chỉ xáo trộn khoảng 5% phần tử)
        FEW_UNIQUE       // Nhiều phần tử trùng lặp (chỉ sinh từ 3 đến 5 giá trị phân biệt)
    }

    private final Random random;

    // Constructor mặc định (mỗi lần chạy sẽ sinh dữ liệu khác nhau)
    public TestGenerator() {
        this.random = new Random();
    }

    // Constructor nhận seed cố định (giúp tái lập lại chính xác cùng một bộ test để debug/benchmark)
    public TestGenerator(long seed) {
        this.random = new Random(seed);
    }

    /**
     * Sinh mảng dữ liệu ngẫu nhiên cơ bản
     *
     * @param minSize Số lượng phần tử tối thiểu (>= 0)
     * @param maxSize Số lượng phần tử tối đa (>= minSize)
     * @param minVal  Giá trị nhỏ nhất của phần tử
     * @param maxVal  Giá trị lớn nhất của phần tử (>= minVal)
     * @return Mảng Integer[] đáp ứng các ràng buộc
     */
    public Integer[] generate(int minSize, int maxSize, int minVal, int maxVal) {
        return generate(minSize, maxSize, minVal, maxVal, CaseType.RANDOM);
    }

    /**
     * Sinh mảng dữ liệu theo kịch bản chỉ định
     */
    public Integer[] generate(int minSize, int maxSize, int minVal, int maxVal, CaseType type) {
        validateParameters(minSize, maxSize, minVal, maxVal);

        // 1. Sinh ngẫu nhiên kích thước mảng trong đoạn [minSize, maxSize]
        int size = random.nextInt(maxSize - minSize + 1) + minSize;
        Integer[] arr = new Integer[size];

        if (size == 0) {
            return arr;
        }

        // 2. Sinh giá trị cho từng phần tử trong đoạn [minVal, maxVal]
        if (type == CaseType.FEW_UNIQUE) {
            // Tạo tập nhỏ tối đa 5 giá trị mẫu trong khoảng [minVal, maxVal]
            int poolSize = Math.min(5, maxVal - minVal + 1);
            int[] pool = new int[poolSize];
            for (int i = 0; i < poolSize; i++) {
                pool[i] = randomInt(minVal, maxVal);
            }
            for (int i = 0; i < size; i++) {
                arr[i] = pool[random.nextInt(poolSize)];
            }
        } else {
            for (int i = 0; i < size; i++) {
                arr[i] = randomInt(minVal, maxVal);
            }
        }

        // 3. Biến đổi cấu trúc mảng theo kịch bản yêu cầu
        switch (type) {
            case SORTED:
                Arrays.sort(arr);
                break;

            case REVERSE_SORTED:
                Arrays.sort(arr, Collections.reverseOrder());
                break;

            case NEARLY_SORTED:
                Arrays.sort(arr);
                // Hoán đổi ngẫu nhiên khoảng 5% số cặp phần tử
                int swaps = Math.max(1, size / 20);
                for (int i = 0; i < swaps; i++) {
                    int idx1 = random.nextInt(size);
                    int idx2 = random.nextInt(size);
                    Integer temp = arr[idx1];
                    arr[idx1] = arr[idx2];
                    arr[idx2] = temp;
                }
                break;

            case RANDOM:
            case FEW_UNIQUE:
            default:
                // Giữ nguyên thứ tự ngẫu nhiên ban đầu
                break;
        }

        return arr;
    }

    /**
     * Sinh một lô gồm nhiều bộ test cùng lúc
     */
    public Integer[][] generateBatch(int numTests, int minSize, int maxSize, int minVal, int maxVal, CaseType type) {
        if (numTests < 0) {
            throw new IllegalArgumentException("Số lượng bộ test không được âm.");
        }
        Integer[][] batch = new Integer[numTests][];
        for (int i = 0; i < numTests; i++) {
            batch[i] = generate(minSize, maxSize, minVal, maxVal, type);
        }
        return batch;
    }

    // Sinh số nguyên trong đoạn [min, max] an toàn, tránh tràn số nguyên (Integer Overflow) khi max - min > Integer.MAX_VALUE
    private int randomInt(int min, int max) {
        long range = (long) max - (long) min + 1L;
        return (int) (min + (long) (random.nextDouble() * range));
    }

    // Kiểm tra tính hợp lệ của tham số đầu vào
    private void validateParameters(int minSize, int maxSize, int minVal, int maxVal) {
        if (minSize < 0) {
            throw new IllegalArgumentException("minSize phải lớn hơn hoặc bằng 0.");
        }
        if (minSize > maxSize) {
            throw new IllegalArgumentException("minSize (" + minSize + ") không được lớn hơn maxSize (" + maxSize + ").");
        }
        if (minVal > maxVal) {
            throw new IllegalArgumentException("minVal (" + minVal + ") không được lớn hơn maxVal (" + maxVal + ").");
        }
    }
}