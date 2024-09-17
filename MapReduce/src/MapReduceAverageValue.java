import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

public class MapReduceAverageValue {

    // Method for creating an array size n
    public static int[] createArray(int n) {
        int[] array = new int[n];

        for (int i = 0; i < n; i++) {
            array[i] = i + 1;
        }

        return array;
    }

    // Method for splitting an array into parts for further parallel processing
    public static List<int[]> map(int[] array, int numThreads){
        List<int[]> chunkedArrays = new ArrayList<>(); // list for storing parts of the array
        int sizeChunk = array.length / numThreads; // the size of each part

        for(int i = 0; i < array.length; i += sizeChunk) {
            int end = Math.min(array.length, i + sizeChunk); // determining the end of the subarray
            chunkedArrays.add(Arrays.copyOfRange(array, i, end)); // adding a subarray to the list
        }

        return chunkedArrays; // return the list of subarrays
    }

    // A method for parallel calculation of the average value of each subarray and their combining
    public static double reduce(List<int[]> chunkedArrays) throws InterruptedException, ExecutionException{
        ExecutorService executorService = Executors.newFixedThreadPool(chunkedArrays.size()); // thread pool
        List<Callable<Double>> threads = new ArrayList<>(); // list of threads for parallel execution

        // adding tasks to threads to calculate the average value and adding threads to the pool
        for(int [] array: chunkedArrays){
            threads.add(() -> Arrays.stream(array).average().orElse(0.0));
        }

        List<Future<Double>> futures = executorService.invokeAll(threads); // start all threads and get results
        double totalSum = 0;

        for(Future<Double> future: futures) {
            totalSum += future.get(); // add up the results
        }

        executorService.shutdown(); // close the thread pool

        return totalSum / chunkedArrays.size(); // return the average value
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        int numThreads = Runtime.getRuntime().availableProcessors(); // number of available processors (4)
        int[] array = createArray(10000000);

        long start = System.currentTimeMillis();
        List<int[]> chunkedArrays = map(array, 4);
        double mapReduceResult = reduce(chunkedArrays);
        long end = System.currentTimeMillis();

        System.out.println("Average value of MapReduce: " + mapReduceResult);
        System.out.println("Elapsed time: " + (end - start) + "ms");
        // Elements: 10000, Threads: 3, Time: 18ms, 18ms, 16ms
        // Elements: 10000000, Threads: 4, Time: 94ms, 114ms, 92ms
    }
}
