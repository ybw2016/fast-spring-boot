package com.fast.springboot.basic.algorithm;

import com.google.common.base.Charsets;
import com.google.common.collect.Maps;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author bw
 * @since 2020-11-16
 */
public class BloomFilterTest {
    private static int size = 1000000;

    // 默认误判率0.03            数组大小：7298440
    // 调整误判率 -> 0.01        数组大小：9585058
    // 调整误判率 -> 0.000001    数组大小：28755175
    // ----------------- 结论：误判率越小，占用空间越大; -----------------
    private static BloomFilter<Integer> bloomFilter = BloomFilter.create(Funnels.integerFunnel(), size, 0.000001);

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < size; i++) {
            bloomFilter.put(i);
        }

        List<Integer> list = new ArrayList<Integer>(1000);
        //故意取10000个不在过滤器里的值，看看有多少个会被认为在过滤器里
        for (int i = size + 10000; i < size + 20000; i++) {
            if (bloomFilter.mightContain(i)) {
                list.add(i);
            }
        }
        System.out.println("误判的数量：" + list.size());

        //TimeUnit.HOURS.sleep(1);
    }

    private static Map<String, String> redis = Maps.newHashMap();
    private static Map<String, String> db = Maps.newHashMap();
    private static BloomFilter<String> bloomFilter1 = BloomFilter.create(Funnels.stringFunnel(Charsets.UTF_8), size, 0.03);

    // 布隆过滤器解决：缓存穿透
    private static String getValue(String key) {
        String value = redis.get(key);
        if (value == null) {
            if (!bloomFilter1.mightContain(key)) {
                return null;
            } else {
                value = db.get(key);
                redis.put(key, value);
            }
        }
        return value;
    }
}
