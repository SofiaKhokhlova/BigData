# Результати:
    Size of array: 10000
    Standard average value: 5000.5, elapsed time: 0ms
    MapReduce average value: 5000.5, elapsed time: 14ms
    Time for chunks array: 0ms
    Time for executing threads: 3ms
    Time for processing results: 0ms

    Size of array: 100000
    Standard average value: 50000.5, elapsed time: 1ms
    MapReduce average value: 50000.5, elapsed time: 1ms
    Time for chunks array: 0ms
    Time for executing threads: 1ms
    Time for processing results: 0ms

    Size of array: 1000000
    Standard average value: 500000.5, elapsed time: 2ms
    MapReduce average value: 500000.5, elapsed time: 4ms
    Time for chunks array: 0ms
    Time for executing threads: 4ms
    Time for processing results: 0ms
    
    Size of array: 100000000
    Standard average value: 5.00000005E7, elapsed time: 106ms
    MapReduce average value: 5.00000005E7, elapsed time: 193ms
    Time for chunks array: 152ms
    Time for executing threads: 38ms
    Time for processing results: 0ms
    

Як видно з рядків вище, метод MapReduce не має перевагу над стандартним проходженням по массиву.

І у мене є кілька здогадок, чому так відбувається:

* По-перше, рроцес розділення вихідного масиву на підмасиви тягне за собою створення нових масивів і копіювання даних, що скоріщ за все додає обчислювадьні витрати.

* По-друге, кожна частина даних обробляється як окреме завдання (Callable), яке необхідно передати в пул потоків, процес передачі завдань, їхнього планування та управління об'єктами Future створює зайві витрати, які можливо збільшують загальний час виконання.

Складність стандартной реалізації: O(n)

Cкладність MapReduce: O(n/p)
