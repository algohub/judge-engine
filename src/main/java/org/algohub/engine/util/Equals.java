package org.algohub.engine.util;

import java.util.Arrays;

public interface Equals {
  /**
   * More powerful than java.util.Objects.equals().
   */
  static boolean equal(Object a, Object b) {
    if (a == b) {
      return true;
    } else if (a == null || b == null) {
      return false;
    }

    boolean eq;
    if (a instanceof Object[] && b instanceof Object[]) {
      eq = Arrays.deepEquals((Object[]) a, (Object[]) b);
    } else if (a instanceof byte[] && b instanceof byte[]) {
      eq = Arrays.equals((byte[]) a, (byte[]) b);
    } else if (a instanceof short[] && b instanceof short[]) {
      eq = Arrays.equals((short[]) a, (short[]) b);
    } else if (a instanceof int[] && b instanceof int[]) {
      eq = Arrays.equals((int[]) a, (int[]) b);
    } else if (a instanceof long[] && b instanceof long[]) {
      eq = Arrays.equals((long[]) a, (long[]) b);
    } else if (a instanceof char[] && b instanceof char[]) {
      eq = Arrays.equals((char[]) a, (char[]) b);
    } else if (a instanceof float[] && b instanceof float[]) {
      eq = Arrays.equals((float[]) a, (float[]) b);
    } else if (a instanceof double[] && b instanceof double[]) {
      eq = Arrays.equals((double[]) a, (double[]) b);
    } else if (a instanceof boolean[] && b instanceof boolean[]) {
      eq = Arrays.equals((boolean[]) a, (boolean[]) b);
    } else {
      eq = a.equals(b);
    }
    return eq;
  }
}
