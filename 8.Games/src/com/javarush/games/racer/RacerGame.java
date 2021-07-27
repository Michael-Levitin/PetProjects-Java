package com.javarush.games.racer;
import com.javarush.engine.cell.*;
import com.javarush.games.racer.road.RoadManager;


public class RacerGame extends Game
{
   public static final int WIDTH = 64;
   public static final int HEIGHT = 64;
   public static final int CENTER_X = WIDTH / 2;
   public static final int ROADSIDE_WIDTH = 14;

   private RoadMarking roadMarking;
   private PlayerCar player;
   private RoadManager roadManager;
   private boolean isGameStopped;
   private FinishLine finishLine;
   private ProgressBar progressBar;
   private int score;

   private static final int RACE_GOAL_CARS_COUNT = 40;


// К статическим переменным, как правило обращаются не по ссылке
//  на объект — orangeCar.km, а по имени класса — Car.km

   @Override
   public void initialize()
   {   showGrid(false);
      setScreenSize(WIDTH, HEIGHT);
      createGame();
      drawField();
   }

   @Override
   public void setCellColor(int x, int y, Color color)
   {   if (x < 0 || x >= WIDTH || y < 0 || y >= HEIGHT)
   {   return;
   }
      super.setCellColor(x, y, color);
   }

   @Override
   public void onTurn(int turn)
   {   if (roadManager.checkCrush(player))
   {   gameOver(); //
      drawScene();
      return;
   }

      if (roadManager.getPassedCarsCount() >= RACE_GOAL_CARS_COUNT )
      {   finishLine.show();
      }

      if ( finishLine.isCrossed(player))
      {   win();
         drawScene();
         return;
      }

      score -= 5;
      setScore(score);

      moveAll();
      roadManager.generateNewRoadObjects(this);
      drawScene();
   }

   @Override
   public void onKeyPress(Key key)
   {   if (key == Key.SPACE && isGameStopped)
   {   createGame();
   } else if (key == Key.RIGHT)
   {   player.setDirection(Direction.RIGHT);
   } else if (key == Key.LEFT)
   {   player.setDirection(Direction.LEFT);
   } else if (key == Key.UP)
   {   player.speed = 2; // simultaneously UP and R/L??
   }
   }

   @Override
   public void onKeyReleased(Key key)
   {   if (  (key == Key.RIGHT && player.getDirection() == Direction.RIGHT)
           || (key == Key.LEFT && player.getDirection() == Direction.LEFT)
   )
   {   player.setDirection(Direction.NONE);
   }

      if (key == Key.UP)
      {   player.speed = 1;
      }
   }

   private void createGame()
   {   roadMarking = new RoadMarking();
      player = new PlayerCar();
      roadManager = new RoadManager();
      isGameStopped = false;
      finishLine = new FinishLine();
      progressBar = new ProgressBar(RACE_GOAL_CARS_COUNT);
      drawScene();
      setTurnTimer(40);
      score = 3500;
   }

   private void drawField()
   {  for (int x = 0; x < WIDTH; x++)
      {  for (int y = 0; y < HEIGHT; y++)
         {  if (x == CENTER_X)
            {  setCellColor(x, y, Color.WHITE);
            } else if (x >= ROADSIDE_WIDTH && x < (WIDTH - ROADSIDE_WIDTH))
            {  setCellColor(x, y, Color.DIMGREY);
            } else
            {  setCellColor(x, y, Color.GREEN);
         }
      }
   }
   }

   private void drawScene()
   {   drawField();
      roadMarking.draw(this);
      player.draw(this);
      roadManager.draw(this);
      finishLine.draw(this);
      progressBar.draw(this);

   }

   private void moveAll()
   {  roadMarking.move(player.speed);
      roadManager.move(player.speed);
      finishLine.move(player.speed);
      progressBar.move(roadManager.getPassedCarsCount());
      player.move();
   }

   private void gameOver()
   {  isGameStopped = true;
      showMessageDialog(Color.NONE, "You've lost", Color.RED, 75);
      stopTurnTimer();
      player.stop();
   }

   private void win()
   {   isGameStopped = true;
      showMessageDialog(Color.NONE, "You won", Color.GREEN, 75);
      stopTurnTimer();
   }
}