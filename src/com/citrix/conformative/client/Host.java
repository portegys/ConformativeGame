// Conformative game host client.

package com.citrix.conformative.client;

import com.citrix.conformative.server.Game;
import com.citrix.conformative.shared.Shared;
import com.citrix.conformative.shared.DelimitedString;
import com.google.gwt.appengine.channel.client.Channel;
import com.google.gwt.appengine.channel.client.ChannelError;
import com.google.gwt.appengine.channel.client.ChannelFactory;
import com.google.gwt.appengine.channel.client.ChannelFactoryImpl;
import com.google.gwt.appengine.channel.client.Socket;
import com.google.gwt.appengine.channel.client.SocketListener;
import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.ListBox;

public class Host
{
   private ButtonHandler      buttonHandler;
   private ListBoxHandler     comboBoxHandler;
   private RootPanel          rootPanel;
   private Label              conformativeGameLabel;
   private TabPanel           roleTabPanel;
   private VerticalPanel      homePanel;
   private FlexTable          homeFlexTable;
   private Label              gameCodeLabel;
   private TextBox            gameCodeTextBox;
   private Label              gameResourcesLabel;
   private TextBox            gameResourcesTextBox;
   private Button             gameCreateDeleteButton;
   private Label              gameStateLabel;
   private ListBox            gameStateListBox;
   private CaptionPanel       playersCaptionPanel;
   private VerticalPanel      playersPanel;
   private HorizontalPanel    playersJoinedPanel;
   private Label              playersJoinedLabel;
   private TextBox            playersJoinedTextBox;
   private ListBox            playersListBox;
   private Button             playerRemoveButton;
   private CaptionPanel       playerResourceCaptionPanel;
   private HorizontalPanel    playerResourcePanel;
   private Label              playerTotalResourceLabel;
   private TextBox            playerTotalResourceTextBox;
   private Label              playerPersonalResourceLabel;
   private TextBox            playerPersonalResourceTextBox;
   private Label              playerCommonResourceLabel;
   private TextBox            playerCommonResourceTextBox;
   private CaptionPanel       playerChatCaptionPanel;
   private VerticalPanel      playerChatPanel;
   private TextArea           playerChatTextArea;
   private Button             playerChatClearButton;
   private TextBox            playerChatTextBox;
   private Button             playerChatSendButton;
   private CaptionPanel       transactionHistoryCaptionPanel;
   private TextArea           transactionHistoryTextArea;
   private VerticalPanel      transactionPanel;
   private CaptionPanel       transactionParticipantsCaptionPanel;
   private VerticalPanel      transactionParticipantsPanel;
   private CaptionPanel       transactionParticipantsClaimantCaptionPanel;
   private HorizontalPanel    transactionParticipantsClaimantPanel;
   private ListBox            transactionParticipantsClaimantCandidateListBox;
   private Label              transactionParticipantsClaimantLabel;
   private TextBox            transactionParticipantsClaimantTextBox;
   private CaptionPanel       transactionParticipantsAuditorCaptionPanel;
   private HorizontalPanel    transactionParticipantsAuditorPanel;
   private ListBox            transactionParticipantsAuditorCandidateListBox;
   private Label              transactionParticipantsAuditorLabel;
   private ListBox            transactionParticipantsAuditorListBox;
   private Button             transactionParticipantsSetButton;
   private CaptionPanel       transactionClaimCaptionPanel;
   private VerticalPanel      transactionClaimPanel;
   private CaptionPanel       transactionClaimDistributionCaptionPanel;
   private VerticalPanel      transactionClaimDistributionPanel;
   private Canvas             transactionClaimDistributionCanvas;
   private NormalDistribution transactionClaimDistribution;
   private FlexTable          transactionClaimDistributionParameterFlexTable;
   private Label              transactionClaimDistributionMeanLabel;
   private TextBox            transactionClaimDistributionMeanTextBox;
   private Label              transactionClaimDistributionSigmaLabel;
   private TextBox            transactionClaimDistributionSigmaTextBox;
   private Button             transactionClaimDistributionParameterSetButton;
   private Label              transactionClaimDistributionTestValueLabel;
   private TextBox            transactionClaimDistributionTestValueTextBox;
   private Button             transactionClaimDistributionTestButton;
   private TextBox            transactionClaimDistributionTestProbabilityTextBox;
   private FlexTable          transactionClaimFlexTable;
   private Label              transactionClaimEntitlementLabel;
   private TextBox            transactionClaimEntitlementTextBox;
   private Button             transactionClaimEntitlementGenerateButton;
   private Button             transactionClaimEntitlementSetButton;
   private Label              transactionClaimAmountLabel;
   private TextBox            transactionClaimAmountTextBox;
   private CaptionPanel       transactionAuditCaptionPanel;
   private VerticalPanel      transactionAuditPanel;
   private CaptionPanel       transactionCompletionCaptionPanel;
   private VerticalPanel      transactionCompletionPanel;

   private static final int HOME_TAB        = 0;
   private static final int TRANSACTION_TAB = 1;

   // Remote service proxy.
   private GameServiceAsync gameService;

