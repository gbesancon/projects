// Copyright (C) 2017 GBesancon

import org.junit.Assert;
import org.junit.Test;

public class SolutionTest {

  @Test
  public void test1() {
    int distance = Solution.bstDistance(new int[] {5, 6, 3, 1, 2, 4}, 6, 2, 4);
    Assert.assertEquals(3, distance);
  }

  @Test
  public void test2() {
    int distance = Solution.bstDistance(new int[] {9, 7, 5, 3, 1}, 5, 7, 20);
    Assert.assertEquals(-1, distance);
  }
}
