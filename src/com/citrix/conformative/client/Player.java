// Conformative game player client.

package com.citrix.conformative.client;

import com.citrix.conformative.shared.Shared;
import com.citrix.conformative.shared.DelimitedString;
import com.google.gwt.appengine.channel.client.Channel;
import com.google.gwt.appengine.channel.client.ChannelError;
import com.google.gwt.appengine.channel.client.ChannelFactory;
import com.google.gwt.appengine.channel.client.ChannelFactoryImpl;
import com.google.gwt.appengine.channel.client.Socket;
import com.google.gwt.appengine.channel.client.SocketListener;
import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CaptionPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TabBar;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.HasVerticalAlignment;

public class Player
{
   // GUI: home, claim, and audit roles.
   private ButtonHandler      buttonHandler;
   private RootPanel          rootPanel;
   private Label              conformativeGameLabel;
   private TabPanel           roleTabPanel;
   private TabBar             roleTabBar;
   private VerticalPanel      homePanel;
   private FlexTable          homeJoinFlexTable;
   private Label              playerNameLabel;
   private TextBox            playerNameTextBox;
   private Button             playerJoinQuitButton;
   private Label              gameCodeLabel;
   private TextBox            gameCodeTextBox;
   private CaptionPanel       homeResourcesCaptionPanel;
   private FlexTable          homeResourcesFlexTable;
   private Label              homeResourcesActualLabel;
   private TextBox            homeResourcesActualTextBox;
   private Label              homeResourcesPersonalLabel;
   private TextBox            homeResourcesPersonalTextBox;
   private Label              homeResourcesCommonLabel;
   private TextBox            homeResourcesCommonTextBox;
   private Label              homeResourcesEntitledLabel;
   private TextBox            homeResourcesEntitledTextBox;
   private CaptionPanel       hostChatCaptionPanel;
   private VerticalPanel      hostChatPanel;
   private TextArea           hostChatTextArea;
   private Button             hostChatClearButton;
   private TextBox            hostChatTextBox;
   private Button             hostChatSendButton;
   private CaptionPanel       claimHistoryCaptionPanel;
   private TextArea           claimHistoryTextArea;
   private CaptionPanel       auditHistoryCaptionPanel;
   private TextArea           auditHistoryTextArea;
   private VerticalPanel      claimPanel;
   private CaptionPanel       claimDistributionCaptionPanel;
   private VerticalPanel      claimDistributionPanel;
   private Canvas             claimDistributionCanvas;
   private NormalDistribution claimDistribution;
   private HorizontalPanel    claimDistributionTestPanel;
   private Label              claimDistributionTestValueLabel;
   private TextBox            claimDistributionTestValueTextBox;
   private Button             claimDistributionTestButton;
   private TextBox            claimDistributionTestProbabilityTextBox;
   private CaptionPanel       claimResourcesCaptionPanel;
   private FlexTable          claimResourcesFlexTable;
   private Label              claimResourcesEntitledLabel;
   private TextBox            claimResourcesEntitledTextBox;
   private Label              claimResourcesEntitledEqualsLabel;
   private TextBox            claimResourcesEntitledPerPlayerTextBox;
   private Label              claimResourcesEntitledTimesLabel;
   private TextBox            claimResourcesEntitledNumPlayersTextBox;
   private Label              claimResourcesEntitledPlayersLabel;
   private Label              claimResourcesClaimLabel;
   private TextBox            claimResourcesClaimTextBox;
   private Button             claimSetButton;
   private Label              claimResourcesGrantLabel;
   private TextBox            claimResourcesGrantTextBox;
   private Label              claimResourcesPenaltyLabel;
   private TextBox            claimResourcesPenaltyTextBox;
   private Label              claimResourcesDonateLabel;
   private TextBox            claimResourcesDonateTextBox;
   private Label              claimResourcesDonateNameLabel;
   private ListBox            claimResourcesDonateNameListBox;
   private Button             claimResourcesDonateButton;
   private Button             claimFinishButton;
   private CaptionPanel       auditorChatCaptionPanel;
   private VerticalPanel      auditorChatPanel;
   private TextArea           auditorChatTextArea;
   private TextBox            auditorChatTextBox;
   private Button             auditorChatSendButton;
   private VerticalPanel      auditPanel;
   private HorizontalPanel    claimantNamePanel;
   private Label              claimantNameLabel;
   private TextBox            claimantNameTextBox;
   private CaptionPanel       auditDistributionCaptionPanel;
   private VerticalPanel      auditDistributionPanel;
   private Canvas             auditDistributionCanvas;
   private NormalDistribution auditDistribution;
   private HorizontalPanel    auditDistributionTestPanel;
   private Label              auditDistributionTestValueLabel;
   private TextBox            auditDistributionTestValueTextBox;
   private Button             auditDistributionTestButton;
   private TextBox            auditDistributionTestProbabilityTextBox;
   private CaptionPanel       auditResourcesCaptionPanel;
   private FlexTable          auditResourcesFlexTable;
   private Label              auditResourcesClaimLabel;
   private TextBox            auditResourcesClaimTextBox;
   private Label              auditResourcesClaimEqualsLabel;
   private TextBox            auditResourcesClaimPerPlayerTextBox;
   private Label              auditResourcesClaimTimesLabel;
   private TextBox            auditResourcesClaimNumPlayersTextBox;
   private Label              auditResourcesClaimPlayersLabel;
   private Label              auditResourcesGrantLabel;
   private TextBox            auditResourcesGrantTextBox;
   private Button             auditResourcesGrantSetButton;
   private Label              auditResourcesGrantConsensusLabel;
   private TextBox            auditResourcesConsensusTextBox;
   private Label              auditResourcesPenaltyLabel;
   private TextBox            auditResourcesPenaltyTextBox;
   private Button             auditFinishButton;
   private CaptionPanel       claimantChatCaptionPanel;
   private VerticalPanel      claimantChatPanel;
   private TextArea           claimantChatTextArea;
   private TextBox            claimantChatTextBox;
   private Button             claimantChatSendButton;

