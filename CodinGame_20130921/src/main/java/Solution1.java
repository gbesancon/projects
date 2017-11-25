// Read inputs from System.in, Write outputs to System.out.
// Your class name has to be Solution

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Solution 1.
 */
public class Solution1
{
  public enum Direction
  {
    SOUTH, EAST, NORTH, WEST
  }

  public static char START = '@';
  public static char SUICIDE_BOOTH = '$';
  public static char BOUNDARY = '#';
  public static char OBSTACLE = 'X';
  public static char SOUTH = 'S';
  public static char EAST = 'E';
  public static char NORTH = 'N';
  public static char WEST = 'W';
  public static char BEER = 'B';
  public static char INVERSE = 'I';
  public static char TELEPORT = 'T';

  public static class Position
  {
    public int line;
    public int column;
  }

  public static class Move
  {
    public Position position;
    public Direction direction;
    public boolean isDrunk = false;
    public boolean isInverse = false;
    public int mapChange;

    @Override
    public boolean equals(Object obj)
    {
      boolean equals = true;
      equals &= position.line == ((Move) obj).position.line;
      equals &= position.column == ((Move) obj).position.column;
      equals &= direction == ((Move) obj).direction;
      equals &= isDrunk == ((Move) obj).isDrunk;
      equals &= isInverse == ((Move) obj).isInverse;
      equals &= mapChange == ((Move) obj).mapChange;
      return equals;
    }
  }

  public static class Map
  {
    public int nbLines = 0;
    public int nbColumns = 0;
    public char[][] map = null;
  }

  public static class Journey
  {
    public boolean isEndOfJourney = false;
    public boolean isLooping = false;
    public List<Move> journey = new ArrayList<Move>();

    public Direction direction = null;
    public boolean isDrunk = false;
    public boolean isInverse = false;
    public int mapChange = 0;
  }

  public static void main(String args[])
  {
    Map map = ParseInput();
    Position startPosition = FindStartPosition(map);
    Journey journey = ExecuteJourney(map, startPosition);
    DisplayOutput(journey);
  }

  public static Map ParseInput()
  {
    Scanner in = new Scanner(System.in);
    Map map = new Map();
    map.nbLines = in.nextInt();
    map.nbColumns = in.nextInt();
    in.nextLine();
    map.map = new char[map.nbLines][map.nbColumns];
    for (int iLine = 0; iLine < map.nbLines; iLine++)
    {
      String line = in.nextLine();
      for (int iColumn = 0; iColumn < map.nbColumns; iColumn++)
      {
        map.map[iLine][iColumn] = line.charAt(iColumn);
      }
    }
    in.close();
    return map;
  }

  public static Position FindStartPosition(Map map)
  {
    Position startPosition = null;
    for (int iLine = 0; iLine < map.nbLines && startPosition == null; iLine++)
    {
      for (int iColumn = 0; iColumn < map.nbColumns && startPosition == null; iColumn++)
      {
        if (map.map[iLine][iColumn] == START)
        {
          startPosition = new Position();
          startPosition.line = iLine;
          startPosition.column = iColumn;
        }
      }
    }
    return startPosition;
  }

  public static Journey ExecuteJourney(Map map, Position startPosition)
  {
    Journey journey = new Journey();
    Position position = startPosition;
    while (!(journey.isEndOfJourney || journey.isLooping))
    {
      ApplyPosition(map, journey, position);
      Move nextMove = ComputeNextMove(map, journey, position);
      if (nextMove != null)
      {
        // System.out.println(nextMove.position.line + " " + nextMove.position.column + " " + nextMove.direction + " " +
        // nextMove.isDrunk + " " + nextMove.isInverse + " " + nextMove.mapChange);
        journey.journey.add(nextMove);
        position = ComputeNextPosition(map, nextMove);
      }
    }
    return journey;
  }

