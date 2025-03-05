import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Main {
    public static class SharedResourceLock {
        private int count = 0;
        private final Lock lock = new ReentrantLock();

//        Dùng synchronized
//        public synchronized void increment() {
//            count++;
//        }
//
//        public synchronized int getCount() {
//            return count;
//        }


        public void increment() {
            lock.lock(); // Khóa trước khi truy cập tài nguyên
//            Nếu dùng lock, có thể check lock và thực hiện dựa trên các phương thức của lớp ReentrantLock
            try {
                count++;
            } finally {
                lock.unlock(); // Giải phóng khóa
            }
        }

        public int getCount() {
            return count;
        }
    }
    public static void main(String[] args) throws InterruptedException {
        SharedResourceLock resource = new SharedResourceLock();

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                resource.increment();
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                resource.increment();
            }
        });

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println("Final count: " + resource.getCount()); // Kết quả luôn là 2000
    }
}