   private static final int HOME_TAB  = 0;
   private static final int CLAIM_TAB = 1;
   private static final int AUDIT_TAB = 2;

   // Remote service proxy.
   private final GameServiceAsync gameService;

   // Channel and socket.
   private Channel channel;
   private Socket  channelSocket;

   // Player name and game code.
   private String playerName;
   private String gameCode;

   // Timer.
   private final int timerInterval_ms = 500;
   private Timer     timer;

   // Constructor.
   public Player(GameServiceAsync gameService, RootPanel rootPanel)
   {
      // Server request.
      this.gameService = gameService;

      // Button handler.
      buttonHandler = new ButtonHandler();

      // Root panel.
      this.rootPanel        = rootPanel;
      conformativeGameLabel = new Label("Conformative Game Player");
      conformativeGameLabel.setStyleName("gwt-Label-Title");
      conformativeGameLabel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
      conformativeGameLabel.setSize("450px", "31px");
      this.rootPanel.add(conformativeGameLabel, 0, 0);

      // Role tabs.
      roleTabPanel = new TabPanel();
      roleTabBar   = roleTabPanel.getTabBar();

      // Home tab.
      homePanel = new VerticalPanel();
      roleTabPanel.add(homePanel, "Home");
      homePanel.setHeight("");
      homeJoinFlexTable = new FlexTable();
      homePanel.add(homeJoinFlexTable);
      playerNameLabel = new Label("Name:");
      homeJoinFlexTable.setWidget(0, 0, playerNameLabel);
      playerNameTextBox = new TextBox();
      homeJoinFlexTable.setWidget(0, 1, playerNameTextBox);
      playerNameTextBox.setWidth("300px");
      playerJoinQuitButton = new Button("Join");
      playerJoinQuitButton.addClickHandler(buttonHandler);
      homeJoinFlexTable.setWidget(0, 2, playerJoinQuitButton);
      gameCodeLabel = new Label("Code:");
      homeJoinFlexTable.setWidget(1, 0, gameCodeLabel);
      gameCodeTextBox = new TextBox();
      homeJoinFlexTable.setWidget(1, 1, gameCodeTextBox);
      gameCodeTextBox.setWidth("120px");
      homeResourcesCaptionPanel = new CaptionPanel("Resources");
      homePanel.add(homeResourcesCaptionPanel);
      homeResourcesCaptionPanel.setSize("450px", "85px");
      homeResourcesFlexTable = new FlexTable();
      homeResourcesCaptionPanel.add(homeResourcesFlexTable);
      homeResourcesFlexTable.setWidth("340px");
      homeResourcesActualLabel = new Label("Actual:");
      homeResourcesFlexTable.setWidget(0, 0, homeResourcesActualLabel);
      homeResourcesActualTextBox = new TextBox();
      homeResourcesActualTextBox.setReadOnly(true);
      homeResourcesFlexTable.setWidget(0, 1, homeResourcesActualTextBox);
      homeResourcesActualTextBox.setWidth("60px");
      homeResourcesPersonalLabel = new Label(" = Personal:");
      homeResourcesFlexTable.setWidget(0, 2, homeResourcesPersonalLabel);
      homeResourcesPersonalLabel.setWidth("75px");
      homeResourcesPersonalTextBox = new TextBox();
      homeResourcesPersonalTextBox.setReadOnly(true);
      homeResourcesFlexTable.setWidget(0, 3, homeResourcesPersonalTextBox);
      homeResourcesPersonalTextBox.setWidth("60px");
      homeResourcesCommonLabel = new Label(" + Common:");
      homeResourcesFlexTable.setWidget(0, 4, homeResourcesCommonLabel);
      homeResourcesCommonLabel.setWidth("75px");
      homeResourcesCommonTextBox = new TextBox();
      homeResourcesCommonTextBox.setReadOnly(true);
      homeResourcesFlexTable.setWidget(0, 5, homeResourcesCommonTextBox);
      homeResourcesCommonTextBox.setWidth("60px");
      homeResourcesEntitledLabel = new Label("Entitled:");
      homeResourcesFlexTable.setWidget(1, 0, homeResourcesEntitledLabel);
      homeResourcesEntitledTextBox = new TextBox();
      homeResourcesEntitledTextBox.setReadOnly(true);
      homeResourcesFlexTable.setWidget(1, 1, homeResourcesEntitledTextBox);
      homeResourcesEntitledTextBox.setWidth("60px");
      hostChatCaptionPanel = new CaptionPanel("Host chat");
      homePanel.add(hostChatCaptionPanel);
      hostChatCaptionPanel.setWidth("450px");
      hostChatPanel = new VerticalPanel();
      hostChatCaptionPanel.add(hostChatPanel);
      hostChatTextArea = new TextArea();
      hostChatTextArea.setReadOnly(true);
      hostChatPanel.add(hostChatTextArea);
      homePanel.setCellVerticalAlignment(hostChatTextArea, HasVerticalAlignment.ALIGN_BOTTOM);
      hostChatTextArea.setSize("430px", "100px");
      hostChatClearButton = new Button("Clear");
      hostChatClearButton.addClickHandler(buttonHandler);
      hostChatPanel.add(hostChatClearButton);
      hostChatTextBox = new TextBox();
      hostChatPanel.add(hostChatTextBox);
      hostChatTextBox.setWidth("430px");
      hostChatSendButton = new Button("Send");
      hostChatSendButton.addClickHandler(buttonHandler);
      hostChatPanel.add(hostChatSendButton);
      claimHistoryCaptionPanel = new CaptionPanel("Claim history");
      homePanel.add(claimHistoryCaptionPanel);
      claimHistoryCaptionPanel.setWidth("450px");
      claimHistoryTextArea = new TextArea();
      claimHistoryTextArea.setReadOnly(true);
      claimHistoryCaptionPanel.add(claimHistoryTextArea);
      claimHistoryTextArea.setSize("430px", "50px");
      auditHistoryCaptionPanel = new CaptionPanel("Audit history");
      homePanel.add(auditHistoryCaptionPanel);
      auditHistoryCaptionPanel.setWidth("450px");
      auditHistoryTextArea = new TextArea();
      auditHistoryTextArea.setReadOnly(true);
      auditHistoryCaptionPanel.add(auditHistoryTextArea);
      auditHistoryTextArea.setSize("430px", "50px");

      // Claim tab.
      claimPanel = new VerticalPanel();
      roleTabPanel.add(claimPanel, "Claim");
      claimDistributionCaptionPanel = new CaptionPanel("Resource entitlement probability");
      claimPanel.add(claimDistributionCaptionPanel);
      claimDistributionCaptionPanel.setWidth("450px");
      claimDistributionPanel = new VerticalPanel();
      claimDistributionCaptionPanel.add(claimDistributionPanel);
      claimDistributionCanvas = Canvas.createIfSupported();
      if (claimDistributionCanvas != null)
      {
         claimDistribution = new NormalDistribution(claimDistributionCanvas);
         claimDistribution.draw();
         claimDistributionPanel.add(claimDistributionCanvas);
      }
      else
      {
         claimDistribution = null;
         String warning = "Your browser does not support the HTML5 Canvas";
         claimDistributionPanel.add(new Label(warning));
         Window.alert(warning);
      }
      claimDistributionTestPanel = new HorizontalPanel();
      claimDistributionPanel.add(claimDistributionTestPanel);
      claimDistributionTestPanel.setWidth("325px");
      claimDistributionTestValueLabel = new Label("Test value:");
      claimDistributionTestPanel.add(claimDistributionTestValueLabel);
      claimDistributionTestPanel.setCellVerticalAlignment(claimDistributionTestValueLabel, HasVerticalAlignment.ALIGN_MIDDLE);
      claimDistributionTestValueTextBox = new TextBox();
      claimDistributionTestValueTextBox.setWidth("60px");
      claimDistributionTestPanel.add(claimDistributionTestValueTextBox);
      claimDistributionTestPanel.setCellVerticalAlignment(claimDistributionTestValueTextBox, HasVerticalAlignment.ALIGN_MIDDLE);
      claimDistributionTestButton = new Button("Probability:");
      claimDistributionTestPanel.add(claimDistributionTestButton);
      claimDistributionTestPanel.setCellVerticalAlignment(claimDistributionTestButton, HasVerticalAlignment.ALIGN_MIDDLE);
      claimDistributionTestButton.addClickHandler(buttonHandler);
      claimDistributionTestProbabilityTextBox = new TextBox();
      claimDistributionTestProbabilityTextBox.setWidth("60px");
      claimDistributionTestProbabilityTextBox.setReadOnly(true);
      claimDistributionTestPanel.add(claimDistributionTestProbabilityTextBox);
      claimDistributionTestPanel.setCellVerticalAlignment(claimDistributionTestProbabilityTextBox, HasVerticalAlignment.ALIGN_MIDDLE);
      claimResourcesCaptionPanel = new CaptionPanel("Resource transaction");
      claimPanel.add(claimResourcesCaptionPanel);
      claimResourcesCaptionPanel.setWidth("450px");
      claimResourcesFlexTable = new FlexTable();
      claimResourcesCaptionPanel.add(claimResourcesFlexTable);
      claimResourcesEntitledLabel = new Label("Entitled:");
      claimResourcesFlexTable.setWidget(0, 0, claimResourcesEntitledLabel);
      claimResourcesEntitledTextBox = new TextBox();
      claimResourcesEntitledTextBox.setReadOnly(true);
      claimResourcesFlexTable.setWidget(0, 1, claimResourcesEntitledTextBox);
      claimResourcesEntitledTextBox.setWidth("60px");
      claimResourcesEntitledEqualsLabel = new Label("=");
      claimResourcesEntitledEqualsLabel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
      claimResourcesFlexTable.setWidget(0, 2, claimResourcesEntitledEqualsLabel);
      claimResourcesEntitledPerPlayerTextBox = new TextBox();
      claimResourcesEntitledPerPlayerTextBox.setReadOnly(true);
      claimResourcesFlexTable.setWidget(0, 3, claimResourcesEntitledPerPlayerTextBox);
      claimResourcesEntitledPerPlayerTextBox.setWidth("60px");
      claimResourcesEntitledTimesLabel = new Label("X");
      claimResourcesEntitledTimesLabel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
      claimResourcesFlexTable.setWidget(0, 4, claimResourcesEntitledTimesLabel);
      claimResourcesEntitledNumPlayersTextBox = new TextBox();
      claimResourcesEntitledNumPlayersTextBox.setReadOnly(true);
      claimResourcesFlexTable.setWidget(0, 5, claimResourcesEntitledNumPlayersTextBox);
      claimResourcesEntitledNumPlayersTextBox.setWidth("60px");
      claimResourcesEntitledPlayersLabel = new Label("players");
      claimResourcesFlexTable.setWidget(0, 6, claimResourcesEntitledPlayersLabel);
      claimResourcesClaimLabel = new Label("Claim:");
      claimResourcesFlexTable.setWidget(1, 0, claimResourcesClaimLabel);
      claimResourcesClaimTextBox = new TextBox();
      claimResourcesFlexTable.setWidget(1, 1, claimResourcesClaimTextBox);
      claimResourcesClaimTextBox.setWidth("60px");
      claimSetButton = new Button("Set");
      claimResourcesFlexTable.setWidget(1, 2, claimSetButton);
      claimSetButton.addClickHandler(buttonHandler);
      claimResourcesGrantLabel = new Label("Grant:");
      claimResourcesFlexTable.setWidget(2, 0, claimResourcesGrantLabel);
      claimResourcesGrantTextBox = new TextBox();
      claimResourcesGrantTextBox.setReadOnly(true);
      claimResourcesGrantTextBox.setText("waiting");
      claimResourcesFlexTable.setWidget(2, 1, claimResourcesGrantTextBox);
      claimResourcesGrantTextBox.setWidth("60px");
      claimResourcesPenaltyLabel = new Label("Penalty:");
      claimResourcesFlexTable.setWidget(3, 0, claimResourcesPenaltyLabel);
      claimResourcesPenaltyTextBox = new TextBox();
      claimResourcesPenaltyTextBox.setReadOnly(true);
      claimResourcesFlexTable.setWidget(3, 1, claimResourcesPenaltyTextBox);
      claimResourcesPenaltyTextBox.setWidth("60px");
      claimResourcesDonateLabel = new Label("Donate:");
      claimResourcesFlexTable.setWidget(4, 0, claimResourcesDonateLabel);
      claimResourcesDonateTextBox = new TextBox();
      claimResourcesFlexTable.setWidget(4, 1, claimResourcesDonateTextBox);
      claimResourcesDonateTextBox.setWidth("60px");
      claimResourcesDonateNameLabel = new Label("To:");
      claimResourcesDonateNameLabel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
      claimResourcesFlexTable.setWidget(4, 2, claimResourcesDonateNameLabel);
      claimResourcesDonateNameListBox = new ListBox();
      claimResourcesDonateNameListBox.setWidth("65px");
      claimResourcesFlexTable.setWidget(4, 3, claimResourcesDonateNameListBox);
      claimResourcesDonateButton = new Button("Send");
      claimResourcesDonateButton.setText("Go");
      claimResourcesDonateButton.addClickHandler(buttonHandler);
      claimResourcesFlexTable.setWidget(4, 4, claimResourcesDonateButton);
      claimFinishButton = new Button("Finish");
      claimFinishButton.addClickHandler(buttonHandler);
      claimResourcesFlexTable.setWidget(5, 0, claimFinishButton);
      auditorChatCaptionPanel = new CaptionPanel("Auditor chat");
      claimPanel.add(auditorChatCaptionPanel);
      auditorChatCaptionPanel.setWidth("450px");
      auditorChatPanel = new VerticalPanel();
      auditorChatCaptionPanel.add(auditorChatPanel);
      auditorChatTextArea = new TextArea();
      auditorChatTextArea.setReadOnly(true);
      auditorChatPanel.add(auditorChatTextArea);
      auditorChatTextArea.setSize("430px", "100px");
      auditorChatTextBox = new TextBox();
      auditorChatPanel.add(auditorChatTextBox);
      auditorChatTextBox.setWidth("430px");
      auditorChatSendButton = new Button("Send");
      auditorChatPanel.add(auditorChatSendButton);
      auditorChatSendButton.addClickHandler(buttonHandler);

      // Audit tab.
      auditPanel = new VerticalPanel();
      roleTabPanel.add(auditPanel, "Audit");
      claimantNamePanel = new HorizontalPanel();
      auditPanel.add(claimantNamePanel);
      claimantNamePanel.setSize("430px", "50px");
      claimantNameLabel = new Label("Claimant:");
      claimantNamePanel.add(claimantNameLabel);
      claimantNamePanel.setCellVerticalAlignment(claimantNameLabel, HasVerticalAlignment.ALIGN_MIDDLE);
      claimantNameLabel.setWidth("55px");
      claimantNameTextBox = new TextBox();
      claimantNameTextBox.setReadOnly(true);
      claimantNamePanel.add(claimantNameTextBox);
      claimantNamePanel.setCellVerticalAlignment(claimantNameTextBox, HasVerticalAlignment.ALIGN_MIDDLE);
      claimantNameTextBox.setWidth("300px");
      auditDistributionCaptionPanel = new CaptionPanel("Resource entitlement probability");
      auditPanel.add(auditDistributionCaptionPanel);
      auditDistributionCaptionPanel.setWidth("450px");
      auditDistributionPanel = new VerticalPanel();
      auditDistributionCaptionPanel.add(auditDistributionPanel);
      auditDistributionCanvas = Canvas.createIfSupported();
      if (auditDistributionCanvas != null)
      {
         auditDistribution = new NormalDistribution(auditDistributionCanvas);
         auditDistribution.draw();
         auditDistributionPanel.add(auditDistributionCanvas);
      }
      else
      {
         auditDistribution = null;
         String warning = "Your browser does not support the HTML5 Canvas";
         auditDistributionPanel.add(new Label(warning));
         Window.alert(warning);
      }
      auditDistributionTestPanel = new HorizontalPanel();
      auditDistributionPanel.add(auditDistributionTestPanel);
      auditDistributionTestPanel.setWidth("325px");
      auditDistributionTestValueLabel = new Label("Test value:");
      auditDistributionTestPanel.add(auditDistributionTestValueLabel);
      auditDistributionTestPanel.setCellVerticalAlignment(auditDistributionTestValueLabel, HasVerticalAlignment.ALIGN_MIDDLE);
      auditDistributionTestValueTextBox = new TextBox();
      auditDistributionTestValueTextBox.setWidth("60px");
      auditDistributionTestPanel.add(auditDistributionTestValueTextBox);
      auditDistributionTestPanel.setCellVerticalAlignment(auditDistributionTestValueTextBox, HasVerticalAlignment.ALIGN_MIDDLE);
      auditDistributionTestButton = new Button("Probability:");
      auditDistributionTestPanel.add(auditDistributionTestButton);
      auditDistributionTestPanel.setCellVerticalAlignment(auditDistributionTestButton, HasVerticalAlignment.ALIGN_MIDDLE);
      auditDistributionTestButton.addClickHandler(buttonHandler);
      auditDistributionTestProbabilityTextBox = new TextBox();
      auditDistributionTestProbabilityTextBox.setWidth("60px");
      auditDistributionTestProbabilityTextBox.setReadOnly(true);
      auditDistributionTestPanel.add(auditDistributionTestProbabilityTextBox);
      auditDistributionTestPanel.setCellVerticalAlignment(auditDistributionTestProbabilityTextBox, HasVerticalAlignment.ALIGN_MIDDLE);
      auditResourcesCaptionPanel = new CaptionPanel("Resource transaction");
      auditPanel.add(auditResourcesCaptionPanel);
      auditResourcesCaptionPanel.setWidth("450px");
      auditResourcesFlexTable = new FlexTable();
      auditResourcesCaptionPanel.add(auditResourcesFlexTable);
      auditResourcesClaimLabel = new Label("Claim:");
      auditResourcesFlexTable.setWidget(0, 0, auditResourcesClaimLabel);
      auditResourcesClaimTextBox = new TextBox();
      auditResourcesClaimTextBox.setReadOnly(true);
      auditResourcesFlexTable.setWidget(0, 1, auditResourcesClaimTextBox);
      auditResourcesClaimTextBox.setWidth("60px");
      auditResourcesClaimEqualsLabel = new Label("=");
      auditResourcesClaimEqualsLabel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
      auditResourcesFlexTable.setWidget(0, 2, auditResourcesClaimEqualsLabel);
      auditResourcesClaimPerPlayerTextBox = new TextBox();
      auditResourcesClaimPerPlayerTextBox.setReadOnly(true);
      auditResourcesFlexTable.setWidget(0, 3, auditResourcesClaimPerPlayerTextBox);
      auditResourcesClaimPerPlayerTextBox.setWidth("60px");
      auditResourcesClaimTimesLabel = new Label("X");
      auditResourcesClaimTimesLabel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
      auditResourcesFlexTable.setWidget(0, 4, auditResourcesClaimTimesLabel);
      auditResourcesClaimTimesLabel.setWidth("10px");
      auditResourcesClaimNumPlayersTextBox = new TextBox();
      auditResourcesClaimNumPlayersTextBox.setReadOnly(true);
      auditResourcesFlexTable.setWidget(0, 5, auditResourcesClaimNumPlayersTextBox);
      auditResourcesClaimNumPlayersTextBox.setWidth("60px");
      auditResourcesClaimPlayersLabel = new Label("players");
      auditResourcesFlexTable.setWidget(0, 6, auditResourcesClaimPlayersLabel);
      auditResourcesGrantLabel = new Label("Grant:");
      auditResourcesFlexTable.setWidget(1, 0, auditResourcesGrantLabel);
      auditResourcesGrantTextBox = new TextBox();
      auditResourcesFlexTable.setWidget(1, 1, auditResourcesGrantTextBox);
      auditResourcesGrantTextBox.setWidth("60px");
      auditResourcesGrantSetButton = new Button("Set");
      auditResourcesGrantSetButton.addClickHandler(buttonHandler);
      auditResourcesFlexTable.setWidget(1, 2, auditResourcesGrantSetButton);
      auditResourcesGrantConsensusLabel = new Label("Consensus:");
      auditResourcesGrantConsensusLabel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
      auditResourcesFlexTable.setWidget(1, 3, auditResourcesGrantConsensusLabel);
      auditResourcesConsensusTextBox = new TextBox();
      auditResourcesConsensusTextBox.setReadOnly(true);
      auditResourcesConsensusTextBox.setText("waiting");
      auditResourcesFlexTable.setWidget(1, 5, auditResourcesConsensusTextBox);
      auditResourcesConsensusTextBox.setWidth("60px");
      auditResourcesPenaltyLabel = new Label("Penalty:");
      auditResourcesFlexTable.setWidget(2, 0, auditResourcesPenaltyLabel);
      auditResourcesPenaltyTextBox = new TextBox();
      auditResourcesPenaltyTextBox.setReadOnly(true);
      auditResourcesFlexTable.setWidget(2, 1, auditResourcesPenaltyTextBox);
      auditResourcesPenaltyTextBox.setWidth("60px");
      auditFinishButton = new Button("Finish");
      auditFinishButton.addClickHandler(buttonHandler);
      auditResourcesFlexTable.setWidget(3, 0, auditFinishButton);
      claimantChatCaptionPanel = new CaptionPanel("Claimant chat");
      auditPanel.add(claimantChatCaptionPanel);
      claimantChatCaptionPanel.setWidth("450px");
      claimantChatPanel = new VerticalPanel();
      claimantChatCaptionPanel.add(claimantChatPanel);
      claimantChatTextArea = new TextArea();
      claimantChatTextArea.setReadOnly(true);
      claimantChatPanel.add(claimantChatTextArea);
      claimantChatTextArea.setSize("430px", "100px");
      claimantChatTextBox = new TextBox();
      claimantChatPanel.add(claimantChatTextBox);
      claimantChatTextBox.setWidth("430px");
      claimantChatSendButton = new Button("Send");
      claimantChatSendButton.addClickHandler(buttonHandler);
      claimantChatPanel.add(claimantChatSendButton);

      roleTabPanel.selectTab(HOME_TAB);

      /* flibber
       *  roleTabBar.setTabEnabled(CLAIM_TAB, false);
       *  roleTabBar.setTabEnabled(AUDIT_TAB, false);
       *  flibber */
      roleTabPanel.setSize("454px", "413px");
      roleTabPanel.addStyleName("table-center");
      this.rootPanel.add(roleTabPanel, 0, 37);

      // Initialize state.
      channel    = null;
      playerName = "";
      gameCode   = "";

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
      animateWaitTextBox(claimResourcesGrantTextBox);
      animateWaitTextBox(auditResourcesConsensusTextBox);
   }