   // Channel and socket.
   private Channel channel;
   private Socket  channelSocket;

   // Game code.
   private String gameCode;

   // Resources.
   private double resources;

   // Player removal.
   private String removePlayer;

   // Timer.
   private final int timerInterval_ms = 500;
   private Timer     timer;

   // Constructor.
   public Host(GameServiceAsync gameService, RootPanel rootPanel)
   {
      // Server request.
      this.gameService = gameService;

      // Handlers.
      buttonHandler   = new ButtonHandler();
      comboBoxHandler = new ListBoxHandler();

      // Root panel.
      this.rootPanel        = rootPanel;
      conformativeGameLabel = new Label("Conformative Game Host");
      conformativeGameLabel.setStyleName("gwt-Label-Title");
      conformativeGameLabel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
      conformativeGameLabel.setSize("450px", "31px");
      this.rootPanel.add(conformativeGameLabel, 0, 0);

      // Role tabs.
      roleTabPanel = new TabPanel();

      // Home tab.
      homePanel = new VerticalPanel();
      roleTabPanel.add(homePanel, "Home");

      homeFlexTable = new FlexTable();
      homePanel.add(homeFlexTable);
      gameCodeLabel = new Label("Code:");
      homeFlexTable.setWidget(0, 0, gameCodeLabel);
      gameCodeTextBox = new TextBox();
      homeFlexTable.setWidget(0, 1, gameCodeTextBox);
      gameCodeTextBox.setWidth("120px");
      gameResourcesLabel = new Label("Resources:");
      homeFlexTable.setWidget(0, 2, gameResourcesLabel);
      gameResourcesTextBox = new TextBox();
      homeFlexTable.setWidget(0, 3, gameResourcesTextBox);
      gameResourcesTextBox.setWidth("60px");
      gameCreateDeleteButton = new Button("Create");
      homeFlexTable.setWidget(0, 4, gameCreateDeleteButton);
      gameCreateDeleteButton.addClickHandler(buttonHandler);
      gameStateLabel = new Label("State:");
      homeFlexTable.setWidget(1, 0, gameStateLabel);
      gameStateListBox = new ListBox(false);
      homeFlexTable.setWidget(1, 1, gameStateListBox);
      gameStateListBox.setEnabled(false);
      gameStateListBox.addChangeHandler(comboBoxHandler);
      gameStateListBox.insertItem("Pending", Game.PENDING);
      gameStateListBox.insertItem("Joining", Game.JOINING);
      gameStateListBox.insertItem("Running", Game.RUNNING);
      gameStateListBox.insertItem("Completed", Game.COMPLETED);
      gameStateListBox.insertItem("Terminated", Game.TERMINATED);
      gameStateListBox.setSelectedIndex(Game.PENDING);
      playersCaptionPanel = new CaptionPanel("Players");
      homePanel.add(playersCaptionPanel);
      playersCaptionPanel.setWidth("450px");
      playersPanel = new VerticalPanel();
      playersCaptionPanel.add(playersPanel);
      playersPanel.setWidth("440px");
      playersJoinedPanel = new HorizontalPanel();
      playersPanel.add(playersJoinedPanel);
      playersJoinedPanel.setWidth("235px");
      playersJoinedLabel = new Label("Joined:");
      playersJoinedPanel.add(playersJoinedLabel);
      playersJoinedPanel.setCellVerticalAlignment(playersJoinedLabel, HasVerticalAlignment.ALIGN_MIDDLE);
      playersJoinedLabel.setWidth("50px");
      playersJoinedTextBox = new TextBox();
      playersJoinedTextBox.setEnabled(false);
      playersJoinedTextBox.setText("0");
      playersJoinedPanel.add(playersJoinedTextBox);
      playersJoinedTextBox.setWidth("40px");
      playersListBox = new ListBox(false);
      playersJoinedPanel.add(playersListBox);
      playersListBox.addChangeHandler(comboBoxHandler);
      playersListBox.insertItem(Shared.ALL_PLAYERS, 0);
      playerRemoveButton = new Button("Remove");
      playersJoinedPanel.add(playerRemoveButton);
      playersJoinedPanel.setCellVerticalAlignment(playerRemoveButton, HasVerticalAlignment.ALIGN_MIDDLE);
      playerRemoveButton.addClickHandler(buttonHandler);
      playerResourceCaptionPanel = new CaptionPanel("Resources");
      playersPanel.add(playerResourceCaptionPanel);
      playerResourceCaptionPanel.setWidth("430px");
      playerResourcePanel = new HorizontalPanel();
      playerResourceCaptionPanel.add(playerResourcePanel);
      playerResourcePanel.setWidth("415px");
      playerTotalResourceLabel = new Label("Total:");
      playerResourcePanel.add(playerTotalResourceLabel);
      playerResourcePanel.setCellVerticalAlignment(playerTotalResourceLabel, HasVerticalAlignment.ALIGN_MIDDLE);
      playerTotalResourceLabel.setWidth("40px");
      playerTotalResourceTextBox = new TextBox();
      playerTotalResourceTextBox.setReadOnly(true);
      playerResourcePanel.add(playerTotalResourceTextBox);
      playerTotalResourceTextBox.setWidth("60px");
      playerPersonalResourceLabel = new Label(" = Personal:");
      playerResourcePanel.add(playerPersonalResourceLabel);
      playerResourcePanel.setCellVerticalAlignment(playerPersonalResourceLabel, HasVerticalAlignment.ALIGN_MIDDLE);
      playerPersonalResourceLabel.setWidth("70px");
      playerPersonalResourceTextBox = new TextBox();
      playerPersonalResourceTextBox.setReadOnly(true);
      playerResourcePanel.add(playerPersonalResourceTextBox);
      playerPersonalResourceTextBox.setWidth("60px");
      playerCommonResourceLabel = new Label(" + Common:");
      playerResourcePanel.add(playerCommonResourceLabel);
      playerResourcePanel.setCellVerticalAlignment(playerCommonResourceLabel, HasVerticalAlignment.ALIGN_MIDDLE);
      playerCommonResourceLabel.setWidth("70px");
      playerCommonResourceTextBox = new TextBox();
      playerCommonResourceTextBox.setReadOnly(true);
      playerResourcePanel.add(playerCommonResourceTextBox);
      playerCommonResourceTextBox.setWidth("60px");
      playerChatCaptionPanel = new CaptionPanel("Player chat");
      homePanel.add(playerChatCaptionPanel);
      playerChatCaptionPanel.setWidth("450px");
      playerChatPanel = new VerticalPanel();
      playerChatCaptionPanel.add(playerChatPanel);
      playerChatTextArea = new TextArea();
      playerChatTextArea.setReadOnly(true);
      playerChatTextArea.setText("Note: prepend <player name>/ to send to specific player");
      playerChatPanel.add(playerChatTextArea);
      homePanel.setCellVerticalAlignment(playerChatTextArea, HasVerticalAlignment.ALIGN_BOTTOM);
      playerChatTextArea.setSize("430px", "100px");
      playerChatClearButton = new Button("Clear");
      playerChatPanel.add(playerChatClearButton);
      playerChatClearButton.addClickHandler(buttonHandler);
      playerChatTextBox = new TextBox();
      playerChatPanel.add(playerChatTextBox);
      playerChatTextBox.setWidth("430px");
      playerChatSendButton = new Button("Send");
      playerChatPanel.add(playerChatSendButton);
      playerChatSendButton.addClickHandler(buttonHandler);
      transactionHistoryCaptionPanel = new CaptionPanel("Transaction history");
      homePanel.add(transactionHistoryCaptionPanel);
      transactionHistoryCaptionPanel.setWidth("450px");
      transactionHistoryTextArea = new TextArea();
      transactionHistoryTextArea.setReadOnly(true);
      transactionHistoryCaptionPanel.add(transactionHistoryTextArea);
      transactionHistoryTextArea.setSize("430px", "50px");

      // Transaction tab.
      transactionPanel = new VerticalPanel();
      roleTabPanel.add(transactionPanel, "Transaction");
      transactionParticipantsCaptionPanel = new CaptionPanel("1. Participants");
      transactionPanel.add(transactionParticipantsCaptionPanel);
      transactionParticipantsCaptionPanel.setWidth("450px");
      transactionParticipantsPanel = new VerticalPanel();
      transactionParticipantsCaptionPanel.add(transactionParticipantsPanel);
      transactionParticipantsClaimantCaptionPanel = new CaptionPanel("Claimant");
      transactionParticipantsPanel.add(transactionParticipantsClaimantCaptionPanel);
      transactionParticipantsClaimantCaptionPanel.setWidth("430px");
      transactionParticipantsClaimantPanel = new HorizontalPanel();
      transactionParticipantsClaimantCaptionPanel.add(transactionParticipantsClaimantPanel);
      transactionParticipantsClaimantCandidateListBox = new ListBox();
      transactionParticipantsClaimantPanel.add(transactionParticipantsClaimantCandidateListBox);
      transactionParticipantsClaimantCandidateListBox.setWidth("65px");
      transactionParticipantsClaimantLabel = new Label("select->");
      transactionParticipantsClaimantPanel.add(transactionParticipantsClaimantLabel);
      transactionParticipantsClaimantPanel.setCellVerticalAlignment(transactionParticipantsClaimantLabel, HasVerticalAlignment.ALIGN_MIDDLE);
      transactionParticipantsClaimantPanel.setCellHorizontalAlignment(transactionParticipantsClaimantLabel, HasHorizontalAlignment.ALIGN_CENTER);
      transactionParticipantsClaimantLabel.setWidth("60px");
      transactionParticipantsClaimantTextBox = new TextBox();
      transactionParticipantsClaimantPanel.add(transactionParticipantsClaimantTextBox);
      transactionParticipantsClaimantTextBox.setWidth("300px");
      transactionParticipantsAuditorCaptionPanel = new CaptionPanel("Auditors");
      transactionParticipantsPanel.add(transactionParticipantsAuditorCaptionPanel);
      transactionParticipantsAuditorCaptionPanel.setWidth("430px");
      transactionParticipantsAuditorPanel = new HorizontalPanel();
      transactionParticipantsAuditorCaptionPanel.add(transactionParticipantsAuditorPanel);
      transactionParticipantsAuditorCandidateListBox = new ListBox();
      transactionParticipantsAuditorPanel.add(transactionParticipantsAuditorCandidateListBox);
      transactionParticipantsAuditorCandidateListBox.setWidth("65px");
      transactionParticipantsAuditorLabel = new Label("select->");
      transactionParticipantsAuditorPanel.add(transactionParticipantsAuditorLabel);
      transactionParticipantsAuditorPanel.setCellVerticalAlignment(transactionParticipantsAuditorLabel, HasVerticalAlignment.ALIGN_MIDDLE);
      transactionParticipantsAuditorPanel.setCellHorizontalAlignment(transactionParticipantsAuditorLabel, HasHorizontalAlignment.ALIGN_CENTER);
      transactionParticipantsAuditorLabel.setWidth("60px");
      transactionParticipantsAuditorListBox = new ListBox();
      transactionParticipantsAuditorPanel.add(transactionParticipantsAuditorListBox);
      transactionParticipantsAuditorListBox.setWidth("65px");
      transactionParticipantsSetButton = new Button("Set");
      transactionParticipantsPanel.add(transactionParticipantsSetButton);
      transactionParticipantsSetButton.addClickHandler(buttonHandler);
      transactionClaimCaptionPanel = new CaptionPanel("2. Claim");
      transactionPanel.add(transactionClaimCaptionPanel);
      transactionClaimCaptionPanel.setWidth("450px");
      transactionClaimPanel = new VerticalPanel();
      //transactionClaimPanel.setVisible(false);
      transactionClaimCaptionPanel.add(transactionClaimPanel);
      transactionClaimPanel.setWidth("430px");
      transactionClaimDistributionCaptionPanel = new CaptionPanel("Resource entitlement probability");
      transactionClaimPanel.add(transactionClaimDistributionCaptionPanel);
      transactionClaimDistributionCaptionPanel.setWidth("430px");
      transactionClaimDistributionPanel = new VerticalPanel();
      transactionClaimDistributionCaptionPanel.add(transactionClaimDistributionPanel);
      transactionClaimDistributionCanvas = Canvas.createIfSupported();
      if (transactionClaimDistributionCanvas != null)
      {
         transactionClaimDistribution = new NormalDistribution(transactionClaimDistributionCanvas);
         transactionClaimDistribution.draw();
         transactionClaimDistributionPanel.add(transactionClaimDistributionCanvas);
      }
      else
      {
         transactionClaimDistribution = null;
         String warning = "Your browser does not support the HTML5 Canvas";
         transactionClaimDistributionPanel.add(new Label(warning));
         Window.alert(warning);
      }
      transactionClaimDistributionParameterFlexTable = new FlexTable();
      transactionClaimDistributionPanel.add(transactionClaimDistributionParameterFlexTable);
      transactionClaimDistributionMeanLabel = new Label("Mean:");
      transactionClaimDistributionParameterFlexTable.setWidget(0, 0, transactionClaimDistributionMeanLabel);
      transactionClaimDistributionMeanTextBox = new TextBox();
      transactionClaimDistributionMeanTextBox.setWidth("60px");
      transactionClaimDistributionMeanTextBox.setText(NormalDistribution.DEFAULT_MU + "");
      transactionClaimDistributionParameterFlexTable.setWidget(0, 1, transactionClaimDistributionMeanTextBox);
      transactionClaimDistributionSigmaLabel = new Label("Sigma:");
      transactionClaimDistributionParameterFlexTable.setWidget(0, 2, transactionClaimDistributionSigmaLabel);
      transactionClaimDistributionSigmaTextBox = new TextBox();
      transactionClaimDistributionSigmaTextBox.setWidth("60px");
      transactionClaimDistributionSigmaTextBox.setText(NormalDistribution.DEFAULT_SIGMA + "");
      transactionClaimDistributionParameterFlexTable.setWidget(0, 3, transactionClaimDistributionSigmaTextBox);
      transactionClaimDistributionParameterSetButton = new Button("Set");
      transactionClaimDistributionParameterFlexTable.setWidget(0, 4, transactionClaimDistributionParameterSetButton);
      transactionClaimDistributionParameterSetButton.addClickHandler(buttonHandler);
      transactionClaimDistributionTestValueLabel = new Label("Test value:");
      transactionClaimDistributionParameterFlexTable.setWidget(1, 0, transactionClaimDistributionTestValueLabel);
      transactionClaimDistributionTestValueTextBox = new TextBox();
      transactionClaimDistributionTestValueTextBox.setWidth("60px");
      transactionClaimDistributionParameterFlexTable.setWidget(1, 1, transactionClaimDistributionTestValueTextBox);
      transactionClaimDistributionTestButton = new Button("Probability:");
      transactionClaimDistributionParameterFlexTable.setWidget(1, 2, transactionClaimDistributionTestButton);
      transactionClaimDistributionTestButton.addClickHandler(buttonHandler);
      transactionClaimDistributionTestProbabilityTextBox = new TextBox();
      transactionClaimDistributionTestProbabilityTextBox.setWidth("60px");
      transactionClaimDistributionTestProbabilityTextBox.setReadOnly(true);
      transactionClaimDistributionParameterFlexTable.setWidget(1, 3, transactionClaimDistributionTestProbabilityTextBox);
      transactionClaimFlexTable = new FlexTable();
      transactionClaimPanel.add(transactionClaimFlexTable);
      transactionClaimEntitlementLabel = new Label("Entitlement:");
      transactionClaimFlexTable.setWidget(0, 0, transactionClaimEntitlementLabel);
      transactionClaimEntitlementTextBox = new TextBox();
      transactionClaimEntitlementTextBox.setWidth("60px");
      transactionClaimFlexTable.setWidget(0, 1, transactionClaimEntitlementTextBox);
      transactionClaimEntitlementGenerateButton = new Button("Generate");
      transactionClaimEntitlementGenerateButton.addClickHandler(buttonHandler);
      transactionClaimFlexTable.setWidget(0, 2, transactionClaimEntitlementGenerateButton);
      transactionClaimEntitlementSetButton = new Button("Set");
      transactionClaimEntitlementSetButton.addClickHandler(buttonHandler);
      transactionClaimFlexTable.setWidget(0, 3, transactionClaimEntitlementSetButton);
      transactionClaimAmountLabel = new Label("Claim:");
      transactionClaimFlexTable.setWidget(1, 0, transactionClaimAmountLabel);
      transactionClaimAmountTextBox = new TextBox();
      transactionClaimAmountTextBox.setReadOnly(true);
      transactionClaimAmountTextBox.setWidth("60px");
      transactionClaimAmountTextBox.setText("waiting");
      transactionClaimFlexTable.setWidget(1, 1, transactionClaimAmountTextBox);
      transactionAuditCaptionPanel = new CaptionPanel("3. Audit");
      transactionPanel.add(transactionAuditCaptionPanel);
      transactionAuditCaptionPanel.setWidth("450px");
      transactionAuditPanel = new VerticalPanel();
      transactionAuditCaptionPanel.add(transactionAuditPanel);
      transactionCompletionCaptionPanel = new CaptionPanel("4. Completion");
      transactionPanel.add(transactionCompletionCaptionPanel);
      transactionCompletionCaptionPanel.setWidth("450px");
      transactionCompletionPanel = new VerticalPanel();
      transactionCompletionCaptionPanel.add(transactionCompletionPanel);

      roleTabPanel.selectTab(HOME_TAB);
      roleTabPanel.setSize("454px", "413px");
      roleTabPanel.addStyleName("table-center");
      this.rootPanel.add(roleTabPanel, 0, 37);

      // Initialize state.
      channel   = null;
      gameCode  = "";
      resources = 0.0;

      // Unlock UI.
      lockUI();
      unlockUI();

      // Start timer.
      timer = new Timer()
      {
         @Override
         public void run()
         {
            doUpdate();
         }
      };
      timer.scheduleRepeating(timerInterval_ms);
   }


