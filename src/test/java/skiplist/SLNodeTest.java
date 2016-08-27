package skiplist;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * 8/26/16 9:42 PM
 */
public class SLNodeTest {
    @Test public void basicAPIChecks() throws Exception {
        List<String> skipList = new SkipList<>(new String[]{"something", "something"});
        List<String> comparison = new ArrayList<>(Arrays.asList("something", "something"));
        assertEquals(comparison.size(), skipList.size());
        assertEquals(comparison.contains("something"), skipList.contains("something"));
        assertEquals(comparison.add("abcd"), skipList.add("abcd"));
        assertEquals(comparison.contains("abcd"), skipList.contains("abcd"));
    }

    @Test public void shouldBeFasterThanArrayListForContains() throws Exception {
        List<Integer> skipList = new SkipList<>();
        List<Integer> comparison = new ArrayList<>();
        Random r = new Random();
        int testSize = 1000;
        for (int i = 0; i < testSize; i++) {
            Integer nextInt = r.nextInt(10000);
            assertEquals(comparison.add(nextInt), skipList.add(nextInt));
        }
        Integer[] onesWeDidntAdd = new Integer[testSize];
        for (int i = 0; i < testSize; i++) {
            onesWeDidntAdd[i] = r.nextInt(10000);
        }
        boolean[] expectedResponses = new boolean[testSize];
        long expectedStart = System.nanoTime();
        for (Integer query : comparison) assertTrue(comparison.contains(query));
        for (int i = 0; i < onesWeDidntAdd.length; i++) {
            expectedResponses[i] = comparison.contains(onesWeDidntAdd[i]);
        }
        long expectedEnd = System.nanoTime();
        long realStart = System.nanoTime();
        for (Integer query : comparison) assertTrue(skipList.contains(query));
        for (int i = 0; i < onesWeDidntAdd.length; i++) {
            assertEquals(expectedResponses[i], skipList.contains(onesWeDidntAdd[i]));
        }
        long realEnd = System.nanoTime();
        System.out.printf("array list: %d\n skip list: %d\n",
            expectedEnd - expectedStart,
            realEnd - realStart
        );
    }
}
