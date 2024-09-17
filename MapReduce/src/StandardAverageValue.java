public class StandardAverageValue {

    // ����� �������� ������ ��������� n
    public static int[] createArray(int n) {
        int[] array = new int[n];

        for (int i = 0; i < n; i++) {
            array[i] = i + 1;
        }

        return array;
    }

    // ����� ��� ������������ ���������� ���������� �������� � �����
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
        // ��������: 10000, ���: 0��, 0��, 0��
        // ��������: 10000000, ���: 16��, 17��, 18��
    }
}