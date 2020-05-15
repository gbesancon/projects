// Copyright (C) 2017 GBesancon

package org.benhur.utility.introspect;

import java.lang.reflect.Field;
import java.net.URL;

public class IntrospectUtility {
  @SuppressWarnings("unchecked")
  public static <U> U getAttribute(Object object, String name) {
    U attribute = null;
    try {
      Field field = object.getClass().getDeclaredField(name);
      field.setAccessible(true);
      attribute = (U) field.get(object);
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
    } catch (SecurityException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (NoSuchFieldException e) {
      e.printStackTrace();
    }
    return attribute;
  }

  public static <T> URL logJar(Class<T> aClass) {
    return aClass.getResource('/' + aClass.getName().replace('.', '/') + ".class");
  }
}
