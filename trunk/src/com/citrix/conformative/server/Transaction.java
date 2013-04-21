// Conformative server transaction.

package com.citrix.conformative.server;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.PersistenceCapable;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class Transaction
{
   @PrimaryKey
   @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
   Key key;

   @Persistent
   private String gameCode;

   // State.
   public static final int PENDING    = 0;
   public static final int JOINING    = 1;
   public static final int RUNNING    = 2;
   public static final int COMPLETED  = 3;
   public static final int TERMINATED = 4;
   @Persistent
   private int state;

   public Transaction(String gameCode)
   {
      this.gameCode = gameCode;
      state         = PENDING;
   }


   public String getGameCode()
   {
      return(gameCode);
   }


   public int getState()
   {
      return(state);
   }


   public void setState(int state)
   {
      this.state = state;
   }
}
