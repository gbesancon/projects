// Copyright (C) 2017 GBesancon

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

// Read inputs from System.in, Write outputs to System.out.
// Your class name has to be Solution

/** Solution 2. */
public class Solution2 {
  public static class Rooms {
    public int nbRooms;
    public List<Room> rooms = new ArrayList<Room>();
    private Map<Integer, Room> roomsByNumber = new HashMap<Integer, Room>();

    public Room getRoom(int number) {
      Room room = roomsByNumber.get(number);
      if (room == null) {
        room = new Room();
        room.number = number;
        rooms.add(room);
        roomsByNumber.put(number, room);
      }
      return room;
    }
  }

  public static class Room {
    public int number;
    public int money;
    public Room accessibleRoom1;
    public Room accessibleRoom2;
  }

  public static class Path {
    public int money;
    public List<Room> rooms = new ArrayList<Room>();
  }

  public static void main(String args[]) {
    Rooms rooms = ParseInput();
    Path bestPath = FindBestPath(rooms);
    DisplayOutput(bestPath);
  }

  public static Rooms ParseInput() {
    Scanner in = new Scanner(System.in);
    Rooms graphe = new Rooms();

    graphe.nbRooms = in.nextInt();
    in.nextLine();
    for (int iRoom = 0; iRoom < graphe.nbRooms; iRoom++) {
      Room room = graphe.getRoom(in.nextInt());
      room.money = in.nextInt();
      try {
        String accessibleRoom1 = in.next();
        room.accessibleRoom1 = graphe.getRoom(Integer.parseInt(accessibleRoom1));
      } catch (NumberFormatException e) {
        room.accessibleRoom1 = null;
      }
      try {
        String accessibleRoom2 = in.next();
        room.accessibleRoom2 = graphe.getRoom(Integer.parseInt(accessibleRoom2));
      } catch (NumberFormatException e) {
        room.accessibleRoom2 = null;
      }
    }
    in.close();
    return graphe;
  }

  public static Path FindBestPath(Rooms rooms) {
    Path bestPath = null;

    return bestPath;
  }

  public static void DisplayOutput(Path path) {
    System.out.println(path.money);
  }
}
