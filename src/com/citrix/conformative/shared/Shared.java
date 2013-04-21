// Shared server and client.

package com.citrix.conformative.shared;

public class Shared
{
   // Messages.
   // Formats: <operation>/<game code>/<args> | <operation>/<game code>/<player name>/<args>
   public static final String CREATE_GAME          = "create_game";
   public static final String UPDATE_GAME          = "update_game";
   public static final String DELETE_GAME          = "delete_game";
   public static final String JOIN_GAME            = "join_game";
   public static final String QUIT_GAME            = "quit_game";
   public static final String REMOVE_PLAYER        = "remove_player";
   public static final String GET_PLAYER_RESOURCES = "get_player_resources";
   public static final String SET_PLAYER_RESOURCES = "set_player_resources";
   public static final String HOST_CHAT            = "host_chat";
   public static final String PLAYER_CHAT          = "player_chat";

   // All players symbol.
   public static final String ALL_PLAYERS = "<all>";

   // OK status.
   public static final String OK = "OK";

   // Error prefix.
   public static final String ERROR_PREFIX = "Error: ";


   public static boolean isVoid(String str)
   {
      return(str == null || "".equals(str.trim()));
   }


   public static boolean isOK(String str)
   {
      if (!isVoid(str))
      {
         if (str.equals(OK)) { return(true); }
      }
      return(false);
   }


   public static boolean isError(String str)
   {
      if (!isVoid(str))
      {
         if (str.startsWith(ERROR_PREFIX)) { return(true); }
      }
      return(false);
   }


   public static String error(String str)
   {
      return(ERROR_PREFIX + str);
   }
}