  public static void ApplyPosition(Map map, Journey journey, Position position)
  {
    if (map.map[position.line][position.column] == SUICIDE_BOOTH)
    {
      journey.isEndOfJourney = true;
    }
    else if (map.map[position.line][position.column] == OBSTACLE && journey.isDrunk)
    {
      // Destroy
      map.map[position.line][position.column] = ' ';
      journey.mapChange++;
    }
    else if (map.map[position.line][position.column] == TELEPORT)
    {
      // Teleport
      boolean teleported = false;
      for (int iLine = 0; iLine < map.nbLines && !teleported; iLine++)
      {
        for (int iColumn = 0; iColumn < map.nbColumns && !teleported; iColumn++)
        {
          if (map.map[iLine][iColumn] == TELEPORT)
          {
            if (!(iLine == position.line && iColumn == position.column))
            {
              position.line = iLine;
              position.column = iColumn;
              teleported = true;
            }
          }
        }
      }
    }
    else if (map.map[position.line][position.column] == BEER)
    {
      // Change drunk status
      journey.isDrunk = !journey.isDrunk;
    }
    else if (map.map[position.line][position.column] == INVERSE)
    {
      // Inverse direction order
      journey.isInverse = !journey.isInverse;
    }
    else if (map.map[position.line][position.column] == SOUTH)
    {
      journey.direction = Direction.SOUTH;
    }
    else if (map.map[position.line][position.column] == EAST)
    {
      journey.direction = Direction.EAST;
    }
    else if (map.map[position.line][position.column] == NORTH)
    {
      journey.direction = Direction.NORTH;
    }
    else if (map.map[position.line][position.column] == WEST)
    {
      journey.direction = Direction.WEST;
    }
  }

  public static Direction[] normal_directions_order = { Direction.SOUTH, Direction.EAST, Direction.NORTH,
      Direction.WEST };
  public static Direction[] inversed_directions_order = { Direction.WEST, Direction.NORTH, Direction.EAST,
      Direction.SOUTH };

  public static Move ComputeNextMove(Map map, Journey journey, Position position)
  {
    Move nextMove = null;
    if (!journey.isEndOfJourney)
    {
      boolean foundValidDirection = false;
      nextMove = new Move();
      nextMove.position = new Position();
      nextMove.position.line = position.line;
      nextMove.position.column = position.column;
      nextMove.isDrunk = journey.isDrunk;
      nextMove.isInverse = journey.isInverse;
      nextMove.mapChange = journey.mapChange;

      if (journey.direction != null)
      {
        nextMove.direction = journey.direction;
        foundValidDirection = CanMove(map, journey, nextMove);
      }

      if (!foundValidDirection)
      {
        Direction[] direction_order = journey.isInverse ? inversed_directions_order : normal_directions_order;
        for (int iDirection = 0; iDirection < direction_order.length && !foundValidDirection; iDirection++)
        {
          nextMove.direction = direction_order[iDirection];
          foundValidDirection = CanMove(map, journey, nextMove);
        }
        journey.direction = nextMove.direction;
      }

      if (journey.journey.contains(nextMove))
      {
        journey.isLooping = true;
        nextMove = null;
      }
    }
    return nextMove;
  }

  public static boolean CanMove(Map map, Journey journey, Move move)
  {
    boolean canMove = true;
    Position newPosition = ComputeNextPosition(map, move);
    if (map.map[newPosition.line][newPosition.column] == BOUNDARY)
    {
      canMove = false;
    }
    else if (map.map[newPosition.line][newPosition.column] == OBSTACLE && !journey.isDrunk)
    {
      canMove = false;
    }
    return canMove;
  }

  public static Position ComputeNextPosition(Map map, Move move)
  {
    Position position = new Position();
    position.line = move.position.line;
    position.column = move.position.column;
    if (move.direction == Direction.SOUTH)
    {
      position.line++;
    }
    else if (move.direction == Direction.EAST)
    {
      position.column++;
    }
    else if (move.direction == Direction.NORTH)
    {
      position.line--;
    }
    else if (move.direction == Direction.WEST)
    {
      position.column--;
    }
    return position;
  }

  public static void DisplayOutput(Journey journey)
  {
    if (journey.isLooping)
    {
      System.out.println("LOOP");
    }
    else if (journey.isEndOfJourney)
    {
      for (Move move : journey.journey)
      {
        System.out.println(move.direction);
      }
    }
  }
}
