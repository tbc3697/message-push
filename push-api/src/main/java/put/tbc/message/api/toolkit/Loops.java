package put.tbc.message.api.toolkit;


import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * 基于函数回调提供更快捷方便的循环功能
 *
 * @author tbc
 * @version 1.0 {2016年8月2日 下午6:04:14}
 */
public final class Loops {

    private Loops() {
        throw new AssertionError("No " + getClass().getName() + " instances for you!");
    }

    public static void times(int end, Consumer<Integer> consumer) {
        upto(0, end, consumer);
    }

    public static void upto(int start, int end, Consumer<Integer> consumer) {
        step(start, end, 1, consumer);
    }

    /**
     * 从start（含）到end（不含）遍历，每次加step
     *
     * @param start    起始值
     * @param end      结束值
     * @param step     步长
     * @param consumer 消费函数
     */
    public static void step(int start, int end, int step, Consumer<Integer> consumer) {
        if (start > end) {
            end = start + end;
            start = end - start;
            end = end - start;
        }

        for (int i = start; i < end; i += step) {
            consumer.accept(i);
        }
    }

    public static <T> void forEach(T[] ts, Consumer<T> consumer) {
        if (Objs.nonEmpty(ts)) {
            for (T t : ts) {
                consumer.accept(t);
            }
        }
    }

    public static <T> void forEach(Collection<T> collection, Consumer<T> consumer) {
        if (Objs.nonEmpty(collection)) {
            for (T t : collection) {
                consumer.accept(t);
            }
        }
    }

    public static <K, V> void forEach(Map<K, V> map, BiConsumer<K, V> biConsumer) {
        if (Objs.nonEmpty(map)) {
            Set<K> keys = map.keySet();
            for (K k : keys) {
                biConsumer.accept(k, map.get(k));
            }
        }
    }

    public static <T> T filter(Iterable<T> iterable) {
        // TODO... 参考, FluentIterable filter
        return null;
    }

    public static <T, R> R map(Iterator<T> iterator) {
        // TODO... FluentIterable transform
        return null;
    }

    public static <T> T folds(Iterator<T> iterator, BiConsumer<T, T> biConsumer) {
        // TODO...
        T pre = null;
        while (iterator.hasNext()) {
            if (Objs.isEmpty(pre)) {
                pre = iterator.next();
                continue;
            }
            T t = iterator.next();
            biConsumer.accept(pre, t);
            pre = t;
        }
        return null;
    }

//    public static <T extends Number> T sum(Iterator<T> numbers) {
//        T sum = null;
//
//        return null;
//    }

    public static double sumDouble(Iterator<Double> numbers) {
        double sum = 0.0D;
        while (numbers.hasNext()) {
            sum += numbers.next();
        }
        return sum;
    }

    public static float sumFloat(Iterator<Float> numbers) {
        float sum = 0.0f;
        while (numbers.hasNext()) {
            sum += numbers.next();
        }
        return sum;
    }


    public static long sumLong(Iterator<Long> numbers) {
        long sum = 0L;
        while (numbers.hasNext()) {
            sum += numbers.next();
        }
        return sum;
    }

    public static int sumInt(Iterator<Double> numbers) {
        int sum = 0;
        while (numbers.hasNext()) {
            sum += numbers.next();
        }
        return sum;
    }

    public static void main(String... args) {
        System.out.println(Loops.class.getName());
        Loops.times(9, new Consumer<Integer>() {
            public void accept(Integer t) {
                System.out.println(t);
            }
        });

        Loops.step(10, 100, 10, new Consumer<Integer>() {
            public void accept(Integer t) {
                System.out.println(t);
            }
        });

    }

}
