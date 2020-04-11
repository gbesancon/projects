// Copyright (C) 2017 GBesancon

package org.benhur.utility.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileUtility {
  public static String getUserHomeFilepath() {
    return System.getProperty("user.home");
  }

  public static URL getURL(File file) {
    URL result = null;
    try {
      result = file.toURI().toURL();
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }
    return result;
  }

  public static File getFile(URL url) {
    File result = null;
    if (url != null) {
      try {
        result = new File(url.toURI());
      } catch (URISyntaxException e) {
        e.printStackTrace();
      }
    }
    return result;
  }

  public static String getFolder(String filename) {
    String result = null;
    int lastSlash = filename.lastIndexOf(File.separator);
    if (lastSlash != -1) {
      result = filename.substring(0, lastSlash + 1);
    } else {
      result = "";
    }
    return result;
  }

  public static String getBaseFilename(String filename) {
    String result = null;
    int lastDot = filename.lastIndexOf(".");
    int lastSlash = filename.lastIndexOf(File.separator);
    if (lastDot != -1) {
      if (lastSlash != -1) {
        if (lastDot > lastSlash) {
          result = filename.substring(lastSlash + 1, lastDot);
        } else {
          result = filename.substring(lastSlash + 1);
        }
      } else {
        result = filename.substring(0, lastDot);
      }
    } else {
      if (lastSlash != -1) {
        result = filename.substring(lastSlash + 1);
      } else {
        result = filename;
      }
    }
    return result;
  }

  public static String getExtension(String filename) {
    String result = null;
    int lastDot = filename.lastIndexOf(".");
    if (lastDot != -1) {
      result = filename.substring(lastDot);
    } else {
      result = "";
    }
    return result;
  }

  public static void removeBlanckLines(File file) {
    if (file.exists()) {
      if (file.isFile()) {
        removeBlanckLinesForFile(file);
      } else if (file.isDirectory()) {
        removeBlanckLinesForDirectory(file);
      }
    }
  }

  public static void removeBlanckLinesForDirectory(File file) {
    if (file.exists()) {
      for (File inFile : file.listFiles()) {
        removeBlanckLines(inFile);
      }
    }
  }

  public static void removeBlanckLinesForFile(File file) {
    if (file.exists()) {
      File tmpFile = new File(file.getPath() + ".tmp");
      file.renameTo(tmpFile);
      BufferedReader br = null;
      BufferedWriter bw = null;
      try {
        br = new BufferedReader(new FileReader(tmpFile));
        bw = new BufferedWriter(new FileWriter(file));
        String line = null;
        while ((line = br.readLine()) != null) {
          Pattern pattern = Pattern.compile("\\s*");
          Matcher matcher = pattern.matcher(line);
          if (!matcher.matches()) {
            bw.append(line);
            bw.newLine();
          }
        }
        bw.flush();
        br.close();
        deleteFile(tmpFile);
        bw.close();
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public static void cleanFolder(File folder, boolean keepHidden) {
    File[] files = folder.listFiles();
    if (files != null) {
      for (File file : files) {
        if (!file.isHidden() || (!keepHidden && file.isHidden())) {
          if (file.isFile()) {
            file.delete();
          } else if (file.isDirectory()) {
            cleanFolder(file, keepHidden);
          }
        }
      }
    }
  }

  public static void closeFile(Closeable closeable) {
    if (closeable != null) {
      try {
        closeable.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public static void deleteFile(File file) {
    if (file.exists()) {
      while (file.exists()) {
        file.delete();
      }
    }
  }

  public static File createFile(String filepath) {
    File result = null;
    if (filepath != null) {
      result = new File(filepath);
      try {
        result.createNewFile();
      } catch (IOException e) {
        e.printStackTrace();
        result = null;
      }
    }
    return result;
  }

  public static boolean isEmptyFolder(final File folder) {
    boolean result = false;
    if (folder == null || !folder.isDirectory() || folder.list().length == 0) {
      result = true;
    }
    return result;
  }

  @SuppressWarnings("resource")
  public static boolean copyFile(File source, File destination) {
    FileChannel in = null;
    FileChannel out = null;
    long size = 0;

    try {
      in = new FileInputStream(source).getChannel();
      out = new FileOutputStream(destination).getChannel();

      size = in.transferTo(0, in.size(), out);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (in != null) {
        closeFile(in);
      }
      if (out != null) {
        closeFile(out);
      }
    }

    return (size != 0);
  }

  public static String changeExtension(String filename, String newExtension) {
    String result = null;
    int lastDot = filename.lastIndexOf(".");
    if (lastDot != -1) {
      result = filename.substring(0, lastDot) + newExtension;
    } else {
      result = filename + newExtension;
    }
    return result;
  }
}
