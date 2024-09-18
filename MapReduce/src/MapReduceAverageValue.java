import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

public class MapReduceAverageValue {

    // Standard method for search average value
    public static double standardAverageValue(int[] array) {
        double res = 0;

        for(int i: array)
            res += i;

        return res / array.length;
    }

    // Method for creating an array size n
    public static int[] createArray(int n) {
        int[] array = new int[n];

        for (int i = 0; i < n; i++) {
            array[i] = i + 1;
        }

        return array;
    }

    public static double mapReduceAverageValue(int[] array, int parts) throws InterruptedException, ExecutionException{
        List<int[]> chunkedArrays = new ArrayList<>(); // Array to hold chunks (Map phase preparation)

        int sizeChunk = array.length / parts; // Calculate chunk size

        System.out.println("Size of array: " + array.length);

        long startTime = System.currentTimeMillis();

        // Map phase: Splitting the array into chunks
        for(int i = 0; i < array.length; i += sizeChunk) {
            int end = Math.min(array.length, i + sizeChunk); // Calculate end index for subarray
            chunkedArrays.add(Arrays.copyOfRange(array, i, end)); // Create chunk and add to the list (Map)
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Time for chunks array: " + (endTime - startTime) + "ms");

        // Create thread pool based on number of chunks
        ExecutorService executor = Executors.newFixedThreadPool(chunkedArrays.size());
        List<Callable<Double>> tasks = new ArrayList<>();

        startTime = System.currentTimeMillis();

        // Map phase: Creating tasks to calculate averages for each chunk
        for (int[] chunk : chunkedArrays) {
            tasks.add(() -> {
                double sum = 0;
                for (int value : chunk) {
                    sum += value; // Summing values in chunk (Map)
                }
                return sum / chunk.length; // Return chunk average (Map result)
            });
        }

        // Execute all tasks (parallel map operations)
        List<Future<Double>> results = executor.invokeAll(tasks);

        endTime = System.currentTimeMillis();
        System.out.println("Time for executing threads: " + (endTime - startTime) + "ms");

        startTime = System.currentTimeMillis();

        // Reduce phase: Summing up all chunk averages
        double totalSum = 0;
        for (Future<Double> result : results) {
            totalSum += result.get(); // Adding up chunk averages (Reduce)
        }

        endTime = System.currentTimeMillis();
        System.out.println("Time for processing results: " + (endTime - startTime) + "ms");

        executor.shutdown();

        return totalSum / chunkedArrays.size(); // Final average result (Reduce)
    }


    public static void main(String[] args) throws InterruptedException, ExecutionException {
        int processors = Runtime.getRuntime().availableProcessors(); // 8
        int[] sizes = {10_000, 100_000, 1_000_000, 100_000_000};

        for(int i: sizes) {
            int[] array = createArray(i);

            long start = System.currentTimeMillis();
            double resultMapReduce = mapReduceAverageValue(array, processors);
            long end = System.currentTimeMillis();

            System.out.println("\nSize of array: " + i);

            System.out.println("MapReduce average value: " + resultMapReduce + ", elapsed time: " + (end - start) + "ms");

            start = System.currentTimeMillis();
            double resultStandard = standardAverageValue(array);
            end = System.currentTimeMillis();
            System.out.println("Standard average value: " + resultStandard + ", elapsed time: " + (end - start) + "ms");
        }

    }
}