// Player.

package com.citrix.conformative.server;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.PersistenceCapable;
import com.google.appengine.api.datastore.Key;


@PersistenceCapable
public class Player
{
   @PrimaryKey
   @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
   Key key;

   @Persistent
   private String name;

   @Persistent
   private String gameCode;

   @Persistent
   private double personalResources;

   @Persistent
   private double entitledResources;

   // Role.
   public static enum ROLE
   {
      STAKEHOLDER,
      CLAIMANT,
      AUDITOR
   };
   @Persistent
   private ROLE role;

   public Player(String name, String gameCode)
   {
      this.name         = name;
      this.gameCode     = gameCode;
      role              = ROLE.STAKEHOLDER;
      personalResources = 0.0;
      entitledResources = 0.0;
   }


   public String getName()
   {
      return(name);
   }


   public String getGameCode()
   {
      return(gameCode);
   }


   public double getPersonalResources()
   {
      return(personalResources);
   }


   public void setPersonalResources(double personalResources)
   {
      this.personalResources = personalResources;
   }


   public double getEntitledResources()
   {
      return(entitledResources);
   }


   public void setEntitledResources(double entitledResources)
   {
      this.entitledResources = entitledResources;
   }


   public ROLE getRole()
   {
      return(role);
   }


   public void setRole(ROLE role)
   {
      this.role = role;
   }
}
