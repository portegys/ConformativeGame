// Game service request.

package com.citrix.conformative.server;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.citrix.conformative.shared.Shared;
import com.citrix.conformative.shared.DelimitedString;
import com.google.appengine.api.channel.ChannelMessage;
import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import java.util.Collections;
import net.sf.jsr107cache.Cache;
import net.sf.jsr107cache.CacheException;
import net.sf.jsr107cache.CacheFactory;
import net.sf.jsr107cache.CacheManager;

@SuppressWarnings("serial")
public class GameService extends RemoteServiceServlet implements
com.citrix.conformative.client.GameService
{
   // Channel timeout.
   static final int CHANNEL_TIMEOUT_MINUTES = 240;

   public String requestService(String input) throws IllegalArgumentException
   {
      if (Shared.isVoid(input))
      {
         return(Shared.error("invalid input"));
      }
      String[] args = new DelimitedString(input).parse();
      if (args.length < 2)
      {
         return(Shared.error("invalid number of arguments"));
      }
      String request       = args[0];
      String gameCode      = args[1];
      String gameKey       = gameCode + DelimitedString.DELIMITER;
      String playerListKey = gameCode + DelimitedString.DELIMITER + Shared.ALL_PLAYERS;

      // Access storage and cache.
      Cache cache;
      try {
         CacheFactory cacheFactory = CacheManager.getInstance().getCacheFactory();
         cache = cacheFactory.createCache(Collections.emptyMap());
      }
      catch (CacheException e) {
         return(Shared.error("cannot access cache"));
      }
      PersistenceManager pm        = PMF.get().getPersistenceManager();
      Query              gameQuery = pm.newQuery(Game.class );
      gameQuery.setFilter("code == codeParam");
      gameQuery.declareParameters("String codeParam");
      Query playersQuery = pm.newQuery(Player.class );
      playersQuery.setFilter("gameCode == gameCodeParam");
      playersQuery.declareParameters("String gameCodeParam");
      Query playerQuery = pm.newQuery(Player.class );
      playerQuery.setFilter("gameCode == gameCodeParam && name == nameParam");
      playerQuery.declareParameters("String gameCodeParam, String nameParam");

      // Do requested action.
      try
      {
         // Game proxy cached?
         Game      persistentGame = null;
         GameProxy game           = (GameProxy)cache.get(gameKey);
         if (game == null)
         {
            // Get persistent game.
            @SuppressWarnings("unchecked")
            List<Game> games = (List<Game> )gameQuery.execute(gameCode);
            if (games.size() == 1)
            {
               persistentGame = games.get(0);
               game           = new GameProxy(persistentGame);
               cache.put(gameKey, game);
            }
            else if (games.size() > 1)
            {
               return(Shared.error("duplicate game codes"));
            }
         }

         // Get the players?
         ArrayList<PlayerProxy> players = new ArrayList<PlayerProxy>();
         if (game != null)
         {
            if (game.getState() == Game.RUNNING)
            {
               // List of players cached?
               players = (ArrayList<PlayerProxy> )cache.get(playerListKey);
               if (players == null)
               {
                  // Cache players and list.
                  players = new ArrayList<PlayerProxy>();
                  @SuppressWarnings("unchecked")
                  List<Player> playersWork = (List<Player> )playersQuery.execute(gameCode);
                  for (int i = 0; i < playersWork.size(); i++)
                  {
                     PlayerProxy player = new PlayerProxy(playersWork.get(i));
                     players.add(player);
                  }
                  cache.put(playerListKey, players);
               }
            }
            else
            {
               @SuppressWarnings("unchecked")
               List<Player> playersWork = (List<Player> )playersQuery.execute(gameCode);
               for (int i = 0; i < playersWork.size(); i++)
               {
                  players.add(new PlayerProxy(playersWork.get(i)));
               }
            }
         }
         ChannelService channelService =
            ChannelServiceFactory.getChannelService();

         if ((request.equals(Shared.JOIN_GAME) ||
              request.equals(Shared.QUIT_GAME) ||
              request.equals(Shared.HOST_CHAT)) &&
             (args.length >= 3))
         {
            if (game == null)
            {
               return(Shared.error("game not found"));
            }
            String      playerName = args[2];
            PlayerProxy player     = null;
            for (int i = 0; i < players.size(); i++)
            {
               player = players.get(i);
               if (player.getName().equals(playerName) &&
                   player.getGameCode().equals(gameCode)) { break; }
               player = null;
            }
            if (request.equals(Shared.JOIN_GAME) && (args.length == 3))
            {
               // Join game?
               if (player == null)
               {
                  if (game.getState() == Game.JOINING)
                  {
                     Player persistentPlayer = new Player(playerName, gameCode);
                     pm.makePersistent(persistentPlayer);
                     DelimitedString clientId = new DelimitedString(gameCode);
                     channelService.sendMessage(new ChannelMessage(clientId.toString(), input));
                     double commonResources = game.getCommonResources() / (double)(players.size() + 1);
                     for (int i = 0; i < players.size(); i++)
                     {
                        player   = players.get(i);
                        clientId = new DelimitedString();
                        clientId.add(gameCode);
                        clientId.add(player.getName());
                        DelimitedString message = new DelimitedString(Shared.SET_PLAYER_RESOURCES);
                        message.add(player.getPersonalResources());
                        message.add(commonResources);
                        message.add(player.getEntitledResources());
                        channelService.sendMessage(
                           new ChannelMessage(clientId.toString(), message.toString()));
                     }
                     clientId = new DelimitedString();
                     clientId.add(gameCode);
                     clientId.add(playerName);
                     DelimitedString response =
                        new DelimitedString(
                           channelService.createChannel(clientId.toString(), CHANNEL_TIMEOUT_MINUTES));
                     response.add("0");
                     response.add(commonResources);
                     response.add("0");
                     return(response.toString());
                  }
                  else
                  {
                     return(Shared.error("game is not accepting players"));
                  }
               }
               else
               {
                  return(Shared.error("duplicate name"));
               }
            }
            else if (request.equals(Shared.QUIT_GAME) && (args.length == 3))
            {
               // Quit game.
               if (player != null)
               {
                  if (game.getState() != Game.RUNNING)
                  {
                     playerQuery.deletePersistentAll(gameCode, playerName);
                     DelimitedString clientId = new DelimitedString(gameCode);
                     channelService.sendMessage(new ChannelMessage(clientId.toString(), input));
                     players.remove(player);
                     if (players.size() > 0)
                     {
                        double commonResources = game.getCommonResources() / (double)(players.size());
                        for (int i = 0; i < players.size(); i++)
                        {
                           player   = players.get(i);
                           clientId = new DelimitedString();
                           clientId.add(gameCode);
                           clientId.add(player.getName());
                           DelimitedString message = new DelimitedString(Shared.SET_PLAYER_RESOURCES);
                           message.add(player.getPersonalResources());
                           message.add(commonResources);
                           message.add(player.getEntitledResources());
                           channelService.sendMessage(
                              new ChannelMessage(clientId.toString(), message.toString()));
                        }
                     }
                  }
                  else
                  {
                     return(Shared.error("cannot quit running game"));
                  }
               }
               return(Shared.OK);
            }
            else if (request.equals(Shared.HOST_CHAT) && (args.length == 4))
            {
               // Host chat message.
               if (player != null)
               {
                  String clientId = gameCode;
                  channelService.sendMessage(new ChannelMessage(clientId, input));
                  return(Shared.OK);
               }
            }
         }
         else
         {
            if (request.equals(Shared.CREATE_GAME) && (args.length == 3))
            {
               // Create game?
               if (game == null)
               {
                  double resources = 0.0;
                  try {
                     resources = Double.parseDouble(args[2]);
                  }
                  catch (NumberFormatException e) {
                     return(Shared.error("resources must be a non-negative number"));
                  }
                  if (resources < 0.0)
                  {
                     return(Shared.error("resources must be a non-negative number"));
                  }
                  persistentGame = new Game(gameCode, resources);
                  pm.makePersistent(persistentGame);
                  game = new GameProxy(persistentGame);
                  cache.put(gameKey, game);
                  String clientId = gameCode;
                  return(channelService.createChannel(clientId, CHANNEL_TIMEOUT_MINUTES));
               }
               else
               {
                  return(Shared.error("duplicate game code"));
               }
            }
            else if (request.equals(Shared.DELETE_GAME) && (args.length == 2))
            {
               // Delete game.
               if (game != null)
               {
                  gameQuery.deletePersistentAll(gameCode);
                  playersQuery.deletePersistentAll(gameCode);
                  cache.remove(gameKey);
                  cache.remove(playerListKey);
                  for (int i = 0; i < players.size(); i++)
                  {
                     PlayerProxy     player     = players.get(i);
                     String          playerName = player.getName();
                     DelimitedString clientId   = new DelimitedString();
                     clientId.add(gameCode);
                     clientId.add(playerName);
                     DelimitedString quitRequest = new DelimitedString(Shared.QUIT_GAME);
                     quitRequest.add("Game deleted by host");
                     channelService.sendMessage(
                        new ChannelMessage(clientId.toString(), quitRequest.toString()));
                  }
               }
               return(Shared.OK);
            }
            else if (request.equals(Shared.UPDATE_GAME) && (args.length == 3))
            {
               // Update game.
               if (game != null)
               {
                  int state = Integer.parseInt(args[2]);
                  game.setState(state);
                  if (persistentGame != null)
                  {
                     persistentGame.setState(state);
                  }
                  else
                  {
                     @SuppressWarnings("unchecked")
                     List<Game> games = (List<Game> )gameQuery.execute(gameCode);
                     if (games.size() == 0)
                     {
                        persistentGame = new Game(gameCode, game.getInitialCommonResources());
                        persistentGame.setCommonResources(game.getCommonResources());
                        persistentGame.setState(state);
                     }
                     else if (games.size() == 1)
                     {
                        persistentGame = games.get(0);
                        persistentGame.setState(state);
                     }
                     else
                     {
                        cache.remove(gameKey);
                        return(Shared.error("duplicate game code"));
                     }
                  }
                  pm.makePersistent(persistentGame);
                  cache.put(gameKey, game);
                  return(Shared.OK);
               }
               else
               {
                  return(Shared.error("game not found"));
               }
            }
            else if (request.equals(Shared.REMOVE_PLAYER) && (args.length == 3))
            {
               // Remove player.
               if (game != null)
               {
                  if (game.getState() != Game.RUNNING)
                  {
                     String                 playerName  = args[2];
                     ArrayList<String>      playerNames = new ArrayList<String>();
                     ArrayList<PlayerProxy> playersTemp = new ArrayList<PlayerProxy>();
                     for (int i = 0; i < players.size(); i++)
                     {
                        PlayerProxy player = players.get(i);
                        if (playerName.equals(Shared.ALL_PLAYERS) ||
                            playerName.equals(player.getName()))
                        {
                           String name = player.getName();
                           playerNames.add(name);
                           playerQuery.deletePersistentAll(gameCode, name);
                        }
                        else
                        {
                           playersTemp.add(player);
                        }
                     }
                     for (int i = 0; i < playerNames.size(); i++)
                     {
                        String          name     = playerNames.get(i);
                        DelimitedString clientId = new DelimitedString();
                        clientId.add(gameCode);
                        clientId.add(name);
                        DelimitedString quitRequest = new DelimitedString(Shared.QUIT_GAME);
                        quitRequest.add("Player removed by host");
                        channelService.sendMessage(
                           new ChannelMessage(clientId.toString(), quitRequest.toString()));
                     }
                     if ((players.size() > playersTemp.size()) && (playersTemp.size() > 0))
                     {
                        double commonResources = game.getCommonResources() / (double)(playersTemp.size());
                        for (int i = 0; i < playersTemp.size(); i++)
                        {
                           PlayerProxy     player   = playersTemp.get(i);
                           DelimitedString clientId = new DelimitedString();
                           clientId.add(gameCode);
                           clientId.add(player.getName());
                           DelimitedString message = new DelimitedString(Shared.SET_PLAYER_RESOURCES);
                           message.add(player.getPersonalResources());
                           message.add(commonResources);
                           message.add(player.getEntitledResources());
                           channelService.sendMessage(
                              new ChannelMessage(clientId.toString(), message.toString()));
                        }
                     }
                     return(Shared.OK);
                  }
                  else
                  {
                     return(Shared.error("cannot remove player from running game"));
                  }
               }
               else
               {
                  return(Shared.error("game not found"));
               }
            }
            else if (request.equals(Shared.GET_PLAYER_RESOURCES) &&
                     (args.length == 3))
            {
               // Get player resources.
               if (game != null)
               {
                  double commonResources = game.getCommonResources();
                  String playerName      = args[2];
                  if (playerName.equals(Shared.ALL_PLAYERS))
                  {
                     double totalPersonalResources = 0.0;
                     double totalEntitledResources = 0.0;
                     for (int i = 0; i < players.size(); i++)
                     {
                        PlayerProxy player = players.get(i);
                        totalPersonalResources += player.getPersonalResources();
                        totalEntitledResources += player.getEntitledResources();
                     }
                     DelimitedString response = new DelimitedString();
                     response.add(totalPersonalResources);
                     response.add(commonResources);
                     response.add(totalEntitledResources);
                     return(response.toString());
                  }
                  else
                  {
                     for (int i = 0; i < players.size(); i++)
                     {
                        PlayerProxy player = players.get(i);
                        if (player.getName().equals(playerName))
                        {
                           DelimitedString response = new DelimitedString();
                           response.add(player.getPersonalResources());
                           response.add(commonResources / players.size());
                           response.add(player.getEntitledResources());
                           return(response.toString());
                        }
                     }
                     return(Shared.error("player not found"));
                  }
               }
               else
               {
                  return(Shared.error("game not found"));
               }
            }
            else if (request.equals(Shared.PLAYER_CHAT) &&
                     ((args.length == 3) || (args.length == 4)))
            {
               // Player chat message.
               if (game != null)
               {
                  String playerName = null;
                  if (args.length == 4)
                  {
                     playerName = args[2];
                  }
                  for (int i = 0; i < players.size(); i++)
                  {
                     PlayerProxy player = players.get(i);
                     if ((playerName == null) || player.getName().equals(playerName))
                     {
                        DelimitedString clientId = new DelimitedString();
                        clientId.add(gameCode);
                        clientId.add(player.getName());
                        channelService.sendMessage(new ChannelMessage(clientId.toString(), input));
                     }
                  }
               }
               return(Shared.OK);
            }
         }
      }
      finally
      {
         pm.close();
      }
      return(Shared.error("unknown input: " + input));
   }
}
