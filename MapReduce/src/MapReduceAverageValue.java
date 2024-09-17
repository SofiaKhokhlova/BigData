import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

public class MapReduceAverageValue {

    // Метод творення масиву розмірністю n
    public static int[] createArray(int n) {
        int[] array = new int[n];

        for (int i = 0; i < n; i++) {
            array[i] = i + 1;
        }

        return array;
    }

    // Метод для розбиття массиву на частини для подальшої паралельної обробки
    public static List<int[]> map(int[] array, int numThreads){
        List<int[]> chunkedArrays = new ArrayList<>(); // список для зберігання частин масиву
        int sizeChunk = array.length / numThreads; // розмір кожної частини

        for(int i = 0; i < array.length; i += sizeChunk) {
            int end = Math.min(array.length, i + sizeChunk); // визначення кінця підмасиву
            chunkedArrays.add(Arrays.copyOfRange(array, i, end)); // додавання підмасиву до списку
        }

        return chunkedArrays; // повертаємо список підмасивів
    }

    // Метод для паралельного обчислення середнього значення кожного підмасиву та їх об'єдання
    public static double reduce(List<int[]> chunkedArrays) throws InterruptedException, ExecutionException{
        ExecutorService executorService = Executors.newFixedThreadPool(chunkedArrays.size()); // пул потоків
        List<Callable<Double>> threads = new ArrayList<>(); // список потоків для паралельного виконання

        // додавання потокам завдань для обчислення середнього значення та додавання потоків до пулу
        for(int [] array: chunkedArrays){
            threads.add(() -> Arrays.stream(array).average().orElse(0.0));
        }

        List<Future<Double>> futures = executorService.invokeAll(threads); // запуск усіх потоків та отримання результатів
        double totalSum = 0;

        for(Future<Double> future: futures) {
            totalSum += future.get(); // отримані результати складаємо
        }

        executorService.shutdown(); // закриваємо пул потоків

        return totalSum / chunkedArrays.size(); // повертаємо середне значення
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        int numThreads = Runtime.getRuntime().availableProcessors(); // кількість доступних процесорів (4)
        int[] array = createArray(10000000);

        long start = System.currentTimeMillis();
        List<int[]> chunkedArrays = map(array, 4);
        double mapReduceResult = reduce(chunkedArrays);
        long end = System.currentTimeMillis();

        System.out.println("MapReduce average value: " + mapReduceResult);
        System.out.println("Elapsed time: " + (end - start) + "ms");
        // Елементів: 10000, Потоків: 3, Час: 18мс, 18мс, 16мс
        // Елементів: 10000000, Потоків: 4, Час: 94мс, 114мс, 92мс
    }
}
