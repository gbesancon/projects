// Copyright (C) 2017 GBesancon

package org.benhur.utility.thread;

public class ThreadUtility {
  public static void sleep(long delay) {
    try {
      Thread.sleep(delay);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
