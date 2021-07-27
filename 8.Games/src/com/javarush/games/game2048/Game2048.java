package com.javarush.games.game2048;
import com.javarush.engine.cell.*;

public class Game2048 extends Game
{
   private static final int SIDE = 4;
   private int[][] gameField = new int[SIDE][SIDE];
   private boolean isGameStopped = false;

   @Override
   public void initialize()
   {   setScreenSize(SIDE, SIDE);
      createGame();
      drawScene();
   }

   @Override
   public void onKeyPress(Key key)
   {   if (isGameStopped && key == Key.SPACE)
   {   createGame();
      isGameStopped = false;
      drawScene();
      return;
   }

      if (!canUserMove())
      {   gameOver();
         return;
      }

      if (key == Key.UP) {
         moveUp();
      } else if (key == Key.RIGHT) {
         moveRight();
      } else if (key == Key.DOWN) {
         moveDown();
      } else if (key == Key.LEFT) {
         moveLeft();
      } else {
         return;
      }
      drawScene();
   }

   private void createGame()
   {  if (isGameStopped)
      {   gameField = new int[SIDE][SIDE];
      }
      createNewNumber();
      createNewNumber();
   }

   private void drawScene()
   {  for (int i = 0; i<SIDE; i++)
      {  for (int j = 0; j<SIDE; j++)
         {  setCellColoredNumber(i, j, gameField[j][i]);
         }
      }
   }


   private void createNewNumber()
   {  int x;
      int y;
      do
      {   x = getRandomNumber(SIDE);
         y = getRandomNumber(SIDE);
      } while (gameField[x][y] != 0);

      gameField[x][y] = (getRandomNumber(10) == 9) ? 4 : 2;

      if (getMaxTileValue() == 2048)
      {   win();
      }
   }

   private Color getColorByValue(int value)
   {  switch (value)
      {  case 0 : return Color.WHITE;
         case 2 : return Color.PLUM;
         case 4 : return Color.SLATEBLUE;
         case 8 : return Color.DODGERBLUE;
         case 16 : return Color.DARKTURQUOISE;
         case 32 : return Color.MEDIUMSEAGREEN;
         case 64 : return Color.LIMEGREEN;
         case 128 : return Color.DARKORANGE;
         case 256 : return Color.SALMON;
         case 512 : return Color.ORANGERED;
         case 1024 : return Color.DEEPPINK;
         case 2048 : return Color.MEDIUMVIOLETRED;
         default   : return Color.NONE;
      }
   }

   private void setCellColoredNumber(int x, int y, int value)
   {  Color color = getColorByValue(value);
      if(value == 0)
      {  setCellValueEx(x, y, color, "");
         return;
      }
      setCellValueEx(x, y, color, Integer.toString(value));
   }

   private boolean compressRow(int[] row)
   {  int insertPosition = 0;
      boolean result = false;
      for (int x = 0; x < SIDE; x++) {
         if (row[x] > 0) {
            if (x != insertPosition) {
               row[insertPosition] = row[x];
               row[x] = 0;
               result = true;
            }
            insertPosition++;
         }
      }
      return result;
   }

   private boolean mergeRow(int[] row)
   {  boolean isMerged = false;
      for (int x = 0; x < SIDE-1; x++)
      {
         if (row[x] > 0 && row[x] == row[x+1])
         {   row[x] = row[x]*2;
            row[x+1] = 0;
            isMerged = true;
            x++;
         }
      }
      return isMerged;
   }

   private void moveLeft()
   {  boolean isMoved = false;
      for (int x = 0; x < SIDE; x++)
      {   if (compressRow(gameField[x]))
      {   isMoved = true;
      }
         if (mergeRow(gameField[x]))
         {   isMoved = true;
         }
         if (compressRow(gameField[x]))
         {   isMoved = true;
         }
      }
      if (isMoved)
      {   createNewNumber();
      }
   }

   private void moveRight()
   {  rotateClockwise();
      rotateClockwise();
      moveLeft();
      rotateClockwise();
      rotateClockwise();
   }

   private void moveUp()
   {  rotateClockwise();
      rotateClockwise();
      rotateClockwise();
      moveLeft();
      rotateClockwise();
   }

   private void moveDown()
   {  rotateClockwise();
      moveLeft();
      rotateClockwise();
      rotateClockwise();
      rotateClockwise();
   }

   private void rotateClockwise()
   {   int[][] tempArray = new int[SIDE][SIDE];

      for (int x = 0; x < SIDE; x++)
      {  for (int y = 0; y < SIDE; y++)
         {  tempArray[y][SIDE - 1 - x] = gameField[x][y];
         }
      }
      gameField = tempArray;
   }

   private int getMaxTileValue()
   {  int max = 0;
      for (int x = 0; x < SIDE; x++)
      {  for (int y = 0; y < SIDE; y++)
         {  if (gameField[x][y] > max)
            {  max = gameField[x][y];
            }
         }
      }
      return max;
   }

   private void win ()
   {  isGameStopped = true;
      showMessageDialog(Color.WHITE, "Congrats man", Color.GREEN, 75);
   }

   private boolean canUserMove()
   {  for (int x = 0; x < SIDE; x++)
      {  for (int y = 0; y < SIDE; y++)
         {  if (gameField[x][y] == 0)
            {   return true;
            }
            if (    (x-1>0 && gameField[x][y] == gameField[x-1][y])
              ||  (y-1>0 && gameField[x][y] == gameField[x][y-1])
              ||  (x+1<SIDE && gameField[x][y] == gameField[x+1][y])
              ||  (y+1<SIDE && gameField[x][y] == gameField[x][y+1])
            )
            {  return true;
            }
         }
      }
      return false;
   }

   private void gameOver()
   {  isGameStopped = true;
      showMessageDialog(Color.WHITE, "Better luck next time", Color.RED, 75);
   }
}