import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.ThreadLocalRandom;

public class CatDogDemo {
    private static final ReentrantLock lock = new ReentrantLock();

    static class Dog {
        public void growl() {
            System.out.println("Dog: Woof Woof!");
        }
    }

    static class Cat {
        public void meow() {
            System.out.println("Cat: Meow Meow!");
        }
    }

    private static final Dog dog = new Dog();
    private static final Cat cat = new Cat();

    public static void main(String[] args) {
        Thread catThread = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                if (lock.tryLock()) {
                    try {
                        cat.meow();
                        Thread.sleep(500); // Giữ lock lâu hơn
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } finally {
                        lock.unlock();
                    }
                } else {
                    System.out.println("Lock Cat");
                }
            }
        });

        Thread dogThread = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                if (lock.tryLock()) {
                    try {
                        dog.growl();
                        Thread.sleep(500); // Giữ lock lâu hơn
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } finally {
                        lock.unlock();
                    }
                } else {
                    System.out.println("Lock Dog");
                }
            }
        });

        // Ngẫu nhiên chọn luồng nào chạy trước để đảm bảo 50/50
        if (ThreadLocalRandom.current().nextBoolean()) {
            catThread.start();
            dogThread.start();
        } else {
            dogThread.start();
            catThread.start();
        }
    }
}