   // Update.
   private void doUpdate()
   {
      String text = transactionClaimAmountTextBox.getText();

      if (text.startsWith("waiting"))
      {
         if (text.equals("waiting..."))
         {
            transactionClaimAmountTextBox.setText("waiting");
         }
         else if (text.equals("waiting.."))
         {
            transactionClaimAmountTextBox.setText("waiting...");
         }
         else if (text.equals("waiting."))
         {
            transactionClaimAmountTextBox.setText("waiting..");
         }
         else if (text.equals("waiting"))
         {
            transactionClaimAmountTextBox.setText("waiting.");
         }
      }
   }


   // Button handler.
   private class ButtonHandler implements ClickHandler
   {
      @Override
      public void onClick(ClickEvent event)
      {
         // Create/delete game.
         if (event.getSource() == gameCreateDeleteButton)
         {
            gameCode = gameCodeTextBox.getText();
            if ((gameCode == null) || (gameCode = gameCode.trim()).isEmpty())
            {
               Window.alert("Please enter game code");
               gameCode = "";
               return;
            }
            if (gameCode.contains(DelimitedString.DELIMITER))
            {
               Window.alert("Invalid code character: " + DelimitedString.DELIMITER);
               gameCode = "";
               return;
            }
            String r = gameResourcesTextBox.getText();
            if (Shared.isVoid(r))
            {
               Window.alert("Please enter resources");
               return;
            }
            try {
               resources = Double.parseDouble(r.trim());
            }
            catch (NumberFormatException e) {
               Window.alert("Resources must be a non-negative number");
               resources = 0.0;
               return;
            }
            if (resources < 0.0)
            {
               Window.alert("Resources must be a non-negative number");
               resources = 0.0;
               return;
            }
            lockUI();
            if (channel == null)
            {
               // Create game.
               DelimitedString createRequest = new DelimitedString(Shared.CREATE_GAME);
               createRequest.add(gameCode);
               createRequest.add(resources);
               gameService.requestService(createRequest.toString(),
                                          new AsyncCallback<String>()
                                          {
                                             public void onFailure(Throwable caught)
                                             {
                                                Window.alert("Error creating game: " + caught.getMessage());
                                                unlockUI();
                                             }

                                             public void onSuccess(String token)
                                             {
                                                if (Shared.isVoid(token))
                                                {
                                                   Window.alert("Error creating game: bad channel token");
                                                }
                                                else
                                                {
                                                   if (token.startsWith(Shared.ERROR_PREFIX))
                                                   {
                                                      Window.alert(token);
                                                   }
                                                   else
                                                   {
                                                      // Create server communication channel.
                                                      ChannelFactory channelFactory = new ChannelFactoryImpl();
                                                      channel = channelFactory.createChannel(token);
                                                      if (channel == null)
                                                      {
                                                         Window.alert("Error creating game: cannot create channel");
                                                      }
                                                      else
                                                      {
                                                         channelSocket = channel.open(new ChannelSocketListener());
                                                         gameCreateDeleteButton.setText("Delete");
                                                         showPlayerResources(0.0, resources);
                                                         Window.alert("Game created");
                                                      }
                                                   }
                                                }
                                                unlockUI();
                                             }
                                          }
                                          );
            }
            else
            {
               // Delete game.
               DelimitedString deleteRequest = new DelimitedString(Shared.DELETE_GAME);
               deleteRequest.add(gameCode);
               gameService.requestService(deleteRequest.toString(),
                                          new AsyncCallback<String>()
                                          {
                                             public void onFailure(Throwable caught)
                                             {
                                                Window.alert("Error deleting game: " + caught.getMessage());
                                                unlockUI();
                                             }

                                             public void onSuccess(String result)
                                             {
                                                if (!Shared.isOK(result))
                                                {
                                                   if (Shared.isError(result))
                                                   {
                                                      Window.alert(result);
                                                   }
                                                   else
                                                   {
                                                      Window.alert("Error deleting game");
                                                   }
                                                }
                                                else
                                                {
                                                   if (channelSocket != null)
                                                   {
                                                      channelSocket.close();
                                                   }
                                                   channel = null;
                                                   gameCreateDeleteButton.setText("Create");
                                                   gameStateListBox.setSelectedIndex(Game.PENDING);
                                                   playersListBox.clear();
                                                   playersListBox.insertItem(Shared.ALL_PLAYERS, 0);
                                                   clearPlayerResources();
                                                   Window.alert("Game deleted");
                                                }
                                                unlockUI();
                                             }
                                          }
                                          );
            }
         }

         // Remove player.
         else if (event.getSource() == playerRemoveButton)
         {
            if (channel == null)
            {
               Window.alert("Please create game!");
               return;
            }
            lockUI();
            removePlayer = null;
            for (int i = 0; i < playersListBox.getItemCount(); i++)
            {
               try
               {
                  if (playersListBox.isItemSelected(i))
                  {
                     removePlayer = playersListBox.getItemText(i);
                     break;
                  }
               }
               catch (IndexOutOfBoundsException e) {}
            }
            if (removePlayer == null)
            {
               unlockUI();
            }
            else
            {
               DelimitedString removeRequest = new DelimitedString(Shared.REMOVE_PLAYER);
               removeRequest.add(gameCode);
               removeRequest.add(removePlayer);
               gameService.requestService(removeRequest.toString(),
                                          new AsyncCallback<String>()
                                          {
                                             public void onFailure(Throwable caught)
                                             {
                                                Window.alert("Error removing player " +
                                                             removePlayer + ": " + caught.getMessage());
                                                removePlayer = null;
                                                unlockUI();
                                             }

                                             public void onSuccess(String result)
                                             {
                                                if (!Shared.isOK(result))
                                                {
                                                   if (Shared.isError(result))
                                                   {
                                                      Window.alert(result + " ");
                                                   }
                                                   else
                                                   {
                                                      Window.alert("Error removing player " + removePlayer);
                                                   }
                                                }
                                                else
                                                {
                                                   if (removePlayer.equals(Shared.ALL_PLAYERS))
                                                   {
                                                      playersListBox.clear();
                                                      playersListBox.insertItem(Shared.ALL_PLAYERS, 0);
                                                   }
                                                   else
                                                   {
                                                      for (int i = 1; i < playersListBox.getItemCount(); i++)
                                                      {
                                                         try
                                                         {
                                                            if (removePlayer.equals(playersListBox.getItemText(i)))
                                                            {
                                                               playersListBox.removeItem(i);
                                                               break;
                                                            }
                                                         }
                                                         catch (IndexOutOfBoundsException e) {}
                                                      }
                                                   }
                                                   clearPlayerResources();
                                                   playersJoinedTextBox.setText("" + (playersListBox.getItemCount() - 1));
                                                }
                                                removePlayer = null;
                                                unlockUI();
                                             }
                                          }
                                          );
            }
         }

         // Player chat.
         else if (event.getSource() == playerChatClearButton)
         {
            playerChatTextArea.setText("Note: prepend <player name>/ to send to specific player");
         }
         else if (event.getSource() == playerChatSendButton)
         {
            String chatText = playerChatTextBox.getText();
            if (Shared.isVoid(chatText))
            {
               return;
            }
            if (channel == null)
            {
               Window.alert("Please create game!");
               return;
            }
            lockUI();
            DelimitedString chatRequest = new DelimitedString(Shared.PLAYER_CHAT);
            chatRequest.add(gameCode);
            chatRequest.add(chatText);
            gameService.requestService(chatRequest.toString(),
                                       new AsyncCallback<String>()
                                       {
                                          public void onFailure(Throwable caught)
                                          {
                                             Window.alert("Error sending chat: " + caught.getMessage());
                                             unlockUI();
                                          }

                                          public void onSuccess(String result)
                                          {
                                             if (!Shared.isOK(result))
                                             {
                                                if (Shared.isError(result))
                                                {
                                                   Window.alert(result);
                                                }
                                                else
                                                {
                                                   Window.alert("Error sending chat");
                                                }
                                             }
                                             else
                                             {
                                                playerChatTextArea.setText(playerChatTextArea.getText() +
                                                                           "host: " +
                                                                           playerChatTextBox.getText() + "\n");
                                                playerChatTextBox.setText("");
                                             }
                                             unlockUI();
                                          }
                                       }
                                       );
         }
         else if (event.getSource() == transactionClaimDistributionParameterSetButton)
         {
            if (transactionClaimDistribution == null)
            {
               Window.alert("Resource entitlement probability unavailable");
               return;
            }
            String meanText = transactionClaimDistributionMeanTextBox.getText();
            if (Shared.isVoid(meanText))
            {
               Window.alert("Please enter mean");
               return;
            }
            String sigmaText = transactionClaimDistributionSigmaTextBox.getText();
            if (Shared.isVoid(sigmaText))
            {
               Window.alert("Please enter sigma");
               return;
            }
            double mean;
            try {
               mean = Double.parseDouble(meanText);
            }
            catch (NumberFormatException e) {
               Window.alert("Invalid mean");
               return;
            }
            if (mean <= 0.0)
            {
               Window.alert("Invalid mean");
               return;
            }
            double sigma;
            try {
               sigma = Double.parseDouble(sigmaText);
            }
            catch (NumberFormatException e) {
               Window.alert("Invalid sigma");
               return;
            }
            if (sigma <= 0.0)
            {
               Window.alert("Invalid sigma");
               return;
            }
            transactionClaimDistribution.setMu(mean);
            transactionClaimDistribution.setSigma(sigma);
            transactionClaimDistribution.draw();
         }
         else if (event.getSource() == transactionClaimDistributionTestButton)
         {
            if (transactionClaimDistribution == null)
            {
               Window.alert("Resource entitlement probability unavailable");
               return;
            }
            String valueText = transactionClaimDistributionTestValueTextBox.getText();
            if (Shared.isVoid(valueText))
            {
               Window.alert("Please enter test value");
               return;
            }
            double value;
            try {
               value = Double.parseDouble(valueText);
            }
            catch (NumberFormatException e) {
               Window.alert("Invalid test value");
               return;
            }
            double       probability   = transactionClaimDistribution.phi(value);
            NumberFormat decimalFormat = NumberFormat.getFormat(".##");
            transactionClaimDistributionTestProbabilityTextBox.setText(decimalFormat.format(probability));
         }
         else if (event.getSource() == transactionClaimEntitlementGenerateButton)
         {
            if (transactionClaimDistribution == null)
            {
               Window.alert("Resource entitlement probability unavailable");
               return;
            }
            NumberFormat decimalFormat = NumberFormat.getFormat(".##");
            transactionClaimEntitlementTextBox.setText(decimalFormat.format(transactionClaimDistribution.nextValue()));
         }
      }
   }

