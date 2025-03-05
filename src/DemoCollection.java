import java.util.*;
import java.util.concurrent.*;

public class DemoCollection {
    // Collection không an toàn khi truy cập đa luồng
    private List<Integer> unsafeList = new ArrayList<>();

    // Collection an toàn khi truy cập đa luồng
    private List<Integer> safeList = new CopyOnWriteArrayList<>();
    private Map<Integer, String> concurrentMap = new ConcurrentHashMap<>();

    // Thêm dữ liệu vào danh sách không an toàn (ArrayList)
    public void addToUnsafeList(int value) {
        unsafeList.add(value);
    }

    // Thêm dữ liệu vào danh sách an toàn (CopyOnWriteArrayList)
    public void addToSafeList(int value) {
        safeList.add(value);
    }

    // Thêm dữ liệu vào ConcurrentHashMap
    public void putToConcurrentMap(int key, String value) {
        concurrentMap.put(key, value);
    }

    // Hiển thị dữ liệu
    public void printData() {
        System.out.println("Unsafe List (ArrayList): " + unsafeList);
        System.out.println("Safe List (CopyOnWriteArrayList): " + safeList);
        System.out.println("Concurrent Map: " + concurrentMap);
    }

    public static void main(String[] args) throws InterruptedException {
        DemoCollection demo = new DemoCollection();

        // Tạo 2 luồng ghi dữ liệu vào ArrayList (không an toàn)
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                demo.addToUnsafeList(i);
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                demo.addToUnsafeList(i);
            }
        });

        // Tạo 2 luồng ghi dữ liệu vào CopyOnWriteArrayList (an toàn)
        Thread t3 = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                demo.addToSafeList(i);
            }
        });

        Thread t4 = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                demo.addToSafeList(i);
            }
        });

        // Tạo 2 luồng ghi dữ liệu vào ConcurrentHashMap
        Thread t5 = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                demo.putToConcurrentMap(i, "Value " + i);
            }
        });

        Thread t6 = new Thread(() -> {
            for (int i = 100; i < 200; i++) {
                demo.putToConcurrentMap(i, "Value " + i);
            }
        });

        // Chạy tất cả các luồng
        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();
        t6.start();

        // Đợi tất cả luồng kết thúc
        t1.join();
        t2.join();
        t3.join();
        t4.join();
        t5.join();
        t6.join();

        // Hiển thị kết quả
        demo.printData();
    }
}