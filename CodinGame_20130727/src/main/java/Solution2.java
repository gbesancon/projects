import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/** Solution 2. */
public class Solution2 {
  public static class Map {
    public int width = 0;
    public int height = 0;
    public MapPoint[][] map; // TRUE : Land, FALSE : Water

    public Map(int width, int height) {
      this.width = width;
      this.height = height;
      this.map = new MapPoint[height][width];
      for (int y = 0; y < height; y++) {
        for (int x = 0; x < width; x++) {
          map[y][x] = new MapPoint(x, y, Boolean.TRUE);
        }
      }
    }
  }

  public static class MapPoint {
    public int x;
    public int y;
    public boolean value;

    public MapPoint(int x, int y, boolean value) {
      this.x = x;
      this.y = y;
      this.value = value;
    }
  }

  public static class Point {
    public int x;
    public int y;

    public Point(int x, int y) {
      this.x = x;
      this.y = y;
    }
  }

  private static Map buildMap(Scanner in) {
    int width = in.nextInt();
    int height = in.nextInt();
    Map map = new Map(width, height);
    for (int y = 0; y < height; y++) {
      String line = in.next();
      for (int x = 0; x < width; x++) {
        char value = line.charAt(x);
        if ('O' == value) {
          map.map[y][x].value = false;
        }
      }
    }
    return map;
  }

  private static List<Point> buildProbedPoints(Scanner in) {
    List<Point> probedPoints = new ArrayList<Point>();
    int nbProbedPoints = in.nextInt();
    for (int i = 0; i < nbProbedPoints; i++) {
      int x = in.nextInt();
      int y = in.nextInt();
      probedPoints.add(new Point(x, y));
    }
    return probedPoints;
  }

  private static List<MapPoint> getLakeAt(Map map, Point point) {
    List<MapPoint> lake = new ArrayList<MapPoint>();
    List<MapPoint> pointsToProcess = new ArrayList<MapPoint>();
    pointsToProcess.add(map.map[point.y][point.x]);
    List<MapPoint> processedPoint = new ArrayList<MapPoint>();
    while (!pointsToProcess.isEmpty()) {
      MapPoint mapPoint = pointsToProcess.remove(0);
      discoverLakeAt(map, mapPoint, lake, pointsToProcess, processedPoint);
    }
    return lake;
  }

  private static void discoverLakeAt(
      Map map,
      MapPoint mapPoint,
      List<MapPoint> lake,
      List<MapPoint> pointsToProcess,
      List<MapPoint> processedPoint) {
    if (!processedPoint.contains(mapPoint)) {
      if (mapPoint.value == false) {
        if (!lake.contains(mapPoint)) {
          lake.add(mapPoint);

          if (mapPoint.x > 0) {
            pointsToProcess.add(map.map[mapPoint.y][mapPoint.x - 1]);
          }
          if (mapPoint.x < map.width - 1) {
            pointsToProcess.add(map.map[mapPoint.y][mapPoint.x + 1]);
          }
          if (mapPoint.y > 0) {
            pointsToProcess.add(map.map[mapPoint.y - 1][mapPoint.x]);
          }
          if (mapPoint.y < map.height - 1) {
            pointsToProcess.add(map.map[mapPoint.y + 1][mapPoint.x]);
          }
        }
      }
      processedPoint.add(mapPoint);
    }
  }

  public static void main(String args[]) {
    Scanner in = new Scanner(System.in);
    Map map = buildMap(in);
    List<Point> probedPoints = buildProbedPoints(in);
    for (Point probedPoint : probedPoints) {
      List<MapPoint> lake = getLakeAt(map, probedPoint);
      System.out.println(lake.size());
    }
    in.close();
  }
}
