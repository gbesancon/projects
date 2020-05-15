// Copyright (C) 2017 GBesancon

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

/**
 * Auto-generated code below aims at helping you parse the standard input according to the problem
 * statement.
 */
public class Player {
  public World world = new World();

  class World {
    public Board board = new Board();
    public List<Wall> walls = new ArrayList<Player.Wall>();
    public int myId = 0;
    public List<GamePlayer> gamePlayers = new ArrayList<Player.GamePlayer>();

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("Board(" + board + ")\n");
      for (Wall wall : walls) {
        builder.append("Wall(" + wall + ")\n");
      }
      builder.append("myId:" + myId + "\n");
      for (GamePlayer gamePlayer : gamePlayers) {
        builder.append("GamePlayer(" + gamePlayer + ")\n");
      }
      return builder.toString();
    }
  }

  class Board {
    public int width;
    public int height;

    boolean isInside(Position position) {
      return position.x >= 0 && position.x < width && position.y >= 0 && position.y < height;
    }

    @Override
    public String toString() {
      return "w:" + width + ",h:" + height;
    }
  }

  class GamePlayer {
    public final int id;
    boolean isDead = false;
    public Path path = new Path();
    public int wallsLeft = 0;

    public GamePlayer(int id) {
      this.id = id;
    }

    @Override
    public String toString() {
      return "id:" + id + ",isDead:" + isDead + ",path(" + path + "),wallsLeft:" + wallsLeft;
    }
  }

  class Path {
    public List<DIRECTION> directions = new ArrayList<Player.DIRECTION>();
    public List<Position> positions = new ArrayList<Player.Position>();

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      for (DIRECTION direction : directions) {
        builder.append(direction + ";");
      }
      builder.append(" => ");
      for (Position position : positions) {
        builder.append(position + ";");
      }
      return builder.toString();
    }
  }

  class Position {
    public final int x;
    public final int y;

    public Position(int x, int y) {
      this.x = x;
      this.y = y;
    }

    @Override
    public String toString() {
      return "x:" + x + ",y:" + y;
    }
  }

  class Wall {
    public final Position position;
    public final ORIENTATION orientation;

    public Wall(Position possition, ORIENTATION orientation) {
      this.position = possition;
      this.orientation = orientation;
    }

    public boolean intersect(Wall wall) {
      boolean intersect = false;
      if (orientation == ORIENTATION.VERTICAL && wall.orientation == ORIENTATION.VERTICAL) {
        if (position.x == wall.position.x) {
          if (wall.position.y - 1 <= position.y && position.y <= wall.position.y + 1) {
            intersect = true;
          }
        }
      } else if (orientation == ORIENTATION.HORIZONTAL
          && wall.orientation == ORIENTATION.HORIZONTAL) {
        if (wall.position.x - 1 <= position.x && position.x <= wall.position.x + 1) {
          if (position.y == wall.position.y) {
            intersect = true;
          }
        }
      } else if (orientation == ORIENTATION.HORIZONTAL
          && wall.orientation == ORIENTATION.VERTICAL) {
        if (position.x + 1 == wall.position.x) {
          if (position.y - 1 == wall.position.y) {
            intersect = true;
          }
        }
      } else if (orientation == ORIENTATION.VERTICAL
          && wall.orientation == ORIENTATION.HORIZONTAL) {
        if (position.x - 1 == wall.position.x) {
          if (position.y + 1 == wall.position.y) {
            intersect = true;
          }
        }
      }
      return intersect;
    }

    public boolean isRunningInto(Position position, DIRECTION direction) {
      boolean isRunningIntoWall = false;
      if (this.orientation == ORIENTATION.HORIZONTAL) {
        boolean validateX = position.x >= this.position.x && position.x < this.position.x + 2;
        if (direction == DIRECTION.UP) {
          isRunningIntoWall = validateX && (position.y == this.position.y);
        } else if (direction == DIRECTION.DOWN) {
          isRunningIntoWall = validateX && (position.y + 1 == this.position.y);
        } else {
          // Going Left or Right : no problem
        }
      } else {
        boolean validateY = position.y >= this.position.y && position.y < this.position.y + 2;
        if (direction == DIRECTION.LEFT) {
          isRunningIntoWall = (position.x == this.position.x) && validateY;
        } else if (direction == DIRECTION.RIGHT) {
          isRunningIntoWall = (position.x + 1 == this.position.x) && validateY;
        } else {
          // Going Up or Down : no problem
        }
      }
      return isRunningIntoWall;
    }

    @Override
    public String toString() {
      return "position(" + position + "),orientation:" + orientation;
    }
  }

  enum ORIENTATION {
    HORIZONTAL,
    VERTICAL
  }

  abstract class Action {
    // -100 < score < 0 : Deadly action
    // 0 : No action
    // 0 < score < 100 : Survival action
    int score = 0;

    public void setScore(int score) {
      this.score = score;
    }

    public int getScore() {
      return score;
    }
  }

  enum DIRECTION {
    NONE,
    UP,
    RIGHT,
    DOWN,
    LEFT
  }

  class MoveAction extends Action {
    public final DIRECTION direction;

    public MoveAction(DIRECTION direction) {
      this.direction = direction;
    }

    @Override
    public String toString() {
      // action: LEFT, RIGHT, UP, DOWN or "putX putY putOrientation" to place a wall
      return direction.toString();
    }
  }

  class PutWallAction extends Action {
    public final int id;
    public final Wall wall;

    public PutWallAction(int id, Wall wall) {
      this.id = id;
      this.wall = wall;
    }

    @Override
    public String toString() {
      // action: LEFT, RIGHT, UP, DOWN or "putX putY putOrientation" to place a wall
      return Integer.toString(wall.position.x)
          + " "
          + Integer.toString(wall.position.y)
          + " "
          + (wall.orientation == ORIENTATION.HORIZONTAL ? "H" : "V");
    }
  }

  public GamePlayer getMyGamePlayer() {
    return getGamePlayer(world.myId);
  }

  public GamePlayer getGamePlayer(int id) {
    return world.gamePlayers.get(id);
  }

  public Position getMyLastPosition() {
    return getLastPosition(world.myId);
  }

  public Position getLastPosition(int id) {
    return getGamePlayer(id).path.positions.get(0);
  }

  public boolean isStressed(int id) {
    return getStress(id) > 0.5;
  }

  public float getMyStress() {
    return getStress(world.myId);
  }

  public float getStress(int id) {
    float stress = 0;
    for (GamePlayer gamePlayer : world.gamePlayers) {
      if (gamePlayer.id != id) {
        int distanceToGoal = getDistanceToGoal(gamePlayer.id);
        DIRECTION direction = getPreferredDirection(gamePlayer.id);
        switch (direction) {
          case LEFT:
          case RIGHT:
            stress = 1.0f - (1.0f * distanceToGoal) / (1.0f * world.board.height);
            break;
          case UP:
          case DOWN:
            stress = 1.0f - (1.0f * distanceToGoal) / (1.0f * world.board.width);
            break;
          default:
            break;
        }
      }
    }
    return stress;
  }

  public int getDistanceToGoal(int id) {
    int distanceToGoal = 0;
    Position lastPosition = getLastPosition(id);
    DIRECTION direction = getPreferredDirection(id);
    switch (direction) {
      case LEFT:
        distanceToGoal = lastPosition.x;
        break;
      case RIGHT:
        distanceToGoal = world.board.width - lastPosition.x - 1;
        break;
      case UP:
        distanceToGoal = lastPosition.y;
        break;
      case DOWN:
        distanceToGoal = world.board.height - lastPosition.y - 1;
        break;
      default:
        break;
    }
    return distanceToGoal;
  }

  public DIRECTION getMyPreferredDirection() {
    return getPreferredDirection(world.myId);
  }

  public DIRECTION getPreferredDirection(int id) {
    final DIRECTION direction;
    switch (id) {
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

  public DIRECTION getMyLastDirection() {
    return getLastDirection(world.myId);
  }

  public DIRECTION getLastDirection(int id) {
    DIRECTION direction = DIRECTION.NONE;
    for (DIRECTION aDirection : getGamePlayer(id).path.directions) {
      if (direction == DIRECTION.NONE && aDirection != DIRECTION.NONE) {
        direction = aDirection;
      }
    }
    return direction;
  }

  public DIRECTION getDirection(Position position0, Position position1) {
    final DIRECTION direction;
    int deltaX = position1.x - position0.x;
    int deltaY = position1.y - position0.y;
    if (deltaX > 0) {
      direction = DIRECTION.RIGHT;
    } else if (deltaX < 0) {
      direction = DIRECTION.LEFT;
    } else if (deltaY > 0) {
      direction = DIRECTION.DOWN;
    } else if (deltaY < 0) {
      direction = DIRECTION.UP;
    } else {
      direction = DIRECTION.NONE;
    }
    return direction;
  }

  public DIRECTION getOppositeDirection(DIRECTION direction) {
    final DIRECTION oppositeDirection;
    switch (direction) {
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

  public Position computeNextPosition(Position position, DIRECTION direction) {
    final Position nextPosition;
    switch (direction) {
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

  public static void main(String args[]) {
    Scanner in = new Scanner(System.in);

    Player player = new Player();

    player.world.board.width = in.nextInt(); // width of the board
    player.world.board.height = in.nextInt(); // height of the board
    int playerCount = in.nextInt(); // number of players (2,3, or 4)
    player.world.myId = in.nextInt(); // id of my player (0 = 1st player, 1 = 2nd player, ...)

    for (int i = 0; i < playerCount; i++) {
      GamePlayer gamePlayer = player.new GamePlayer(i);
      player.world.gamePlayers.add(gamePlayer);
    }

    // game loop
    while (true) {
      for (int i = 0; i < playerCount; i++) {
        GamePlayer gamePlayer = player.getGamePlayer(i);
        int x = in.nextInt(); // x-coordinate of the player
        int y = in.nextInt(); // y-coordinate of the player
        int wallsLeft = in.nextInt(); // number of walls available for the player
        if (!gamePlayer.isDead) {
          if (x == -1 && y == -1 && wallsLeft == -1) {
            gamePlayer.isDead = true;
          } else {
            if (gamePlayer.path.positions.size() == 0) {
              gamePlayer.path.positions.add(0, player.new Position(x, y));
            } else {
              gamePlayer.path.positions.add(0, player.new Position(x, y));
              gamePlayer.path.directions.add(
                  0,
                  player.getDirection(
                      gamePlayer.path.positions.get(1), gamePlayer.path.positions.get(0)));
            }
            gamePlayer.wallsLeft = wallsLeft;
          }
        }
      }

      int wallCount = in.nextInt(); // number of walls on the board
      player.world.walls.clear();
      for (int i = 0; i < wallCount; i++) {
        int x = in.nextInt(); // x-coordinate of the wall
        int y = in.nextInt(); // y-coordinate of the wall
        String orientation = in.next(); // wall orientation ('H' or 'V')
        player.world.walls.add(
            player
            .new Wall(
                player.new Position(x, y),
                orientation.compareTo("H") == 0 ? ORIENTATION.HORIZONTAL : ORIENTATION.VERTICAL));
      }

      // System.err.println(player.world);

      String action = player.computeAction();
      // Write an action using System.out.println()
      System.out.println(action);
    }
  }

  public String computeAction() {
    List<Action> actions = computePotentialActions();
    sortActionsByScore(actions);
    printActions(actions);
    return selectRandom(actions, getNbTieActions(actions)).toString();
  }

  public void printActions(List<Action> actions) {
    System.err.println("myStress:" + getMyStress());
    System.err.println("tie:" + getNbTieActions(actions));
    for (Action action : actions) {
      System.err.println(action + ":" + action.getScore());
    }
  }

  public int getNbTieActions(List<Action> actions) {
    int tie = 0;
    int maxScore = actions.get(0).getScore();
    for (Action action : actions) {
      if (action.getScore() == maxScore) {
        tie++;
      }
    }
    return tie;
  }

  public List<Action> computePotentialActions() {
    List<Action> actions = new ArrayList<Player.Action>();
    actions.addAll(computePotentialMoveActions());
    actions.addAll(computePotentialPutWallActions());
    return actions;
  }

  public List<Action> computePotentialMoveActions() {
    List<Action> actions = new ArrayList<Player.Action>();
    List<DIRECTION> directions = computeMyHeatMapDirections();
    DIRECTION direction = selectRandom(directions, directions.size());
    MoveAction moveAction = new MoveAction(direction);
    int score = 0;
    // Preferred direction
    if (!isStressed(world.myId)) {
      // No stress we keep in preferred direction
      score = 100;
    } else {
      // Stressed, give a chance to attack
      score = 50;
    }
    moveAction.setScore(score);
    actions.add(moveAction);
    return actions;
  }

  public boolean canMove(Position position, DIRECTION direction) {
    boolean canMove = false;
    Position nextPosition = computeNextPosition(position, direction);
    if (nextPosition != null) {
      if (world.board.isInside(nextPosition)) {
        if (getBlockingWall(position, direction) == null) {
          canMove = true;
        }
      }
    }
    return canMove;
  }

  public Wall getBlockingWall(Position position, DIRECTION direction) {
    Wall blockingWall = null;
    for (Wall wall : world.walls) {
      if (blockingWall == null) {
        if (wall.isRunningInto(position, direction)) {
          blockingWall = wall;
        }
      }
    }
    return blockingWall;
  }

  public List<DIRECTION> computeMyHeatMapDirections() {
    return computeHeatMapDirections(world.myId);
  }

  public List<DIRECTION> computeHeatMapDirections(int id) {
    float[][] heatMap = computeHeatMap(id);
    // printHeatMap(heatMap);
    List<DIRECTION> directions = computeDirectionsFromHeatMap(heatMap, id);
    return directions;
  }

  public void printHeatMap(float[][] heatMap) {
    StringBuilder builder = new StringBuilder();
    for (int iY = 0; iY < world.board.height; iY++) {
      for (int iX = 0; iX < world.board.width; iX++) {
        builder.append(heatMap[iX][iY]);
        builder.append(" ");
      }
      builder.append("\n");
    }
    System.err.println(builder.toString());
  }

  public float[][] computeHeatMap(int id) {
    float[][] heatMap = new float[world.board.width][world.board.height];
    List<Position> firstStepHotPositionsToVisit =
        setHeatMapSeed(heatMap, getPreferredDirection(id));
    List<Position> positionVisited = new ArrayList<Player.Position>();
    visitStep(heatMap, firstStepHotPositionsToVisit, positionVisited);
    return heatMap;
  }

  public List<Position> setHeatMapSeed(float[][] heatMap, DIRECTION preferredDirection) {
    List<Position> hotPositions = new ArrayList<Player.Position>();
    for (int iX = 0; iX < world.board.width; iX++) {
      for (int iY = 0; iY < world.board.height; iY++) {
        heatMap[iX][iY] = -1.0f;
      }
    }
    switch (preferredDirection) {
      case UP:
        for (int iX = 0; iX < world.board.width; iX++) {
          heatMap[iX][0] = 1.0f;
          hotPositions.add(new Position(iX, 0));
        }
        break;
      case DOWN:
        for (int iX = 0; iX < world.board.width; iX++) {
          heatMap[iX][world.board.height - 1] = 1.0f;
          hotPositions.add(new Position(iX, world.board.height - 1));
        }
        break;
      case LEFT:
        for (int iY = 0; iY < world.board.height; iY++) {
          heatMap[0][iY] = 1.0f;
          hotPositions.add(new Position(0, iY));
        }
        break;
      case RIGHT:
        for (int iY = 0; iY < world.board.height; iY++) {
          heatMap[world.board.width - 1][iY] = 1.0f;
          hotPositions.add(new Position(world.board.width - 1, iY));
        }
        break;

      default:
        break;
    }
    return hotPositions;
  }

  public void visitStep(
      float[][] heatMap, List<Position> stepHotPositionsToVisit, List<Position> positionVisited) {
    List<Position> nextStepHotPositionsToVisit = new ArrayList<Player.Position>();
    for (Position hotPosition : stepHotPositionsToVisit) {
      visitPosition(heatMap, hotPosition, positionVisited, nextStepHotPositionsToVisit);
    }
    if (!nextStepHotPositionsToVisit.isEmpty()) {
      visitStep(heatMap, nextStepHotPositionsToVisit, positionVisited);
    }
  }

  public void visitPosition(
      float[][] heatMap,
      Position hotPosition,
      List<Position> positionVisited,
      List<Position> nextStepHotPositionsToVisit) {
    float newHeat = 0.9f * heatMap[hotPosition.x][hotPosition.y];
    for (DIRECTION direction : DIRECTION.values()) {
      if (direction != DIRECTION.NONE) {
        if (canMove(hotPosition, direction)) {
          Position nextPosition = computeNextPosition(hotPosition, direction);
          if (newHeat > heatMap[nextPosition.x][nextPosition.y]) {
            if (heatMap[nextPosition.x][nextPosition.y] < 0.0f) {
              if (!nextStepHotPositionsToVisit.contains(nextPosition)
                  && !positionVisited.contains(nextPosition)) {
                nextStepHotPositionsToVisit.add(nextPosition);
              }
            }
            heatMap[nextPosition.x][nextPosition.y] = newHeat;
          }
        }
      }
    }
    positionVisited.add(hotPosition);
  }

  public List<DIRECTION> computeDirectionsFromHeatMap(float[][] heatMap, int id) {
    List<DIRECTION> directions = new ArrayList<Player.DIRECTION>();
    Position position = getLastPosition(id);
    float maxHeat = -1.0f;
    for (DIRECTION direction : DIRECTION.values()) {
      if (canMove(position, direction)) {
        Position nextPosition = computeNextPosition(position, direction);
        float heatValue = heatMap[nextPosition.x][nextPosition.y];
        if (heatValue > maxHeat) {
          maxHeat = heatValue;
          directions.clear();
          directions.add(direction);
        } else if (heatValue == maxHeat) {
          directions.add(direction);
        } else {
          // Not interesting
        }
      }
    }
    return directions;
  }

  public List<Action> computePotentialPutWallActions() {
    List<Action> actions = new ArrayList<Player.Action>();
    if (getMyGamePlayer().wallsLeft > 0) {
      for (GamePlayer gamePlayer : world.gamePlayers) {
        if (gamePlayer.id != world.myId) {
          Position position = getLastPosition(gamePlayer.id);
          DIRECTION direction = getPreferredDirection(gamePlayer.id);
          if (canMove(position, direction)) {
            List<Wall> blockingWalls = computeBlockingWall(position, direction);
            for (Wall blockingWall : blockingWalls) {
              PutWallAction putWallAction = new PutWallAction(gamePlayer.id, blockingWall);
              int score = 0;
              // player can move in it's preferred direction.
              if (isStressed(world.myId)) {
                // Stressed, we attack
                score = 100;
              } else {
                // No stress we prefer moving in right direction if we can
                score = 50;
              }
              putWallAction.setScore(score);
              actions.add(putWallAction);
            }
          }
        }
      }
    }
    return actions;
  }

  public List<Wall> computeBlockingWall(Position position, DIRECTION direction) {
    List<Wall> walls = new ArrayList<Player.Wall>();
    final Wall wall1;
    final Wall wall2;
    switch (direction) {
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
    if (wall1 != null && canPutWall(wall1)) {
      walls.add(wall1);
    }
    if (wall2 != null && canPutWall(wall2)) {
      walls.add(wall2);
    }
    return walls;
  }

  public boolean canPutWall(Wall wall) {
    boolean canPutWall = true;
    switch (wall.orientation) {
      case HORIZONTAL:
        if ((wall.position.x >= 0 && wall.position.x < world.board.width - 1)
            && (wall.position.y > 0 && wall.position.y < world.board.height)) {
          for (Wall aWall : world.walls) {
            canPutWall &= !wall.intersect(aWall);
          }
        } else {
          canPutWall = false;
        }
        break;
      case VERTICAL:
        if ((wall.position.x > 0 && wall.position.x < world.board.width)
            && (wall.position.y >= 0 && wall.position.y < world.board.height - 1)) {
          for (Wall aWall : world.walls) {
            canPutWall &= !wall.intersect(aWall);
          }
        } else {
          canPutWall = false;
        }
        break;
    }
    return canPutWall;
  }

  public void sortActionsByScore(List<Action> actions) {
    Collections.sort(
        actions,
        new Comparator<Action>() {
          @Override
          public int compare(Action o1, Action o2) {
            return o2.getScore() - o1.getScore();
          }
        });
  }

  public <T> T selectRandom(List<T> list, int nbFirstElementToPickFrom) {
    int iIndex = 0;
    if (nbFirstElementToPickFrom > 1) {
      double randomValue = Math.random();
      double value = randomValue * (nbFirstElementToPickFrom - 1);
      iIndex = (int) Math.rint(value);
    }
    return list.get(iIndex);
  }
}
