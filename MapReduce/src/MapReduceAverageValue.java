import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

public class MapReduceAverageValue {

    // ����� �������� ������ ��������� n
    public static int[] createArray(int n) {
        int[] array = new int[n];

        for (int i = 0; i < n; i++) {
            array[i] = i + 1;
        }

        return array;
    }

    // ����� ��� �������� ������� �� ������� ��� �������� ���������� �������
    public static List<int[]> map(int[] array, int numThreads){
        List<int[]> chunkedArrays = new ArrayList<>(); // ������ ��� ��������� ������ ������
        int sizeChunk = array.length / numThreads; // ����� ����� �������

        for(int i = 0; i < array.length; i += sizeChunk) {
            int end = Math.min(array.length, i + sizeChunk); // ���������� ���� ��������
            chunkedArrays.add(Arrays.copyOfRange(array, i, end)); // ��������� �������� �� ������
        }

        return chunkedArrays; // ��������� ������ ��������
    }

    // ����� ��� ������������ ���������� ���������� �������� ������� �������� �� �� ��'������
    public static double reduce(List<int[]> chunkedArrays) throws InterruptedException, ExecutionException{
        ExecutorService executorService = Executors.newFixedThreadPool(chunkedArrays.size()); // ��� ������
        List<Callable<Double>> threads = new ArrayList<>(); // ������ ������ ��� ������������ ���������

        // ��������� ������� ������� ��� ���������� ���������� �������� �� ��������� ������ �� ����
        for(int [] array: chunkedArrays){
            threads.add(() -> Arrays.stream(array).average().orElse(0.0));
        }

        List<Future<Double>> futures = executorService.invokeAll(threads); // ������ ��� ������ �� ��������� ����������
        double totalSum = 0;

        for(Future<Double> future: futures) {
            totalSum += future.get(); // ������� ���������� ��������
        }

        executorService.shutdown(); // ��������� ��� ������

        return totalSum / chunkedArrays.size(); // ��������� ������� ��������
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        int numThreads = Runtime.getRuntime().availableProcessors(); // ������� ��������� ��������� (4)
        int[] array = createArray(10000000);

        long start = System.currentTimeMillis();
        List<int[]> chunkedArrays = map(array, 4);
        double mapReduceResult = reduce(chunkedArrays);
        long end = System.currentTimeMillis();

        System.out.println("MapReduce average value: " + mapReduceResult);
        System.out.println("Elapsed time: " + (end - start) + "ms");
        // ��������: 10000, ������: 3, ���: 18��, 18��, 16��
        // ��������: 10000000, ������: 4, ���: 94��, 114��, 92��
    }
}