   // ListBox handler.
   private class ListBoxHandler implements ChangeHandler
   {
      @Override
      public void onChange(ChangeEvent event)
      {
         if (event.getSource() == gameStateListBox)
         {
            // Update game state.
            if (channel == null)
            {
               Window.alert("Please create game!");
               return;
            }
            lockUI();
            DelimitedString updateRequest = new DelimitedString(Shared.UPDATE_GAME);
            updateRequest.add(gameCode);
            updateRequest.add(gameStateListBox.getSelectedIndex());
            gameService.requestService(updateRequest.toString(),
                                       new AsyncCallback<String>()
                                       {
                                          public void onFailure(Throwable caught)
                                          {
                                             Window.alert("Error updating game: " + caught.getMessage());
                                             unlockUI();
                                          }

                                          public void onSuccess(String result)
                                          {
                                             if (!Shared.isOK(result))
                                             {
                                                if (Shared.isError(result))
                                                {
                                                   Window.alert(result);
                                                }
                                                else
                                                {
                                                   Window.alert("Error updating game");
                                                }
                                             }
                                             unlockUI();
                                          }
                                       }
                                       );
         }
         else if (event.getSource() == playersListBox)
         {
            updatePlayerResources();
         }
      }
   }

   // Update player resources.
   private void updatePlayerResources()
   {
      String playerName = null;

      for (int i = 0; i < playersListBox.getItemCount(); i++)
      {
         try
         {
            if (playersListBox.isItemSelected(i))
            {
               playerName = playersListBox.getItemText(i);
               break;
            }
         }
         catch (IndexOutOfBoundsException e) {}
      }
      if (playerName == null)
      {
         clearPlayerResources();
         return;
      }
      lockUI();
      DelimitedString updateRequest = new DelimitedString(Shared.GET_PLAYER_RESOURCES);
      updateRequest.add(gameCode);
      updateRequest.add(playerName);
      gameService.requestService(updateRequest.toString(),
                                 new AsyncCallback<String>()
                                 {
                                    public void onFailure(Throwable caught)
                                    {
                                       Window.alert("Error getting player resources: " + caught.getMessage());
                                       unlockUI();
                                    }

                                    public void onSuccess(String result)
                                    {
                                       if (Shared.isVoid(result))
                                       {
                                          Window.alert("Error getting player resources");
                                       }
                                       else
                                       {
                                          if (Shared.isError(result))
                                          {
                                             Window.alert(result);
                                          }
                                          else
                                          {
                                             String[] args = new DelimitedString(result).parse();
                                             if (args.length != 3)
                                             {
                                                Window.alert("Error getting player resources");
                                             }
                                             else
                                             {
                                                showPlayerResources(args[0], args[1]);
                                             }
                                          }
                                       }
                                       unlockUI();
                                    }
                                 }
                                 );
   }