   // Animate wait text box.
   private void animateWaitTextBox(TextBox textBox)
   {
      String text = textBox.getText();

      if (text.startsWith("waiting"))
      {
         if (text.equals("waiting..."))
         {
            textBox.setText("waiting");
         }
         else if (text.equals("waiting.."))
         {
            textBox.setText("waiting...");
         }
         else if (text.equals("waiting."))
         {
            textBox.setText("waiting..");
         }
         else if (text.equals("waiting"))
         {
            textBox.setText("waiting.");
         }
      }
   }


   // Button handler.
   private class ButtonHandler implements ClickHandler
   {
      public void onClick(ClickEvent event)
      {
         // Join/quit game.
         if (event.getSource() == playerJoinQuitButton)
         {
            playerName = playerNameTextBox.getText();
            if ((playerName == null) || (playerName = playerName.trim()).isEmpty())
            {
               Window.alert("Please enter name");
               playerName = "";
               return;
            }
            if (playerName.contains(DelimitedString.DELIMITER))
            {
               Window.alert("Invalid name character: " + DelimitedString.DELIMITER);
               playerName = "";
               return;
            }
            if (playerName.equals(Shared.ALL_PLAYERS))
            {
               Window.alert("Invalid name");
               playerName = "";
               return;
            }
            gameCode = gameCodeTextBox.getText();
            if ((gameCode == null) || (gameCode = gameCode.trim()).isEmpty())
            {
               Window.alert("Please enter game code");
               gameCode = "";
               return;
            }
            if (gameCode.contains(DelimitedString.DELIMITER))
            {
               Window.alert("Invalid game code character: " + DelimitedString.DELIMITER);
               gameCode = "";
               return;
            }
            lockUI();
            if (channel == null)
            {
               // Join.
               DelimitedString joinRequest = new DelimitedString(Shared.JOIN_GAME);
               joinRequest.add(gameCode);
               joinRequest.add(playerName);
               gameService.requestService(joinRequest.toString(),
                                          new AsyncCallback<String>()
                                          {
                                             public void onFailure(Throwable caught)
                                             {
                                                Window.alert("Error joining game: " + caught.getMessage());
                                                unlockUI();
                                             }

                                             public void onSuccess(String result)
                                             {
                                                if (Shared.isVoid(result))
                                                {
                                                   Window.alert("Cannot join game: void channel token data");
                                                }
                                                else
                                                {
                                                   if (Shared.isError(result))
                                                   {
                                                      Window.alert(result);
                                                   }
                                                   else
                                                   {
                                                      // Create server communication channel.
                                                      String[] args = new DelimitedString(result).parse();
                                                      if (args.length < 4)
                                                      {
                                                         Window.alert("Cannot join game: invalid channel token data: " + result);
                                                      }
                                                      else
                                                      {
                                                         String token = args[0];
                                                         for (int i = 1, j = args.length - 3; i < j; i++)
                                                         {
                                                            token = token + DelimitedString.DELIMITER + args[i];
                                                         }
                                                         ChannelFactory channelFactory = new ChannelFactoryImpl();
                                                         channel = channelFactory.createChannel(token);
                                                         if (channel == null)
                                                         {
                                                            Window.alert("Cannot join game: cannot create channel for token: " + token);
                                                         }
                                                         else
                                                         {
                                                            channelSocket = channel.open(new ChannelSocketListener());
                                                            playerJoinQuitButton.setText("Quit");
                                                            String personalResources = args[args.length - 3];
                                                            String commonResources = args[args.length - 2];
                                                            String entitledResources = args[args.length - 1];
                                                            showHomeResources(personalResources, commonResources, entitledResources);
                                                            Window.alert("Welcome!");
                                                         }
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
               // Quit.
               DelimitedString quitRequest = new DelimitedString(Shared.QUIT_GAME);
               quitRequest.add(gameCode);
               quitRequest.add(playerName);
               gameService.requestService(quitRequest.toString(),
                                          new AsyncCallback<String>()
                                          {
                                             public void onFailure(Throwable caught)
                                             {
                                                Window.alert("Error quitting game: " + caught.getMessage());
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
                                                      Window.alert("Error quitting game");
                                                   }
                                                }
                                                else
                                                {
                                                   if (channelSocket != null)
                                                   {
                                                      channelSocket.close();
                                                   }
                                                   channel = null;
                                                   playerJoinQuitButton.setText("Join");
                                                   clearHomeResources();
                                                   Window.alert("Goodbye!");
                                                }
                                                unlockUI();
                                             }
                                          }
                                          );
            }
         }

         // Host chat.
         else if (event.getSource() == hostChatClearButton)
         {
            hostChatTextArea.setText("");
         }
         else if (event.getSource() == hostChatSendButton)
         {
            String chatText = hostChatTextBox.getText();
            if (Shared.isVoid(chatText))
            {
               return;
            }
            if (channel == null)
            {
               Window.alert("Please create game!");
               return;
            }
            if (chatText.contains(DelimitedString.DELIMITER))
            {
               Window.alert("Invalid character: " + DelimitedString.DELIMITER);
               return;
            }
            if (channel == null)
            {
               Window.alert("Please join game!");
               return;
            }
            lockUI();
            DelimitedString chatRequest = new DelimitedString(Shared.HOST_CHAT);
            chatRequest.add(gameCode);
            chatRequest.add(playerName);
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
                                                hostChatTextArea.setText(hostChatTextArea.getText() +
                                                                         playerName + ": " +
                                                                         hostChatTextBox.getText() + "\n");
                                                hostChatTextBox.setText("");
                                             }
                                             unlockUI();
                                          }
                                       }
                                       );
         }
         else if (event.getSource() == claimDistributionTestButton)
         {
            if (claimDistribution == null)
            {
               Window.alert("Resource entitlement probability unavailable");
               return;
            }
            String valueText = claimDistributionTestValueTextBox.getText();
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
            double       probability   = claimDistribution.phi(value);
            NumberFormat decimalFormat = NumberFormat.getFormat(".##");
            claimDistributionTestProbabilityTextBox.setText(decimalFormat.format(probability));
         }
         else if (event.getSource() == auditDistributionTestButton)
         {
            if (auditDistribution == null)
            {
               Window.alert("Resource entitlement probability unavailable");
               return;
            }
            String valueText = auditDistributionTestValueTextBox.getText();
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
            double       probability   = auditDistribution.phi(value);
            NumberFormat decimalFormat = NumberFormat.getFormat(".##");
            auditDistributionTestProbabilityTextBox.setText(decimalFormat.format(probability));
         }
      }
   }

