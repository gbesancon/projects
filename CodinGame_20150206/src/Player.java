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
    public int width;
    public int height;

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

  class GamePlayer
  {
    public final int id;
    boolean isDead = false;
    public Path path = new Path();
    public int wallsLeft = 0;

    public GamePlayer(int id)
    {
      this.id = id;
    }

    @Override
    public String toString()
    {
      return "id:" + id + ",isDead:" + isDead + ",path(" + path + "),wallsLeft:" + wallsLeft;
    }
  }

  class Path
  {
    public List<DIRECTION> directions = new ArrayList<Player.DIRECTION>();
    public List<Position> positions = new ArrayList<Player.Position>();

    @Override
    public String toString()
    {
      StringBuilder builder = new StringBuilder();
      for (DIRECTION direction : directions)
      {
        builder.append(direction + ";");
      }
      builder.append(" => ");
      for (Position position : positions)
      {
        builder.append(position + ";");
      }
      return builder.toString();
    }
  }

  class Position
  {
    public final int x;
    public final int y;

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
    public final Position position;
    public final ORIENTATION orientation;

    public Wall(Position possition, ORIENTATION orientation)
    {
      this.position = possition;
      this.orientation = orientation;
    }

    public boolean intersect(Wall wall)
    {
      boolean intersect = false;
      if (orientation == ORIENTATION.VERTICAL && wall.orientation == ORIENTATION.VERTICAL)
      {
        if (position.x == wall.position.x)
        {
          if (wall.position.y - 1 <= position.y && position.y <= wall.position.y + 1)
          {
            intersect = true;
          }
        }
      }
      else if (orientation == ORIENTATION.HORIZONTAL && wall.orientation == ORIENTATION.HORIZONTAL)
      {
        if (wall.position.x - 1 <= position.x && position.x <= wall.position.x + 1)
        {
          if (position.y == wall.position.y)
          {
            intersect = true;
          }
        }
      }
      else if (orientation == ORIENTATION.HORIZONTAL && wall.orientation == ORIENTATION.VERTICAL)
      {
        if (position.x + 1 == wall.position.x)
        {
          if (position.y - 1 == wall.position.y)
          {
            intersect = true;
          }
        }
      }
      else if (orientation == ORIENTATION.VERTICAL && wall.orientation == ORIENTATION.HORIZONTAL)
      {
        if (position.x - 1 == wall.position.x)
        {
          if (position.y + 1 == wall.position.y)
          {
            intersect = true;
          }
        }
      }
      return intersect;
    }

    public boolean isRunningInto(Position position, DIRECTION direction)
    {
      boolean isRunningIntoWall = false;
      if (this.orientation == ORIENTATION.HORIZONTAL)
      {
        boolean validateX = position.x >= this.position.x && position.x < this.position.x + 2;
        if (direction == DIRECTION.UP)
        {
          isRunningIntoWall = validateX && (position.y == this.position.y);
        }
        else if (direction == DIRECTION.DOWN)
        {
          isRunningIntoWall = validateX && (position.y + 1 == this.position.y);
        }
        else
        {
          // Going Left or Right : no problem
        }
      }
      else
      {
        boolean validateY = position.y >= this.position.y && position.y < this.position.y + 2;
        if (direction == DIRECTION.LEFT)
        {
          isRunningIntoWall = (position.x == this.position.x) && validateY;
        }
        else if (direction == DIRECTION.RIGHT)
        {
          isRunningIntoWall = (position.x + 1 == this.position.x) && validateY;
        }
        else
        {
          // Going Up or Down : no problem
        }
      }
      return isRunningIntoWall;
    }

    @Override
    public String toString()
    {
      return "position(" + position + "),orientation:" + orientation;
    }
  }

  enum ORIENTATION
  {
    HORIZONTAL, VERTICAL
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
    public final DIRECTION direction;

    public MoveAction(DIRECTION direction)
    {
      this.direction = direction;
    }

    @Override
    public int computeScore()
    {
      int score = 0;
      if ((getMyGamePlayer().path.directions.size() == 0)
          || (getMyGamePlayer().path.directions.size() != 0 && direction != getOppositeDirection(getMyLastDirection())))
      {
        if (direction == getMyPreferredDirection())
        {
          score = 100;
        }
        else if (direction != getOppositeDirection(getMyPreferredDirection()))
        {
          score = 50;
        }
        else
        {
          score = 20;
        }
      }
      else
      {
        // Can move but no progress, just backtracking
        score = 10;
      }
      return score;
    }

    @Override
    public String toString()
    {
      // action: LEFT, RIGHT, UP, DOWN or "putX putY putOrientation" to place a wall
      return direction.toString();
    }
  }

  class PuttWallAction extends Action
  {
    public final int id;
    public final DIRECTION direction;
    public final Wall wall;

    public PuttWallAction(int id, DIRECTION direction, Wall wall)
    {
      this.id = id;
      this.direction = direction;
      this.wall = wall;
    }

    @Override
    public int computeScore()
    {
      int score = 0;
      if (direction == getPreferredDirection(id))
      {
        score = 90;
      }
      else
      {
        score = 40;
      }
      return score;
    }

    @Override
    public String toString()
    {
      // action: LEFT, RIGHT, UP, DOWN or "putX putY putOrientation" to place a wall
      return Integer.toString(wall.position.x) + " " + Integer.toString(wall.position.y) + " "
          + (wall.orientation == ORIENTATION.HORIZONTAL ? "H" : "V");
    }
  }

  public GamePlayer getMyGamePlayer()
  {
    return getGamePlayer(world.myId);
  }

  public GamePlayer getGamePlayer(int id)
  {
    return world.gamePlayers.get(id);
  }

  public Position getMyLastPosition()
  {
    return getLastPosition(world.myId);
  }

  public Position getLastPosition(int id)
  {
    return getGamePlayer(id).path.positions.get(0);
  }

  public DIRECTION getMyPreferredDirection()
  {
    return getPreferredDirection(world.myId);
  }

  public DIRECTION getPreferredDirection(int id)
  {
    final DIRECTION direction;
    switch (id)
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
        direction = DIRECTION.NONE;
        break;
    }
    return direction;
  }

  public DIRECTION getMyLastDirection()
  {
    return getLastDirection(world.myId);
  }

  public DIRECTION getLastDirection(int id)
  {
    DIRECTION direction = DIRECTION.NONE;
    for (DIRECTION aDirection : getGamePlayer(id).path.directions)
    {
      if (direction == DIRECTION.NONE && aDirection != DIRECTION.NONE)
      {
        direction = aDirection;
      }
    }
    return direction;
  }

  public DIRECTION getDirection(Position position0, Position position1)
  {
    final DIRECTION direction;
    int deltaX = position1.x - position0.x;
    int deltaY = position1.y - position0.y;
    if (deltaX > 0)
    {
      direction = DIRECTION.RIGHT;
    }
    else if (deltaX < 0)
    {
      direction = DIRECTION.LEFT;
    }
    else if (deltaY > 0)
    {
      direction = DIRECTION.DOWN;
    }
    else if (deltaY < 0)
    {
      direction = DIRECTION.UP;
    }
    else
    {
      direction = DIRECTION.NONE;
    }
    return direction;
  }

  public DIRECTION getOppositeDirection(DIRECTION direction)
  {
    final DIRECTION oppositeDirection;
    switch (direction)
    {
      case RIGHT:
        oppositeDirection = DIRECTION.LEFT;
        break;
      case LEFT:
        oppositeDirection = DIRECTION.RIGHT;
        break;
      case UP:
        oppositeDirection = DIRECTION.DOWN;
        break;
      case DOWN:
        oppositeDirection = DIRECTION.UP;
        break;
      default:
        oppositeDirection = DIRECTION.NONE;
        break;
    }
    return oppositeDirection;
  }

  public Position computeNextPosition(Position position, DIRECTION direction)
  {
    final Position nextPosition;
    switch (direction)
    {
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
        nextPosition = null;
        break;
    }
    return nextPosition;
  }

  public static void main(String args[])
  {
    Scanner in = new Scanner(System.in);

    Player player = new Player();

    player.world.board.width = in.nextInt(); // width of the board
    player.world.board.height = in.nextInt(); // height of the board
    int playerCount = in.nextInt(); // number of players (2,3, or 4)
    player.world.myId = in.nextInt(); // id of my player (0 = 1st player, 1 = 2nd player, ...)

    for (int i = 0; i < playerCount; i++)
    {
      GamePlayer gamePlayer = player.new GamePlayer(i);
      player.world.gamePlayers.add(gamePlayer);
    }

    // game loop
    while (true)
    {
      for (int i = 0; i < playerCount; i++)
      {
        GamePlayer gamePlayer = player.getGamePlayer(i);
        int x = in.nextInt(); // x-coordinate of the player
        int y = in.nextInt(); // y-coordinate of the player
        int wallsLeft = in.nextInt(); // number of walls available for the player
        if (!gamePlayer.isDead)
        {
          if (x == -1 && y == -1 && wallsLeft == -1)
          {
            gamePlayer.isDead = true;
          }
          else
          {
            if (gamePlayer.path.positions.size() == 0)
            {
              gamePlayer.path.positions.add(0, player.new Position(x, y));
            }
            else
            {
              gamePlayer.path.positions.add(0, player.new Position(x, y));
              gamePlayer.path.directions.add(0, player.getDirection(gamePlayer.path.positions.get(1),
                                                                    gamePlayer.path.positions.get(0)));
            }
            gamePlayer.wallsLeft = wallsLeft;
          }
        }
      }

      int wallCount = in.nextInt(); // number of walls on the board
      player.world.walls.clear();
      for (int i = 0; i < wallCount; i++)
      {
        int x = in.nextInt(); // x-coordinate of the wall
        int y = in.nextInt(); // y-coordinate of the wall
        String orientation = in.next(); // wall orientation ('H' or 'V')
        player.world.walls.add(player.new Wall(player.new Position(x, y),
            orientation.compareTo("H") == 0 ? ORIENTATION.HORIZONTAL : ORIENTATION.VERTICAL));
      }

      System.err.println(player.world);

      String action = player.computeAction();
      // Write an action using System.out.println()
      System.out.println(action);
    }
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

  public List<Action> computePotentialActions()
  {
    List<Action> actions = new ArrayList<Player.Action>();
    actions.addAll(computePotentialMoveActions());
    actions.addAll(computePotentialPutWallActions());
    return actions;
  }

  public List<Action> computePotentialMoveActions()
  {
    List<Action> actions = new ArrayList<Player.Action>();
    int algorithm = 0;
    if (algorithm == 0)
    {
      for (DIRECTION direction : DIRECTION.values())
      {
        if (direction != DIRECTION.NONE)
        {
          if (canMove(getMyLastPosition(), direction))
          {
            actions.add(new MoveAction(direction));
          }
        }
      }
    }
    else if (algorithm == 1)
    {
      List<DIRECTION> shortestPathGuidance = computeShortestPathGuidance();
      actions.add(new MoveAction(shortestPathGuidance.get(0)));
    }
    return actions;
  }

  public boolean canMove(Position position, DIRECTION direction)
  {
    boolean canMove = false;
    Position nextPosition = computeNextPosition(position, direction);
    if (world.board.isInside(nextPosition))
    {
      if (getBlockingWall(position, direction) == null)
      {
        canMove = true;
      }
    }
    return canMove;
  }

  public Wall getBlockingWall(Position position, DIRECTION direction)
  {
    Wall blockingWall = null;
    for (Wall wall : world.walls)
    {
      if (blockingWall == null)
      {
        if (wall.isRunningInto(position, direction))
        {
          blockingWall = wall;
        }
      }
    }
    return blockingWall;
  }

  public List<DIRECTION> computeShortestPathGuidance()
  {
    List<DIRECTION> guidance = new ArrayList<Player.DIRECTION>();
    computeShortestPathGuidance(getMyLastPosition(), getMyPreferredDirection(), guidance);
    return guidance;
  }

  public boolean computeShortestPathGuidance(Position position, DIRECTION preferredDirection, List<DIRECTION> guidance)
  {
    boolean foundPath = false;
    // Stop conditions
    if ((preferredDirection == DIRECTION.RIGHT && position.x == world.board.width - 1)
        || (preferredDirection == DIRECTION.LEFT && position.x == 0)
        || (preferredDirection == DIRECTION.UP && position.y == 0)
        || (preferredDirection == DIRECTION.DOWN && position.y == world.board.height - 1))
    {
      guidance.add(preferredDirection);
      foundPath = true;
    }
    else if (guidance.size() > 14)
    {
      // Cut infinite loop
      foundPath = false;
    }
    // Search the path
    else
    {
      DIRECTION[] directions = new DIRECTION[4];
      int iDirection = 0;
      directions[iDirection++] = preferredDirection;
      for (DIRECTION direction : DIRECTION.values())
      {
        if (direction != preferredDirection)
        {
          directions[iDirection++] = direction;
        }
      }
      DIRECTION shortestPathNextDirection = null;
      List<DIRECTION> shortestPathTmpGuidance = null;
      for (DIRECTION direction : directions)
      {
        if (canMove(getMyLastPosition(), direction))
        {
          List<DIRECTION> tmpGuidance = new ArrayList<Player.DIRECTION>();
          tmpGuidance.addAll(guidance);
          tmpGuidance.add(direction);
          Position nextPosition = computeNextPosition(position, direction);
          boolean foundSubPath = computeShortestPathGuidance(nextPosition, preferredDirection, tmpGuidance);
          if (foundSubPath)
          {
            if (shortestPathTmpGuidance == null || (tmpGuidance.size() < shortestPathTmpGuidance.size()))
            {
              shortestPathNextDirection = direction;
              shortestPathTmpGuidance = tmpGuidance;
            }
          }
        }
      }

      if (shortestPathNextDirection != null)
      {
        guidance.clear();
        guidance.addAll(shortestPathTmpGuidance);
        foundPath = true;
      }
      else
      {
        foundPath = false;
      }
    }
    return foundPath;
  }

  public List<Action> computePotentialPutWallActions()
  {
    List<Action> actions = new ArrayList<Player.Action>();
    if (getMyGamePlayer().wallsLeft > 0)
    {
      for (GamePlayer gamePlayer : world.gamePlayers)
      {
        if (gamePlayer.id != world.myId)
        {
          Position position = getLastPosition(gamePlayer.id);
          for (DIRECTION direction : DIRECTION.values())
          {
            if (direction != DIRECTION.NONE)
            {
              if (canMove(position, direction))
              {
                List<Wall> blockingWalls = computeBlockingWall(position, direction);
                for (Wall blockingWall : blockingWalls)
                {
                  actions.add(new PuttWallAction(gamePlayer.id, direction, blockingWall));
                }
              }
            }
          }
        }
      }
    }
    return actions;
  }

  public List<Wall> computeBlockingWall(Position position, DIRECTION direction)
  {
    List<Wall> walls = new ArrayList<Player.Wall>();
    final Wall wall1;
    final Wall wall2;
    switch (direction)
    {
      case LEFT:
        wall1 = new Wall(new Position(position.x, position.y - 1), ORIENTATION.VERTICAL);
        wall2 = new Wall(new Position(position.x, position.y), ORIENTATION.VERTICAL);
        break;
      case RIGHT:
        wall1 = new Wall(new Position(position.x + 1, position.y - 1), ORIENTATION.VERTICAL);
        wall2 = new Wall(new Position(position.x + 1, position.y), ORIENTATION.VERTICAL);
        break;
      case UP:
        wall1 = new Wall(new Position(position.x - 1, position.y), ORIENTATION.HORIZONTAL);
        wall2 = new Wall(new Position(position.x, position.y), ORIENTATION.HORIZONTAL);
        break;
      case DOWN:
        wall1 = new Wall(new Position(position.x - 1, position.y + 1), ORIENTATION.HORIZONTAL);
        wall2 = new Wall(new Position(position.x, position.y + 1), ORIENTATION.HORIZONTAL);
        break;
      default:
        wall1 = null;
        wall2 = null;
        break;
    }
    if (wall1 != null && canPutWall(wall1))
    {
      walls.add(wall1);
    }
    if (wall2 != null && canPutWall(wall2))
    {
      walls.add(wall2);
    }
    return walls;
  }

  public boolean canPutWall(Wall wall)
  {
    boolean canPutWall = true;
    switch (wall.orientation)
    {
      case HORIZONTAL:
        if ((wall.position.x >= 0 && wall.position.x < world.board.width - 1)
            && (wall.position.y > 0 && wall.position.y < world.board.height))
        {
          for (Wall aWall : world.walls)
          {
            canPutWall &= !wall.intersect(aWall);
          }
        }
        else
        {
          canPutWall = false;
        }
        break;
      case VERTICAL:
        if ((wall.position.x > 0 && wall.position.x < world.board.width)
            && (wall.position.y >= 0 && wall.position.y < world.board.height - 1))
        {
          for (Wall aWall : world.walls)
          {
            canPutWall &= !wall.intersect(aWall);
          }
        }
        else
        {
          canPutWall = false;
        }
        break;
    }
    return canPutWall;
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
}
