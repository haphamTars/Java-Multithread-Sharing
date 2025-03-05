import java.util.concurrent.*;

public class FallDown {
    public static void main(String[] args) throws InterruptedException {
        ConcurrentHashMap<Integer, Integer> map = new ConcurrentHashMap<>();
        map.put(1, 0); // Ban đầu giá trị là 0

        Runnable task = () -> {
            for (int i = 0; i < 1000; i++) {
                // Đọc giá trị hiện tại rồi cập nhật lại (KHÔNG nguyên tử)
                map.put(1, map.get(1) + 1);
//                Dùng compute method, khóa key lại để tránh update sai
//                map.compute(1, (key, value) -> value + 1);
            }
        };

        // Tạo 2 luồng đồng thời cập nhật map
        Thread t1 = new Thread(task);
        Thread t2 = new Thread(task);
        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println("Kết quả mong đợi: 2000");
        System.out.println("Kết quả thực tế: " + map.get(1)); // Có thể nhỏ hơn 2000 do mất dữ liệu
    }
}
