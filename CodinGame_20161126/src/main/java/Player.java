// Copyright (C) 2017 GBesancon

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Grab Snaffles and try to throw them through the opponent's goal! Move towards a Snaffle and use
 * your team id to determine where you need to throw it.
 */
public class Player {

  class Position {
    public final int x;
    public final int y;

    public Position(int x, int y) {
      this.x = x;
      this.y = y;
    }

    public int computeDistance(Position position) {
      return (int)
          Math.sqrt((position.x - x) * (position.x - x) + (position.y - y) * (position.y - y));
    }

    @Override
    public String toString() {
      return "x=" + x + ",y=" + y;
    }
  }

  class Velocity {
    public final int vx;
    public final int vy;

    public Velocity(int vx, int vy) {
      this.vx = vx;
      this.vy = vy;
    }

    @Override
    public String toString() {
      return "vx=" + vx + ",vy=" + vy;
    }
  }

  class Disc {
    public final Position position;
    public final int radius;

    public Disc(Position position, int radius) {
      this.position = position;
      this.radius = radius;
    }

    @Override
    public String toString() {
      return "position(" + position + "),radius(" + radius + ")";
    }
  }

  class Entity extends Disc {
    public final int id;
    public final Velocity velocity;

    public Entity(int id, Position position, int radius, Velocity velocity) {
      super(position, radius);
      this.id = id;
      this.velocity = velocity;
    }

    @Override
    public String toString() {
      return "id(" + id + ")," + super.toString() + ",velocity(" + velocity + ")";
    }
  }

  class Wizard extends Entity {
    public final boolean holdingSnaffle;

    public Wizard(int id, Position position, Velocity velocity, boolean holdingSnaffle) {
      super(id, position, 400, velocity);
      this.holdingSnaffle = holdingSnaffle;
    }

    @Override
    public String toString() {
      return "Wizard:" + super.toString() + ",holdingSnaffle(" + holdingSnaffle + ")";
    }
  }

  class Snaffle extends Entity {
    public Snaffle(int id, Position position, Velocity velocity) {
      super(id, position, 150, velocity);
    }

    @Override
    public String toString() {
      return "Snaffle:" + super.toString();
    }
  }

  class Bludger extends Entity {
    public Bludger(int id, Position position, Velocity velocity) {
      super(id, position, 200, velocity);
    }

    @Override
    public String toString() {
      return "Bludger:" + super.toString();
    }
  }

  class Pole extends Disc {
    public Pole(Position position, int radius) {
      super(position, radius);
    }
  }

  class Goal {
    public final Pole pole0;
    public final Pole pole1;

    public Goal(Pole pole0, Pole pole1) {
      this.pole0 = pole0;
      this.pole1 = pole1;
    }

    public Position getCenter() {
      return new Position(
          (pole0.position.x + pole1.position.x) / 2, (pole0.position.y + pole1.position.y) / 2);
    }
  }

  class Map {
    public final Position pos0 = new Position(0, 0);
    public final Position pos1 = new Position(16000, 7500);
    public final Goal goal0 =
        new Goal(new Pole(new Position(0, 1750), 150), new Pole(new Position(0, 5750), 150));
    public final Goal goal1 =
        new Goal(
            new Pole(new Position(16000, 1750), 150), new Pole(new Position(16000, 5750), 150));
    public final List<Wizard> wizards = new ArrayList<>();
    public final List<Wizard> opponentWizards = new ArrayList<>();
    public final List<Snaffle> snaffles = new ArrayList<>();
    public final List<Bludger> bludgers = new ArrayList<>();

    public Goal getGoal(int teamId) {
      final Goal goal;
      if (teamId == 0) {
        goal = goal1;
      } else {
        goal = goal0;
      }
      return goal;
    }
  }

  public final int teamId;

  public Player(int teamId) {
    this.teamId = teamId;
  }

  public void play(Scanner in) {
    // game loop
    int turn = 0;
    while (true) {
      Map map = readInput(in);
      logTurn(teamId, turn++, map);
      playActions(map);
    }
  }

