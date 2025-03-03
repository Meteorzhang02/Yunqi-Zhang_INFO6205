package com.phasmidsoftware.dsaipg.huskySort.sort.radix;

import com.phasmidsoftware.dsaipg.huskySort.sort.ComparableSortHelper;
import com.phasmidsoftware.dsaipg.huskySort.sort.ComparisonSortHelper;
import com.phasmidsoftware.dsaipg.huskySort.sort.huskySort.HuskySortBenchmark;
import com.phasmidsoftware.dsaipg.huskySort.sort.huskySort.HuskySortBenchmarkHelper;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class MSDStringSortTest {

    @Test
    public void sort() {
        final String[] input = "she sells seashells by the seashore the shells she sells are surely seashells".split(" ");
        final String[] expected = "are by seashells seashells seashore sells sells she she shells surely the the".split(" ");

        final MSDStringSort msdStringSort = new MSDStringSort(Alphabet.ASCII);
        msdStringSort.sort(input);
        assertArrayEquals(expected, input);
    }

    @Test
    public void sortEmptyArray() {
        final String[] input = {};
        final String[] expected = {};

        final MSDStringSort msdStringSort = new MSDStringSort(Alphabet.ASCII);
        msdStringSort.sort(input);
        assertArrayEquals(expected, input);
    }

    @Test
    public void sortSingleElementArray() {
        final String[] input = {"single"};
        final String[] expected = {"single"};

        final MSDStringSort msdStringSort = new MSDStringSort(Alphabet.ASCII);
        msdStringSort.sort(input);
        assertArrayEquals(expected, input);
    }

    @Test
    public void sortSameElementArray() {
        final String[] input = {"same", "same", "same"};
        final String[] expected = {"same", "same", "same"};

        final MSDStringSort msdStringSort = new MSDStringSort(Alphabet.ASCII);
        msdStringSort.sort(input);
        assertArrayEquals(expected, input);
    }

    @Test
    public void sortCaseSensitiveStrings() {
        final String[] input = {"b", "B", "a", "A"};
        final String[] expected = {"A", "B", "a", "b"};

        final MSDStringSort msdStringSort = new MSDStringSort(Alphabet.ASCII);
        msdStringSort.sort(input);
        assertArrayEquals(expected, input);
    }

    @Test
    public void sortNumericStrings() {
        final String[] input = {"10", "1", "2"};
        final String[] expected = {"1", "10", "2"};

        final MSDStringSort msdStringSort = new MSDStringSort(Alphabet.ASCII);
        msdStringSort.sort(input);
        assertArrayEquals(expected, input);
    }

    //    @Test
    public void sort1() {
        final ComparisonSortHelper<String> helper = new ComparableSortHelper<>("test", 1000, 1L);
        final String[] words = HuskySortBenchmarkHelper.getWords("3000-common-words.txt", HuskySortBenchmark::lineAsList);
        final String[] xs = helper.random(String.class, r -> words[r.nextInt(words.length)]);
        assertEquals(1000, xs.length);
        final MSDStringSort msdStringSort = new MSDStringSort(Alphabet.ASCII);
        msdStringSort.sort(xs);
        assertEquals("African-American", xs[0]);
        assertEquals("Palestinian", xs[16]);
    }

    //    @Test
    public void sort2() {
        final ComparisonSortHelper<String> helper = new ComparableSortHelper<>("test", 1000, 1L);
        final String[] words = HuskySortBenchmarkHelper.getWords("3000-common-words.txt", HuskySortBenchmark::lineAsList);
        final String[] xs = helper.random(String.class, r -> words[r.nextInt(words.length)]);
        assertEquals(1000, xs.length);
        final MSDStringSort msdStringSort = new MSDStringSort(new Alphabet(Alphabet.RADIX_UNICODE));
        msdStringSort.sort(xs);
        assertEquals("African-American", xs[0]);
        assertEquals("Palestinian", xs[16]);
    }

    //    @Test
    public void sortWithExtendedAscii() {
        final String[] input = ("Le renard brun rapide saute par-dessus le chien paresseux chacó chacra cháchara cántara cantar caña cana canal canapé cañón día desayuno ").split(" ");
        final MSDStringSort msdStringSort = new MSDStringSort(new Alphabet(Alphabet.RADIX_UNICODE));
        msdStringSort.sort(input);
        final Alphabet alphabet = msdStringSort.getAlphabet();
        assertTrue(new ComparableSortHelper<String>("sortWithUnicode").sorted(input));
    }

    //    @Test
    public void sortWithUnicode() {
        // CONSIDER compiling regex
        final String[] input = "python.txt\t狗.txt\t\t羊.txt\t\t鸡.txt\t\t兔子.txt\t河马.txt\t猴子.txt\t豹子.txt\t眼镜蛇.txt\n熊.txt\t\t猪.txt\t\t蛇.txt\t\t鹅.txt\t\t大象.txt\t熊猫.txt\t老虎.txt\t骆驼.txt\n牛.txt\t\t猫.txt\t\t马.txt\t\t龙.txt\t\t斑马.txt\t狮子.txt\t老鼠.txt\t鳄鱼.txt".split("\\s+");
        System.out.println(Arrays.toString(input));
        MSDStringSort.setCutoff(1);
        final MSDStringSort msdStringSort = new MSDStringSort(new Alphabet(Alphabet.RADIX_UNICODE));
        msdStringSort.sort(input);
        final Alphabet alphabet = msdStringSort.getAlphabet();
        System.out.println(alphabet);
        System.out.println(Arrays.toString(input));
        final boolean sorted = new ComparableSortHelper<String>("sortWithUnicode").sorted(input);
        assertTrue(sorted);
    }
}