   private void clearHomeResources()
   {
      homeResourcesActualTextBox.setText("");
      homeResourcesPersonalTextBox.setText("");
      homeResourcesCommonTextBox.setText("");
      homeResourcesEntitledTextBox.setText("");
   }


   private void showHomeResources(String personal, String common, String entitled)
   {
      double actual = Double.parseDouble(personal) + Double.parseDouble(common);

      NumberFormat decimalFormat = NumberFormat.getFormat(".##");

      homeResourcesActualTextBox.setText(decimalFormat.format(actual));
      homeResourcesPersonalTextBox.setText(personal);
      homeResourcesCommonTextBox.setText(common);
      homeResourcesEntitledTextBox.setText(entitled);
   }


   private void showHomeResources(double personal, double common, double entitled)
   {
      double       actual        = personal + common;
      NumberFormat decimalFormat = NumberFormat.getFormat(".##");

      homeResourcesActualTextBox.setText(decimalFormat.format(actual));
      homeResourcesPersonalTextBox.setText(decimalFormat.format(personal));
      homeResourcesCommonTextBox.setText(decimalFormat.format(common));
      homeResourcesEntitledTextBox.setText(decimalFormat.format(entitled));
   }


   private void lockUI()
   {
      playerNameTextBox.setReadOnly(true);
      playerJoinQuitButton.setEnabled(false);
      gameCodeTextBox.setReadOnly(true);
      hostChatTextBox.setReadOnly(true);
      hostChatSendButton.setEnabled(false);
      claimResourcesClaimTextBox.setReadOnly(true);
      claimSetButton.setEnabled(false);
      claimResourcesDonateTextBox.setReadOnly(true);
      claimResourcesDonateNameListBox.setEnabled(false);
      claimResourcesDonateButton.setEnabled(false);
      claimFinishButton.setEnabled(false);
      auditorChatTextBox.setReadOnly(true);
      auditorChatSendButton.setEnabled(false);
      auditResourcesGrantTextBox.setReadOnly(true);
      auditResourcesGrantSetButton.setEnabled(false);
      auditFinishButton.setEnabled(false);
      claimantChatTextBox.setReadOnly(true);
      claimantChatSendButton.setEnabled(false);
   }


