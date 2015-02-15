import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

/**
 * Auto-generated code below aims at helping you parse the standard input according to the problem statement.
 **/
class Player
{
  public World world = new World();

  class World
  {
    public Board board = new Board();
    public List<Wall> walls = new ArrayList<Player.Wall>();
    public int myId = 0;
    public List<GamePlayer> gamePlayers = new ArrayList<Player.GamePlayer>();

    @Override
    public String toString()
    {
      StringBuilder builder = new StringBuilder();
      builder.append("Board(" + board + ")\n");
      for (Wall wall : walls)
      {
        builder.append("Wall(" + wall + ")\n");
      }
      builder.append("myId:" + myId + "\n");
      for (GamePlayer gamePlayer : gamePlayers)
      {
        builder.append("GamePlayer(" + gamePlayer + ")\n");
      }
      return builder.toString();
    }
  }

  class Board
  {
    public int width = 0;
    public int height = 0;

    boolean isInside(Position position)
    {
      return position.x >= 0 && position.x < width && position.y >= 0 && position.y < height;
    }

    @Override
    public String toString()
    {
      return "w:" + width + ",h:" + height;
    }
  }

  class Position
  {
    public int x = 0;
    public int y = 0;

    public Position(int x, int y)
    {
      this.x = x;
      this.y = y;
    }

    @Override
    public String toString()
    {
      return "x:" + x + ",y:" + y;
    }
  }

  class Wall
  {
    //
    public Position position = new Position(0, 0);
    public ORIENTATION orientation = ORIENTATION.VERTICAL;

    @Override
    public String toString()
    {
      return "position(" + position + "),orientation:" + orientation;
    }
  }

  abstract class Action
  {
    boolean scoreComputed = false;
    int score = 0;

    public int getScore()
    {
      if (!scoreComputed)
      {
        score = computeScore();
      }
      return score;
    }

    // -100 < score < 0 : Deadly action
    // 0 : No action
    // 0 < score < 100 : Survival action
    public abstract int computeScore();
  }

  enum DIRECTION
  {
    NONE, UP, RIGHT, DOWN, LEFT
  }

  class MoveAction extends Action
  {
    public DIRECTION direction = DIRECTION.NONE;

    public MoveAction(DIRECTION direction)
    {
      this.direction = direction;
    }

    @Override
    public int computeScore()
    {
      int score = 0; // Case of DIRECTION.NONE
      if (direction != DIRECTION.NONE)
      {
        if (canMove(GetMyPosition(), direction))
        {
          if (getMyPreferredDirection() == direction)
          {
            score = 100;
          }
          else
          {
            score = 50;
          }
        }
        else
        {
          score = -50;
        }
      }
      return score;
    }

    @Override
    public String toString()
    {
      // action: LEFT, RIGHT, UP, DOWN or "putX putY putOrientation" to place a wall
      return direction != DIRECTION.NONE ? direction.toString() : "";
    }
  }

  enum ORIENTATION
  {
    HORIZONTAL, VERTICAL
  }

  class PuttWallAction extends Action
  {
    Position position = new Position(0, 0);
    ORIENTATION orientation = ORIENTATION.VERTICAL;

    public PuttWallAction(int x, int y, ORIENTATION orientation)
    {
      this.position.x = x;
      this.position.y = y;
      this.orientation = orientation;
    }

    @Override
    public int computeScore()
    {
      return -100;
    }

    @Override
    public String toString()
    {
      // action: LEFT, RIGHT, UP, DOWN or "putX putY putOrientation" to place a wall
      return Integer.toString(position.x) + " " + Integer.toString(position.y) + " "
          + (orientation == ORIENTATION.HORIZONTAL ? "H" : "V");
    }
  }

  class GamePlayer
  {
    public int id = 0;
    public Position position = new Position(0, 0);
    public int wallsLeft = 0;

    @Override
    public String toString()
    {
      return "id:" + id + ",position(" + position + "),wallsLeft:" + wallsLeft;
    }
  }

  public static void main(String args[])
  {
    Scanner in = new Scanner(System.in);

    Player player = new Player();

    player.world.board.width = in.nextInt(); // width of the board
    player.world.board.height = in.nextInt(); // height of the board
    int playerCount = in.nextInt(); // number of players (2,3, or 4)
    player.world.myId = in.nextInt(); // id of my player (0 = 1st player, 1 = 2nd player, ...)

    // game loop
    while (true)
    {
      player.world.gamePlayers.clear();
      for (int i = 0; i < playerCount; i++)
      {
        GamePlayer gamePlayer = player.new GamePlayer();
        gamePlayer.id = i;
        gamePlayer.position.x = in.nextInt(); // x-coordinate of the player
        gamePlayer.position.y = in.nextInt(); // y-coordinate of the player
        gamePlayer.wallsLeft = in.nextInt(); // number of walls available for the player
        player.world.gamePlayers.add(gamePlayer);
      }

      int wallCount = in.nextInt(); // number of walls on the board
      player.world.walls.clear();
      for (int i = 0; i < wallCount; i++)
      {
        Wall wall = player.new Wall();
        wall.position.x = in.nextInt(); // x-coordinate of the wall
        wall.position.y = in.nextInt(); // y-coordinate of the wall
        // wall orientation ('H' or 'V')
        wall.orientation = in.next().compareTo("H") == 0 ? ORIENTATION.HORIZONTAL : ORIENTATION.VERTICAL;
        player.world.walls.add(wall);
      }

      System.err.println(player.world);

      String action = player.computeAction();
      // Write an action using System.out.println()
      System.out.println(action);
    }
  }

