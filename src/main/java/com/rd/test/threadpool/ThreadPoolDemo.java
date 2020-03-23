package com.rd.test.threadpool;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Honyelchak
 * @create 2020-03-23 18:45
 */
public class ThreadPoolDemo {


    public static void main(String[] args) throws InterruptedException {
        // 初始化线程
        ThreadPoolExecutor executor = new ThreadPoolExecutor(3, 3, 0, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(3));
        Number number = new Number();
        // 对数组进行初始化
        for (int i = 0; i < Number.NUMBER_ARRAY_SIZE; i++) {
            Number.arr_get[i] = i + 1;
        }
        executor.execute(() -> {
            number.thread1();
        });
        executor.execute(() -> {
            number.thread2();
        });
        executor.execute(() -> {
            number.thread3();
        });
        TimeUnit.SECONDS.sleep(3);
        // 打印结果数组(arr_put)
        for (int i = 0; i < Number.NUMBER_ARRAY_SIZE; i++) {
            System.out.println(Number.arr_put[i]);
        }
        // 关闭线程池
        executor.shutdownNow();
    }
}

// 资源类
class Number {
    // 数组的大小
    final static int NUMBER_ARRAY_SIZE = 16;
    // 初始数组(get)1，2，3，4.....
    static int arr_get[] = new int[NUMBER_ARRAY_SIZE];
    // result数组(put)
    static int arr_put[] = new int[NUMBER_ARRAY_SIZE];
    static Random random = new Random();
    static int arr_put_count = 0;
    /**
     * 当前线程flag，用于线程轮流执行
     **/
    private int current_thread = 1;
    private Lock lock = new ReentrantLock();
    private Condition condition1 = lock.newCondition();
    private Condition condition2 = lock.newCondition();
    private Condition condition3 = lock.newCondition();

    /**
     * 从数组中删除一个数，其余数向左对齐
     *
     * @param arr          目标数组
     * @param deletedIndex 被删除数的下标
     */
    public static void fromArrayDeleteOne(int[] arr, int deletedIndex) {
        for (int i = deletedIndex; i < arr.length - 1; i++) {
            arr[i] = arr[i + 1];
        }
    }

    /**
     * 从arr_get数组随机获取一个数，并删除
     */
    public static void getNumberFromArrayByRandom() {
        // 从0~数组现有长度中获取随机数
        int random_index = random.nextInt(NUMBER_ARRAY_SIZE - arr_put_count);
        // 将取出的数放到结果数组中(arr_put)
        arr_put[arr_put_count++] = arr_get[random_index];
        // 从数组中删除那个随机取出的数
        Number.fromArrayDeleteOne(arr_get, random_index);
    }


    public void thread1() {
        while (arr_put_count < NUMBER_ARRAY_SIZE) {
            lock.lock();
            try {
                while (current_thread != 1) {
                    condition1.await();
                }
                if (NUMBER_ARRAY_SIZE <= arr_put_count) continue;
                getNumberFromArrayByRandom();
                current_thread = 2;
                condition2.signal();
            } catch (InterruptedException e) {
                System.out.println("抛出异常");
            } finally {
                lock.unlock();
            }
        }
    }

    public void thread2() {
        while (arr_put_count < NUMBER_ARRAY_SIZE) {
            lock.lock();
            try {
                while (current_thread != 2) {
                    condition2.await();
                }
                if (NUMBER_ARRAY_SIZE <= arr_put_count) continue;
                getNumberFromArrayByRandom();
                current_thread = 3;
                condition3.signal();
            } catch (InterruptedException e) {
                System.out.println("抛出异常");
            } finally {
                lock.unlock();
            }
        }
    }


    public void thread3() {
        while (arr_put_count < NUMBER_ARRAY_SIZE) {
            lock.lock();
            try {
                while (current_thread != 3) {
                    condition3.await();
                }
                if (NUMBER_ARRAY_SIZE <= arr_put_count) continue;
                getNumberFromArrayByRandom();
                current_thread = 1;
                condition1.signal();
            } catch (InterruptedException e) {
                System.out.println("抛出异常");
            } finally {
                lock.unlock();
            }
        }
    }
}