   private void unlockUI()
   {
      playerJoinQuitButton.setEnabled(true);
      if (channel == null)
      {
         playerNameTextBox.setReadOnly(false);
         gameCodeTextBox.setReadOnly(false);
      }
      else
      {
         hostChatTextBox.setReadOnly(false);
         hostChatSendButton.setEnabled(true);
         claimResourcesClaimTextBox.setReadOnly(false);
         claimSetButton.setEnabled(true);
         claimResourcesDonateTextBox.setReadOnly(false);
         claimResourcesDonateNameListBox.setEnabled(true);
         claimResourcesDonateButton.setEnabled(true);
         claimFinishButton.setEnabled(true);
         auditorChatTextBox.setReadOnly(false);
         auditorChatSendButton.setEnabled(true);
         auditResourcesGrantTextBox.setReadOnly(false);
         auditResourcesGrantSetButton.setEnabled(true);
         auditFinishButton.setEnabled(true);
         claimantChatTextBox.setReadOnly(false);
         claimantChatSendButton.setEnabled(true);
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
         if (operation.equals(Shared.QUIT_GAME) && (args.length == 2))
         {
            // Forced quit.
            lockUI();
            if (channelSocket != null)
            {
               channelSocket.close();
            }
            channel = null;
            playerJoinQuitButton.setText("Join");
            clearHomeResources();
            Window.alert(args[1]);
            unlockUI();
         }
         else if (operation.equals(Shared.SET_PLAYER_RESOURCES) &&
                  (args.length == 4))
         {
            // Set player resources.
            double personalResources = Double.parseDouble(args[1]);
            double commonResources   = Double.parseDouble(args[2]);
            double entitledResources = Double.parseDouble(args[3]);
            showHomeResources(personalResources, commonResources, entitledResources);
         }
         else if (operation.equals(Shared.PLAYER_CHAT) &&
                  ((args.length == 3) || (args.length == 4)))
         {
            // Player chat.
            if (args.length == 3)
            {
               String chatText = args[2];
               hostChatTextArea.setText(
                  hostChatTextArea.getText() + "host: " + chatText + "\n");
            }
            else
            {
               String chatText = args[3];
               hostChatTextArea.setText(
                  hostChatTextArea.getText() + "host: " + chatText + "\n");
            }
         }
      }


      @Override
      public void onError(ChannelError error)
      {
         Window.alert("Channel error: " + error.getCode() +
                      " : " + error.getDescription());
      }


      @Override
      public void onClose()
      {
      }
   }
}
