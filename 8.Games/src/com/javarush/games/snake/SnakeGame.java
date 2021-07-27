package com.javarush.games.snake;
import com.javarush.engine.cell.*;

public class SnakeGame extends Game{
   public static final int WIDTH = 15;
   public static final int HEIGHT = 15;
   private static final int GOAL = 28;

   private Snake snake;
   private Apple apple;
   private int turnDelay;
   private int score;
   private boolean isGameStopped;

   @Override
   public void initialize(){
      setScreenSize(WIDTH, HEIGHT);
      createGame();
   }
   private void createGame(){
      turnDelay = 300;
      setTurnTimer(turnDelay);
      snake = new Snake(WIDTH/2, HEIGHT/2);
      createNewApple();
      isGameStopped = false;
      drawScene();
      score = 0;
      setScore(score);
   }

   private void drawScene(){
      for (int i = 0; i < WIDTH; i++) {
         for (int j = 0; j < HEIGHT; j++) {
            setCellValueEx(i, j, Color.DARKSEAGREEN, "");
         }
      }
      snake.draw(this);
      apple.draw(this);
   }

   @Override
   public void onTurn(int x)
   {   snake.move(apple);
      if (!apple.isAlive)
      {   createNewApple();
         score +=5;
         setScore(score);
         turnDelay -=10;
         setTurnTimer(turnDelay);
      }

      if (!snake.isAlive)
      {   gameOver();
      }

      if (snake.getLength()>GOAL)
      {   win();
      }
      drawScene();
   }

   @Override
   public void onKeyPress(Key key){
      if (key == Key.LEFT){
         snake.setDirection(Direction.LEFT);
      } else if (key == Key.RIGHT){
         snake.setDirection(Direction.RIGHT);
      } else if (key == Key.UP){
         snake.setDirection(Direction.UP);
      } else if (key == Key.DOWN){
         snake.setDirection(Direction.DOWN);
      }
      if (key == Key.SPACE && isGameStopped)
      {   createGame();
      }
   }

   private void createNewApple()
   {   do
      {  int x = getRandomNumber(WIDTH);
         int y = getRandomNumber(HEIGHT);
         apple = new Apple(x,y);
      } while (snake.checkCollision(apple));
   }

   private void gameOver()
   {   stopTurnTimer();
      isGameStopped = true;
      showMessageDialog(Color.WHITE, "GAME OVER", Color.RED, 75);
   }

   private void win()
   {   stopTurnTimer();
      isGameStopped = true;
      showMessageDialog(Color.WHITE, "WIN", Color.GREEN, 75);
   }
}
