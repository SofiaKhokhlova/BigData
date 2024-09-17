public class StandardAverageValue {

    // Метод творення масиву розмірністю n
    public static int[] createArray(int n) {
        int[] array = new int[n];

        for (int i = 0; i < n; i++) {
            array[i] = i + 1;
        }

        return array;
    }

    // Метод для стандартного обчислення середнього значення в масиві
    public static double standardAverageValue(int[] array){
        double result = 0;

        for(int i: array){
            result += i;
        }

        return result / array.length;
    }

    public static void main(String[] args) {
        int[] array = createArray(10000);

        long start = System.currentTimeMillis();
        double standardResult = standardAverageValue(array);
        long end = System.currentTimeMillis();

        System.out.println("Standard average value: " + standardResult);
        System.out.println("Elapsed time: " + (end - start) + "ms");
        // Елементів: 10000, Час: 0мс, 0мс, 0мс
        // Елементів: 10000000, Час: 16мс, 17мс, 18мс
    }
}