  public Map readInput(Scanner in) {
    Map map = new Map();

    int entities = in.nextInt(); // number of entities still in game
    for (int i = 0; i < entities; i++) {
      int entityId = in.nextInt(); // entity identifier
      String entityType = in.next(); // "WIZARD", "OPPONENT_WIZARD" or
      // "SNAFFLE" (or "BLUDGER" after
      // first league)
      int x = in.nextInt(); // position
      int y = in.nextInt(); // position
      int vx = in.nextInt(); // velocity
      int vy = in.nextInt(); // velocity
      int state = in.nextInt(); // 1 if the wizard is holding a
      // Snaffle, 0 otherwise

      switch (entityType) {
        case "WIZARD":
          map.wizards.add(
              new Wizard(
                  entityId, new Player.Position(x, y), new Player.Velocity(vx, vy), state == 1));
          break;
        case "OPPONENT_WIZARD":
          map.opponentWizards.add(
              new Wizard(
                  entityId, new Player.Position(x, y), new Player.Velocity(vx, vy), state == 1));
          break;
        case "SNAFFLE":
          map.snaffles.add(
              new Snaffle(entityId, new Player.Position(x, y), new Player.Velocity(vx, vy)));
          break;
        case "BLUDGER":
          map.bludgers.add(
              new Bludger(entityId, new Player.Position(x, y), new Player.Velocity(vx, vy)));
          break;

        default:
          System.err.println(entityType);
          break;
      }
    }

    return map;
  }

  public void logTurn(int teamId, int turn, Map map) {
    System.err.println("Team id: " + teamId + ",Turn: " + turn);
    System.err.println("Wizards:");
    for (Wizard wizard : map.wizards) {
      System.err.println(wizard.toString());
    }
    System.err.println("Opponent Wizards:");
    for (Wizard opponentWizard : map.opponentWizards) {
      System.err.println(opponentWizard.toString());
    }
    System.err.println("Snaffles:");
    for (Snaffle snaffle : map.snaffles) {
      System.err.println(snaffle.toString());
    }
    System.err.println("Bludgers:");
    for (Bludger bludger : map.bludgers) {
      System.err.println(bludger.toString());
    }
  }

  class Action {}

  class MoveAction extends Action {
    public final Position position;
    public final int thrust;

    public MoveAction(Position position, int thrust) {
      this.position = position;
      this.thrust = thrust;
    }

    @Override
    public String toString() {
      return "MOVE " + position.x + " " + position.y + " " + thrust;
    }
  }

  class ThrowAction extends Action {
    public final Position position;
    public final int power;

    public ThrowAction(Position position, int power) {
      this.position = position;
      this.power = power;
    }

    @Override
    public String toString() {
      return "THROW " + position.x + " " + position.y + " " + power;
    }
  }

  class Strategy {
    public Strategy() {}

    public Snaffle computeClosestSnaffleForWizard(
        Wizard wizard, Map map, List<Snaffle> excludedSnaffles) {
      Snaffle closestSnaffle = null;
      int closestDistance = Integer.MAX_VALUE;
      for (Snaffle snaffle : map.snaffles) {
        if (!excludedSnaffles.contains(snaffle)) {
          int distance = wizard.position.computeDistance(snaffle.position);
          if (distance < closestDistance) {
            closestSnaffle = snaffle;
            closestDistance = distance;
          }
        }
      }
      if (closestSnaffle == null) {
        if (!excludedSnaffles.isEmpty()) {
          closestSnaffle = excludedSnaffles.get(0);
        } else {
          closestSnaffle = map.snaffles.get(0);
        }
      }
      return closestSnaffle;
    }

    public List<Action> computeActions(Map map) {
      List<Action> actions = new ArrayList<Player.Action>();

      Goal goal = map.getGoal(teamId);

      List<Snaffle> excludedSnaffles = new ArrayList<Player.Snaffle>();
      for (Wizard wizard : map.wizards) {
        if (wizard.holdingSnaffle) {
          actions.add(new ThrowAction(goal.getCenter(), 500));
        } else {
          Snaffle closestSnaffle = computeClosestSnaffleForWizard(wizard, map, excludedSnaffles);
          actions.add(new MoveAction(closestSnaffle.position, 150));
        }
      }
      return actions;
    }
  }

  public void playActions(Map map) {
    // Write an action using System.out.println()
    // To debug: System.err.println("Debug messages...");

    // Edit this line to indicate the action for each wizard (0 ≤
    // thrust ≤ 150, 0 ≤ power ≤ 500)
    // i.e.: "MOVE x y thrust" or "THROW x y power"

    Strategy strategy = new Strategy();
    List<Action> actions = strategy.computeActions(map);
    for (Action action : actions) {
      System.out.println(action.toString());
    }
  }

  public static void main(String args[]) {
    Scanner in = new Scanner(System.in);
    int myTeamId = in.nextInt(); // if 0 you need to score on the right of
    // the map, if 1 you need to score on
    // the left
    Player player = new Player(myTeamId);
    player.play(in);
  }
}
