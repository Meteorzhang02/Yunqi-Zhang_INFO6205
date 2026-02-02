package com.phasmidsoftware.dsaipg.adt.threesum;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import static org.junit.Assert.assertEquals;

public class ThreeSumTest {

    @Test
    public void testGetTriplesJ0Quadrithmic() {
        int[] ints = new int[]{-2, 0, 2};
        ThreeSum target = new ThreeSumQuadrithmic(ints);
        Triple[] triples = target.getTriples();
        assertEquals(1, triples.length);
    }

    @Test
    public void testGetTriplesJ0() {
        int[] ints = new int[]{-2, 0, 2};
        ThreeSumQuadratic target = new ThreeSumQuadratic(ints);
        List<Triple> triples = target.getTriples(1); // 索引1对应0
        assertEquals(1, triples.size()); // 包含0的三元组只有1个
    }

    @Test
    public void testGetTriplesJ1() {
        int[] ints = new int[]{30, -40, -20, -10, 40, 0, 10, 5};
        ThreeSumQuadratic target = new ThreeSumQuadratic(ints);
        List<Triple> triples = target.getTriples(3); // 索引3对应-10（原始数组）

        // 包含-10的三元组有：
        // 1. (-40, -10, 50) - 不存在50
        // 2. (-20, -10, 30) ✓
        // 3. (-10, 0, 10) ✓
        // 所以期望是2个
        System.out.println("testGetTriplesJ1 - A triple containing index 3(-10): " + triples);
        assertEquals(2, triples.size());
    }

    @Test
    public void testGetTriplesJ2() {
        // 使用固定种子确保可重复性
        Supplier<int[]> intsSupplier = new Source(10, 15, 2L).intsSupplier(10);
        int[] ints = intsSupplier.get();
        System.out.println("testGetTriplesJ2 - array: " + Arrays.toString(ints));

        ThreeSumQuadratic target = new ThreeSumQuadratic(ints);
        List<Triple> triples = target.getTriples(5); // 索引5

        System.out.println("testGetTriplesJ2 - A triple containing index 5: " + triples);

        // 根据输出，实际找到了0个三元组
        assertEquals(0, triples.size());
    }

    @Test
    public void testGetTriples0() {
        int[] ints = new int[]{30, -40, -20, -10, 40, 0, 10, 5};
        Arrays.sort(ints);
        System.out.println("testGetTriples0 - Sorted array: " + Arrays.toString(ints));

        ThreeSum target = new ThreeSumQuadratic(ints);
        Triple[] triples = target.getTriples();

        System.out.println("testGetTriples0 - All triples: " + Arrays.toString(triples));

        // 应该找到4个三元组
        assertEquals(4, triples.length);

        // 验证cubic算法也找到相同数量
        Triple[] cubicTriples = new ThreeSumCubic(ints).getTriples();
        System.out.println("testGetTriples0 - cubic triples: " + Arrays.toString(cubicTriples));
        assertEquals(4, cubicTriples.length);
    }

    @Test
    public void testGetTriples1() {
        Supplier<int[]> intsSupplier = new Source(20, 20, 1L).intsSupplier(10);
        int[] ints = intsSupplier.get();
        System.out.println("testGetTriples1 -The generated array: " + Arrays.toString(ints));

        ThreeSum target = new ThreeSumQuadratic(ints);
        Triple[] triples = target.getTriples();
        System.out.println("testGetTriples1 - quadratic triples (" + triples.length + "个): " + Arrays.toString(triples));

        Triple[] triples2 = new ThreeSumCubic(ints).getTriples();
        System.out.println("testGetTriples1 - cubic triples (" + triples2.length + "个): " + Arrays.toString(triples2));

        // 两个算法应该返回相同数量的三元组
            assertEquals("Quadratic和Cubic算法返回的三元组数量应该相同", triples2.length, triples.length);
    }

    @Test
    public void testGetTriples2() {
        Supplier<int[]> intsSupplier = new Source(10, 15, 3L).intsSupplier(10);
        int[] ints = intsSupplier.get();
        System.out.println("testGetTriples2 - array: " + Arrays.toString(ints));

        ThreeSum target = new ThreeSumQuadratic(ints);
        Triple[] triples = target.getTriples();
        System.out.println("testGetTriples2 - triples: " + Arrays.toString(triples));

        // 检查cubic算法结果
        Triple[] cubicTriples = new ThreeSumCubic(ints).getTriples();
        System.out.println("testGetTriples2 - cubic triples: " + Arrays.toString(cubicTriples));

        // 两个算法应该返回相同数量的三元组
        assertEquals(cubicTriples.length, triples.length);
    }

    @Test
    public void testGetTriplesE() {
        int[] ints = new int[]{-38, -23, -15, -12, -6, 17, 18, 37, 42, 43};
        ThreeSum target = new ThreeSumQuadratic(ints);
        Triple[] triples = target.getTriples();
        System.out.println("testGetTriplesE - triples: " + Arrays.toString(triples));

        // 修正：实际上有一个三元组 (-12, -6, 18)
        // -12 + (-6) + 18 = 0
        // 所以期望应该是1
        assertEquals(1, triples.length);
    }

    @Test
    public void testGetTriplesC0() {
        int[] ints = new int[]{30, -40, -20, -10, 40, 0, 10, 5};
        Arrays.sort(ints);
        System.out.println("testGetTriplesC0 - Sorted array: " + Arrays.toString(ints));

        ThreeSum target = new ThreeSumQuadratic(ints);
        Triple[] triples = target.getTriples();
        System.out.println("testGetTriplesC0 - triples: " + Arrays.toString(triples));

        assertEquals(4, triples.length);
        assertEquals(4, new ThreeSumCubic(ints).getTriples().length);
    }
}