   private void clearPlayerResources()
   {
      playerTotalResourceTextBox.setText("");
      playerPersonalResourceTextBox.setText("");
      playerCommonResourceTextBox.setText("");
   }


   private void showPlayerResources(String personal, String common)
   {
      double total = Double.parseDouble(personal) + Double.parseDouble(common);

      NumberFormat decimalFormat = NumberFormat.getFormat(".##");

      playerTotalResourceTextBox.setText(decimalFormat.format(total));
      playerPersonalResourceTextBox.setText(personal);
      playerCommonResourceTextBox.setText(common);
   }


   private void showPlayerResources(double personal, double common)
   {
      double total = personal + common;

      NumberFormat decimalFormat = NumberFormat.getFormat(".##");

      playerTotalResourceTextBox.setText(decimalFormat.format(total));
      playerPersonalResourceTextBox.setText(decimalFormat.format(personal));
      playerCommonResourceTextBox.setText(decimalFormat.format(common));
   }


   private void lockUI()
   {
      gameCodeTextBox.setReadOnly(true);
      gameResourcesTextBox.setReadOnly(true);
      gameCreateDeleteButton.setEnabled(false);
      gameStateListBox.setEnabled(false);
      playersListBox.setEnabled(false);
      playerRemoveButton.setEnabled(false);
      playerChatTextBox.setReadOnly(true);
      playerChatSendButton.setEnabled(false);
   }


