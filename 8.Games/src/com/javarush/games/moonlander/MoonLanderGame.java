package com.javarush.games.moonlander;
import com.javarush.engine.cell.*;

public class MoonLanderGame extends Game
{   public static final int WIDTH = 64;
   public static final int HEIGHT = 64;
   private boolean isUpPressed;
   private boolean isLeftPressed;
   private boolean isRightPressed;
   private GameObject platform;
   private boolean isGameStopped;
   private int score;


   private GameObject landscape;
   private Rocket rocket;

   @Override
   public void initialize()
   {   setScreenSize(WIDTH, HEIGHT);
      createGame();
      showGrid(false);
   }

   @Override
   public void onTurn(int x)
   {   rocket.move(isUpPressed, isLeftPressed, isRightPressed);
      check();
      drawScene();
      if (score > 0)
      {   score--;
      }
      setScore(score);
   }

   @Override
   public void setCellColor(int x, int y, Color color)
   {   if (x>=WIDTH || x<0 || y>=HEIGHT || y<0)
   {   return;
   }
      super.setCellColor(x, y, color); // Calls for constructor of super class
   }

   @Override
   public void onKeyPress(Key key)
   {   if (isGameStopped && key == Key.SPACE )
   {   createGame();
      return;
   }

      switch (key)
      {  case LEFT : isLeftPressed = true; isRightPressed = false; break;
         case RIGHT: isLeftPressed = false; isRightPressed = true; break;
         case UP:      isUpPressed = true; break;
      }
   }

   @Override
   public void onKeyReleased(Key key)
   {   switch (key)
      {  case UP :      isUpPressed = false; break;
         case RIGHT: isRightPressed = false; break;
         case LEFT:   isLeftPressed = false; break;
      }
   }

   private void createGame()
   {   createGameObjects();
      drawScene();
      setTurnTimer(50);
      isUpPressed = false;
      isRightPressed = false;
      isLeftPressed = false;
      isGameStopped = false;
      score = 1000;
   }

   private void drawScene()
   {   for (int i = 0; i< WIDTH; i++)
   {   for (int j = 0; j< WIDTH; j++)
   {   setCellColor(i, j, Color.BLACK);
   }
   }
      rocket.draw(this);
      landscape.draw(this);
   }

   private void createGameObjects()
   {  rocket = new Rocket(WIDTH/2, 0);
      landscape  = new GameObject(0, 25, ShapeMatrix.LANDSCAPE);
      platform = new GameObject (23, MoonLanderGame.HEIGHT - 1, ShapeMatrix.PLATFORM);
   }

   private void check()
   {   if (   rocket.isCollision (landscape)
           && !(rocket.isCollision (platform) && rocket.isStopped()))
   {   gameOver();
   }   else if (rocket.isCollision (platform) && rocket.isStopped())
   {   win();
   }
   }

   private void win()
   {  rocket.land();
      isGameStopped = true;
      showMessageDialog(Color.NONE, "You've won", Color.GREEN, 75);
      stopTurnTimer();
   }

   private void gameOver()
   {  rocket.crash();
      isGameStopped = true;
      showMessageDialog(Color.NONE, "You've lost", Color.RED, 75);
      stopTurnTimer();
      score = 0;
   }
}