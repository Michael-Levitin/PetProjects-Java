package com.javarush.games.racer.road;

import com.javarush.games.racer.RacerGame;
import com.javarush.games.racer.PlayerCar;
import com.javarush.engine.cell.Game;

import java.util.ArrayList;
import java.util.List;

public class RoadManager
{   public static final int LEFT_BORDER = RacerGame.ROADSIDE_WIDTH;
   public static final int RIGHT_BORDER = RacerGame.WIDTH - LEFT_BORDER;

   private static final int PLAYER_CAR_DISTANCE = 12;
   private static final int FIRST_LANE_POSITION = 16;
   private static final int FOURTH_LANE_POSITION = 44;
   private List<RoadObject> items = new ArrayList();
   private int passedCarsCount = 0;

   public int getPassedCarsCount()
   {   return passedCarsCount;
   }


   private RoadObject createRoadObject(RoadObjectType type, int x, int y)
   {  if (type == RoadObjectType.THORN)
      {   return new Thorn(x, y);
      } else if (type == RoadObjectType.DRUNK_CAR)
      {   return new MovingCar(x, y);
      }
      {   return new Car(type, x, y);
      }
   }

   private void addRoadObject(RoadObjectType roadObjectType, Game game)
   {   int x = game.getRandomNumber(FIRST_LANE_POSITION, FOURTH_LANE_POSITION);
      int y = -1 * RoadObject.getHeight(roadObjectType);

      RoadObject roadObject = createRoadObject(roadObjectType, x, y);
      if( isRoadSpaceFree(roadObject)) //
      {   items.add(roadObject);
      }
   }

   public void draw(Game game)
   {   for (RoadObject iteM : items) // for each item in list items
   {   iteM.draw(game);    // calls for RoadObject draw, couse iteM
   }                       // is of RoadObject type
   }

   public void move(int boost)
   {   for (RoadObject iteM : items)
   {   iteM.move(boost + iteM.speed, items);
   }
      deletePassedItems();
   }

   public boolean checkCrush(PlayerCar player)
   {   for (RoadObject item : items)
   {   if (item.isCollision(player))
   {   return true;
   }
   }
      return false;
   }

   private boolean isThornExists()
   {   for (RoadObject item : items)
   {   if (item instanceof Thorn)
   {   return true;
   }
   }
      return false;
   }

   private void generateThorn(Game game)
   {   if (game.getRandomNumber(100) < 10 && !isThornExists())
   {   addRoadObject(RoadObjectType.THORN, game);
   }
   }

   public void generateNewRoadObjects(Game game)
   {   generateThorn(game);
      generateRegularCar(game);
      generateMovingCar(game);
   }

   private void deletePassedItems() { // Creating new Arraylist
      for (RoadObject item : new ArrayList<>(items)) { // <--
         if (item.y >= RacerGame.HEIGHT) {
            if (! (item instanceof Thorn) )
            {   this.passedCarsCount++;
            }
            items.remove(item);
         } // Cannot iterate over list and remove at the same time;
      }
   }   // or use iterator below

//   Iterator<Integer> it = collection.iterator();
//    while (it.hasNext ())
//   {   Integer element = it.next();
//      System.out.println(element);
//      element.remove();
//   }

   private void generateRegularCar(Game game)
   {   int carTypeNumber = game.getRandomNumber(4);
      if (game.getRandomNumber(100) < 30)
      {   addRoadObject(RoadObjectType.values()[carTypeNumber], game);
      }   //
   }

   private boolean isRoadSpaceFree(RoadObject roadObject) // Take an object
   {   for (RoadObject item : items) // and check if it collides with distance
   {   if (item.isCollisionWithDistance(roadObject, PLAYER_CAR_DISTANCE))
   {   return false;       // between player and every other object
   }
   }
      return true;
   }

   private boolean isMovingCarExists()
   {  for (RoadObject item : items)
      {  if (item instanceof MovingCar)
         {   return true;
         }
   }
      return false;
   }

   private void generateMovingCar(Game game)
   {  if(game.getRandomNumber(100) < 10 && !isMovingCarExists())
      {   addRoadObject(RoadObjectType.DRUNK_CAR, game);
      }
   }

}