   private void unlockUI()
   {
      gameCreateDeleteButton.setEnabled(true);
      if (channel == null)
      {
         gameCodeTextBox.setReadOnly(false);
         gameResourcesTextBox.setReadOnly(false);
      }
      else
      {
         gameStateListBox.setEnabled(true);
         playersListBox.setEnabled(true);
         playerRemoveButton.setEnabled(true);
         playerChatTextBox.setReadOnly(false);
         playerChatSendButton.setEnabled(true);
      }
   }


   // Channel socket listener.
   class ChannelSocketListener implements SocketListener
   {
      @Override
      public void onOpen()
      {
      }


      @Override
      public void onMessage(String message)
      {
         if (Shared.isVoid(message))
         {
            return;
         }
         String[] args = new DelimitedString(message).parse();
         if (args.length == 0)
         {
            return;
         }
         String operation = args[0];
         if (operation.equals(Shared.JOIN_GAME) && (args.length == 3))
         {
            // Player joining game.
            String playerName = args[2];
            int    i          = 1;
            for ( ; i < playersListBox.getItemCount(); i++)
            {
               if (playerName.compareTo(playersListBox.getItemText(i)) < 0)
               {
                  break;
               }
            }
            playersListBox.insertItem(playerName, i);
            playersJoinedTextBox.setText("" + (playersListBox.getItemCount() - 1));
            updatePlayerResources();
         }
         else if (operation.equals(Shared.QUIT_GAME) && (args.length == 3))
         {
            // Player quitting game.
            String playerName = args[2];
            for (int i = 1; i < playersListBox.getItemCount(); i++)
            {
               try
               {
                  if (playersListBox.getItemText(i).equals(playerName))
                  {
                     playersListBox.removeItem(i);
                     break;
                  }
               }
               catch (IndexOutOfBoundsException e) {}
            }
            playersJoinedTextBox.setText("" + (playersListBox.getItemCount() - 1));
            updatePlayerResources();
         }
         else if (operation.equals(Shared.HOST_CHAT) && (args.length == 4))
         {
            // Host chat.
            String playerName = args[2];
            String chatText   = args[3];
            playerChatTextArea.setText(playerChatTextArea.getText() +
                                       playerName + ": " + chatText + "\n");
         }
      }


      @Override
      public void onError(ChannelError error)
      {
         Window.alert("Channel error: " + error.getCode() + " : " + error.getDescription());
      }


      @Override
      public void onClose()
      {
      }
   }
}
