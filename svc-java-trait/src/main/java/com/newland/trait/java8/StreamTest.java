package com.newland.trait.java8;

import java.util.*;
import java.util.stream.Collectors;

/**
 * +--------------------+       +------+   +------+   +---+   +-------+
 * | stream of elements +-----> |filter+-> |sorted+-> |map+-> |collect|
 * +--------------------+       +------+   +------+   +---+   +-------+
 * 聚合操作 类似SQL语句一样的操作， 比如filter, map, reduce, find, match, sorted等。
 */
public class StreamTest {

    /**
     * stream() − 为集合创建串行流。
     * parallelStream() − 为集合创建并行流。
     */
    public void stream() {
        List<String> strings = Arrays.asList("abc", "", "bc", "efg", "abcd", "", "jkl");
        List<String> filtered = strings.stream().filter(string -> !string.isEmpty()).collect(Collectors.toList());
    }

    //Stream 提供了新的方法 'forEach' 来迭代流中的每个数据
    public void forEach() {
        Random random = new Random();
        random.ints().limit(10).forEach(System.out::println);
        List<String> strings = Arrays.asList("abc", "", "bc", "efg", "abcd", "", "jkl");
        strings.forEach(System.out::println);
        strings.forEach(item -> System.out.println(item));
    }

    //map 方法用于映射每个元素到对应的结果
    public void map() {
        List<Integer> numbers = Arrays.asList(3, 2, 2, 3, 7, 3, 5);
        // 获取对应的平方数
        List<Integer> squaresList = numbers.stream().map(i -> i * i).distinct().collect(Collectors.toList());
    }

    //方法用于通过设置的条件过滤出元素
    public void filter() {
        List<String> strings = Arrays.asList("abc", "", "bc", "efg", "abcd", "", "jkl");
        // 获取空字符串的数量
        long count = strings.stream().filter(String::isEmpty).count();
    }

    //limit 方法用于获取指定数量的流。
    public void limit() {
//        Random random = new Random();
//        random.ints().limit(10).forEach(System.out::println);

        List<String> strings = Arrays.asList("abc", "", "bc", "efg", "abcd", "", "jkl");
        strings.stream().limit(3).forEach(System.out::println);
    }

    public void sorted() {
        List<String> strings = Arrays.asList("abc", "", "bc", "efg", "abcd", "", "jkl");
        strings.stream().sorted(String::compareTo).forEach(System.out::println);
    }

    //parallelStream 是流并行处理程序的代替方法。
    public void parallel() {
        List<String> strings = Arrays.asList("abc", "", "bc", "efg", "abcd", "", "jkl");
        // 获取空字符串的数量
        long count = strings.parallelStream().filter(string -> string.isEmpty()).count();
        System.out.println(count);
    }

    //Collectors 类实现了很多归约操作，例如将流转换成集合和聚合元素。
    public void Collectors() {
        List<String> strings = Arrays.asList("abc", "", "bc", "efg", "abcd", "", "jkl");
        List<String> filtered = strings.stream().filter(string -> !string.isEmpty()).collect(Collectors.toList());

        System.out.println("筛选列表: " + filtered);
        String mergedString = strings.stream().filter(string -> !string.isEmpty()).collect(Collectors.joining(", "));
        System.out.println("合并字符串: " + mergedString);
    }

    public void changeDAta() {
        int[] array = new int[]{1, 2, 3, 4, 5, 6};
        //int[] => List[]
        List<Integer> integerList = Arrays.stream(array).mapToObj(Integer::valueOf).collect(Collectors.toList());
        //int[] => Integer[]
        Integer[] integers = Arrays.stream(array).mapToObj(Integer::valueOf).toArray(Integer[]::new);

        //List[]=>Integer[]
        integers = integerList.stream().toArray(Integer[]::new);
        //List[]=>int[]
        int[] newArray=integerList.stream().mapToInt(Integer::intValue).toArray();
    }
    //将多个集合，放入一个集合
    public void flagmap(){
        List<String> strings = Arrays.asList("abc", "", "bc", "efg", "abcd", "", "jkl");
        List<String> strings2 = Arrays.asList("abc", "", "bc", "efg", "abcd", "", "jkl");
        List<List<String>> list=new ArrayList<>();
        list.add(strings);
        list.add(strings2);
        List<String> result=list.stream().flatMap(item->item.stream().filter(String::isEmpty)).collect(Collectors.toList());
    }

    public static void main(String[] args) {
        new StreamTest().Collectors();
    }
}