  public DIRECTION getMyPreferredDirection()
  {
    DIRECTION direction = DIRECTION.NONE;
    switch (world.myId)
    {
      case 0:
        direction = DIRECTION.RIGHT;
        break;
      case 1:
        direction = DIRECTION.LEFT;
        break;
      case 2:
        direction = DIRECTION.DOWN;
        break;
      case 3:
        direction = DIRECTION.UP;
        break;
      default:
        break;
    }
    return direction;
  }

  public Position computeNextPosition(Position position, DIRECTION direction)
  {
    Position nextPosition = null;
    switch (direction)
    {
      case NONE:
        nextPosition = position;
        break;
      case UP:
        nextPosition = new Position(position.x, position.y - 1);
        break;
      case RIGHT:
        nextPosition = new Position(position.x + 1, position.y);
        break;
      case DOWN:
        nextPosition = new Position(position.x, position.y + 1);
        break;
      case LEFT:
        nextPosition = new Position(position.x - 1, position.y);
        break;
      default:
        break;
    }
    return nextPosition;
  }

  public boolean isRunningIntoWall(Position position, DIRECTION direction)
  {
    boolean isRunningIntoWall = false;
    for (Wall wall : world.walls)
    {
      if (wall.orientation == ORIENTATION.HORIZONTAL)
      {
        boolean validateX = position.x >= wall.position.x && position.x < wall.position.x + 2;
        if (direction == DIRECTION.UP)
        {
          isRunningIntoWall |= validateX && (position.y == wall.position.y);
        }
        else if (direction == DIRECTION.DOWN)
        {
          isRunningIntoWall |= validateX && (position.y + 1 == wall.position.y);
        }
        else
        {
          // Going Left or Right : no problem
        }
      }
      else
      {
        boolean validateY = position.y >= wall.position.y && position.y < wall.position.y + 2;
        if (direction == DIRECTION.LEFT)
        {
          isRunningIntoWall |= (position.x == wall.position.x) && validateY;
        }
        else if (direction == DIRECTION.RIGHT)
        {
          isRunningIntoWall |= (position.x + 1 == wall.position.x) && validateY;
        }
        else
        {
          // Going Up or /Down : no problem
        }
      }
    }
    return isRunningIntoWall;
  }

  public boolean canMove(Position position, DIRECTION direction)
  {
    boolean canMove = false;
    Position nextPosition = computeNextPosition(position, direction);
    if (world.board.isInside(nextPosition))
    {
      if (!isRunningIntoWall(position, direction))
      {
        canMove = true;
      }
    }
    return canMove;
  }

  public Position GetMyPosition()
  {
    return world.gamePlayers.get(world.myId).position;
  }

  public List<Action> computePotentialActions()
  {
    List<Action> actions = new ArrayList<Player.Action>();
    actions.add(new MoveAction(DIRECTION.NONE));
    actions.add(new MoveAction(DIRECTION.UP));
    actions.add(new MoveAction(DIRECTION.RIGHT));
    actions.add(new MoveAction(DIRECTION.DOWN));
    actions.add(new MoveAction(DIRECTION.LEFT));
    return actions;
  }

  public int sortActionsByScore(List<Action> actions)
  {
    Collections.sort(actions, new Comparator<Action>()
    {
      @Override
      public int compare(Action o1, Action o2)
      {
        return o2.getScore() - o1.getScore();
      }
    });

    int tie = 0;
    int maxScore = actions.get(0).getScore();
    for (Action action : actions)
    {
      if (action.getScore() == maxScore)
      {
        tie++;
      }
    }

    return tie;
  }

  public Action selectAction(List<Action> actions, int tie)
  {
    int iIndex = 0;
    if (tie > 1)
    {
      double randomValue = Math.random();
      double value = randomValue * (tie - 1);
      iIndex = (int) Math.rint(value);
    }
    return actions.get(iIndex);
  }

  public String computeAction()
  {
    List<Action> actions = computePotentialActions();
    int tie = sortActionsByScore(actions);
    System.err.println("tie:" + tie);
    for (Action action : actions)
    {
      System.err.println(action + ":" + action.getScore());
    }
    return selectAction(actions, tie).toString();
  }
}
