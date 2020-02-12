/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;

import data.SearchBot;
import data.SearchBots;

/**
 *
 * @author Frank Weber
 */
public class Willhaben_Searchbot_Client extends javax.swing.JFrame {

	public String serverAddress = null;
	public int serverPort = 50519;
	// The searchbots of the user to show in table
	private SearchBots searchBots = new SearchBots();
	// The Email settings of the user
	private Properties emailProps = new Properties();

	/**
	 * Creates new form Willhaben_Searchbot_Client
	 */
	public Willhaben_Searchbot_Client() {
		initComponents();
		setLocationRelativeTo(null);
		searchTable.setModel(new SearchTableModel(searchBots));

		DefaultTableCellRenderer renderer = (DefaultTableCellRenderer) searchTable.getDefaultRenderer(Object.class);
		renderer.setHorizontalAlignment(SwingConstants.CENTER);
	}

	public static String sendRequestAndReceiveResponse(String url, int port, String request, String codierung)
			throws Exception {
		String answer = "";

		final InetAddress host = InetAddress.getByName(url);
		// System.out.println("URL: "+url);
		// System.out.println("IP: "+host.getHostAddress());

		try (final Socket socket = new Socket(host, port)) {
			// System.out.format("Verbindung zum Host %s:%d
			// aufgebaut!%n",host.getHostAddress(),port);

			final BufferedWriter writer = new BufferedWriter(
					new OutputStreamWriter(socket.getOutputStream(), codierung));
			writer.write(request);
			writer.flush();
			socket.shutdownOutput();

			final BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), codierung));

			final StringBuilder sb = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line).append("\n");
			}
			answer = sb.toString();
		}
		// System.out.println("Verbindung geschlossen");
		// socket.close();

		/*
		 * final InetAddress test = InetAddress.getByAddress(new
		 * byte[]{95,(byte)143,(byte)172,(byte)237});
		 * System.out.println("URL: "+test.getHostName());
		 */
		// System.out.println("Antwort: \n"+answer);
		return answer;
	}

	/**
	 * Initialize all GUI components
	 */
	private void initComponents() {

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setTitle("Willhaben Searchbot");

		lbStatus.setText("Server connection:");
		lbStatus.setHorizontalTextPosition(JLabel.LEFT);
		try {
			lbStatus.setIcon(new ImageIcon(ImageIO.read(getClass().getResource("/media/red_connection.png"))));
		} catch (IOException e) {
			e.printStackTrace();
		}

		pStatus.setLayout(new java.awt.BorderLayout());
		pStatus.add(new JLabel("by Frank Weber & Philipp Ensinger"), java.awt.BorderLayout.EAST);
		pStatus.add(lbStatus, java.awt.BorderLayout.WEST);

		pMain.setLayout(new java.awt.BorderLayout());

		searchTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jScrollPane1.setViewportView(searchTable);

		pMain.add(jScrollPane1, java.awt.BorderLayout.CENTER);
		pMain.add(pStatus, java.awt.BorderLayout.SOUTH);

		getContentPane().add(pMain, java.awt.BorderLayout.CENTER);

		menuSearchbot.setText("Searchbot");

		menuAddBot.setText("Add Searchbot");
		menuAddBot.setEnabled(false);
		menuAddBot.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				onAddSearchbot(evt);
			}
		});
		menuSearchbot.add(menuAddBot);

		menuRemoveBot.setText("Remove Searchbot");
		menuRemoveBot.setEnabled(false);
		menuRemoveBot.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				onRemoveSearchbot(evt);
			}
		});
		menuSearchbot.add(menuRemoveBot);

		menuEditBot.setText("Edit Searchbot");
		menuEditBot.setEnabled(false);
		menuEditBot.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				onEditSearchbot(evt);
			}
		});
		menuSearchbot.add(menuEditBot);

		jMenuBar1.add(menuSearchbot);

		menuServer.setText("Server");

		menuConnect.setText("Connect");
		menuConnect.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				onConnect(evt);
			}
		});
		menuServer.add(menuConnect);

		menuDisconnect.setText("Disconnect");
		menuDisconnect.setEnabled(false);
		menuDisconnect.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				onDisconnect(evt);
			}
		});
		menuServer.add(menuDisconnect);
		menuServer.add(jSeparator1);

		menuEmailSettings.setText("Email Settings");
		menuEmailSettings.setEnabled(false);
		menuEmailSettings.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				onEmailSettings(evt);
			}
		});
		menuServer.add(menuEmailSettings);

		menuUserSettings.setText("User Settings");
		menuUserSettings.setEnabled(false);
		menuUserSettings.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				onUserSettings(evt);
			}
		});
		menuServer.add(menuUserSettings);
		menuServer.add(jSeparator2);

		menuEditServer.setText("Edit server config");
		menuEditServer.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				onEditServerConfig(evt);
			}
		});
		menuServer.add(menuEditServer);

		jMenuBar1.add(menuServer);

		menuHelp.setText("Help");

		menuAbout.setText("About");
		menuAbout.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				onEditServerConfig(evt);
			}
		});
		menuHelp.add(menuAbout);

		menuManual.setText("Manual");
		menuManual.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				onEditServerConfig(evt);
			}
		});
		menuHelp.add(menuManual);

		jMenuBar1.add(menuHelp);

		setJMenuBar(jMenuBar1);

		pack();
	}

	/**
	 * Called from "add Searchbot" button
	 * 
	 * @param evt
	 */
	private void onAddSearchbot(java.awt.event.ActionEvent evt) {
		final AddSearchbotDialog dlg = new AddSearchbotDialog(this, true);
		dlg.setVisible(true);
		if (dlg.pressedOK()) {
			try {
				searchBots.addNew(new SearchBot(dlg.getBotname(), dlg.getLink(), dlg.getInterval(), dlg.getEmails()));
				searchTable.setModel(new SearchTableModel(searchBots));
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	/**
	 * Called from "connect" button builds the connection to the server and shows
	 * existing searchbots for user
	 * 
	 * @param evt
	 */
	private void onConnect(java.awt.event.ActionEvent evt) {
		System.out.println(serverAddress);
		if (serverAddress != null && serverPort != 0) {
			try {
				// TODO server connection
				// final String answer = sendRequestAndReceiveResponse(serverAddress,
				// serverPort, "Hallo Server", "utf8");
				// System.out.println("Antwort: \n" + answer);
				menuConnect.setEnabled(false);
				menuDisconnect.setEnabled(true);
				menuAddBot.setEnabled(true);
				menuRemoveBot.setEnabled(true);
				menuEditBot.setEnabled(true);
				menuEditServer.setEnabled(false);
				menuEmailSettings.setEnabled(true);
				menuUserSettings.setEnabled(true);

				try {
					lbStatus.setIcon(
							new ImageIcon(ImageIO.read(getClass().getResource("/media/green_connection.png"))));
				} catch (IOException e) {
					e.printStackTrace();
				}

				// TODO Load configs/bots from server

			} catch (Exception e) {
				JOptionPane.showMessageDialog(this, "Can't connect to server: \n\n" + e, "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		} else {
			JOptionPane.showMessageDialog(this, "There is no server defined!", "No server address",
					JOptionPane.INFORMATION_MESSAGE);
			serverAddress = "127.0.0.1";
			onEditServerConfig(null);
			if (!serverAddress.equals("127.0.0.1")) {
				onConnect(null);
			}
		}
	}

	/**
	 * Called from "edit Server config" button manage the configuration for the
	 * server
	 * 
	 * @param evt
	 */
	private void onEditServerConfig(java.awt.event.ActionEvent evt) {
		final EditServerDialog dlg = new EditServerDialog(this, true);
		dlg.setIP(serverAddress);
		dlg.setPort(serverPort);
		dlg.setVisible(true);
		if (dlg.pressedOK()) {
			serverAddress = dlg.getIpAddress();
			serverPort = dlg.getPort();
		}
		// TODO save local settings
	}

	/**
	 * Called from "disconnect" button disconnects from server
	 * 
	 * @param evt
	 */
	private void onDisconnect(java.awt.event.ActionEvent evt) {
		menuConnect.setEnabled(true);
		menuDisconnect.setEnabled(false);
		menuAddBot.setEnabled(false);
		menuRemoveBot.setEnabled(false);
		menuEditBot.setEnabled(false);
		menuEditServer.setEnabled(true);
		menuEmailSettings.setEnabled(false);
		menuUserSettings.setEnabled(false);
		try {
			lbStatus.setIcon(new ImageIcon(ImageIO.read(getClass().getResource("/media/red_connection.png"))));
		} catch (IOException e) {
			e.printStackTrace();
		}
		// TODO Remove searchbots
	}

	/**
	 * Called from "edit Searchbot" button edit the selected searchbot
	 * 
	 * @param evt
	 */
	private void onEditSearchbot(java.awt.event.ActionEvent evt) {
		try {
			if (searchTable.getSelectedColumnCount() < 1) {
				throw new Exception("Please select a searchbot first!");
			}
			SearchBot searchBot = searchBots.get(searchTable.getSelectedRow());
			final EditSearchbotDialog dlg = new EditSearchbotDialog(this, true, searchBot.getName(),
					searchBot.getLink(), searchBot.getInterval(), searchBot.getEmails());
			dlg.setVisible(true);
			if (dlg.pressedOK()) {
				searchBots.set(searchTable.getSelectedRow(),
						new SearchBot(dlg.getBotname(), dlg.getLink(), dlg.getInterval(), dlg.getEmails()));
				searchTable.setModel(new SearchTableModel(searchBots));
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Could not edit Searchbot!\n\n" + ex.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
		// TODO send information to server

	}

	/**
	 * Called from "remove Searchbot" button removes the selected searchbot from
	 * table
	 * 
	 * @param evt
	 */
	private void onRemoveSearchbot(java.awt.event.ActionEvent evt) {
		try {
			if (searchTable.getSelectedColumnCount() < 1) {
				throw new Exception("Please select a searchbot first!");
			}
			if (JOptionPane.showConfirmDialog(this,
					"Do you really want to remove the Searchbot \""
							+ searchBots.get(searchTable.getSelectedRow()).getName() + "\"?",
					"Remove?", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
				searchBots.remove(searchTable.getSelectedRow());
				searchTable.setModel(new SearchTableModel(searchBots));
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Could not remove Searchbot!\n\n" + ex.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
		// TODO send information to server
	}

	/**
	 * Called from "Email Settings" button
	 * 
	 * @param evt
	 */
	private void onEmailSettings(java.awt.event.ActionEvent evt) {
		final EmailSettingsDialog dlg = new EmailSettingsDialog(this, true);
		dlg.setProperties(emailProps);
		dlg.setVisible(true);
		if (dlg.pressedOK()) {
			emailProps = dlg.getEmailProperties();
		}
		// TODO send settings to server
	}

	/**
	 * Called from "User Settings" button To change the users password
	 * 
	 * @param evt
	 */
	private void onUserSettings(java.awt.event.ActionEvent evt) {
		final UserSettingsDialog dlg = new UserSettingsDialog(this, true, "user1", "12345");
		dlg.setVisible(true);
		if (dlg.pressedOK()) {
			System.out.println(dlg.getNewPassword());
		}
		// TODO send new password to server
	}

	/**
	 * @param args the command line arguments
	 */
	public static void main(String args[]) {
		/* Set the Nimbus look and feel */
		// <editor-fold defaultstate="collapsed" desc=" Look and feel setting code
		// (optional) ">
		/*
		 * If Nimbus (introduced in Java SE 6) is not available, stay with the default
		 * look and feel. For details see
		 * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
		 */
		try {
			Properties props = new Properties();
			props.put("logoString", "");
			com.jtattoo.plaf.aluminium.AluminiumLookAndFeel.setCurrentTheme(props);
			UIManager.setLookAndFeel("com.jtattoo.plaf.aluminium.AluminiumLookAndFeel");
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(Willhaben_Searchbot_Client.class.getName())
					.log(java.util.logging.Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(Willhaben_Searchbot_Client.class.getName())
					.log(java.util.logging.Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(Willhaben_Searchbot_Client.class.getName())
					.log(java.util.logging.Level.SEVERE, null, ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(Willhaben_Searchbot_Client.class.getName())
					.log(java.util.logging.Level.SEVERE, null, ex);
		}

		/* Create and display the form */
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new Willhaben_Searchbot_Client().setVisible(true);
			}
		});
	}

	// GUI Variables declaration
	private javax.swing.JMenuBar jMenuBar1 = new javax.swing.JMenuBar();
	private javax.swing.JScrollPane jScrollPane1 = new javax.swing.JScrollPane();
	private javax.swing.JPopupMenu.Separator jSeparator1 = new javax.swing.JPopupMenu.Separator();
	private javax.swing.JPopupMenu.Separator jSeparator2 = new javax.swing.JPopupMenu.Separator();
	private javax.swing.JMenuItem menuAddBot = new javax.swing.JMenuItem();
	private javax.swing.JMenuItem menuConnect = new javax.swing.JMenuItem();
	private javax.swing.JMenuItem menuDisconnect = new javax.swing.JMenuItem();
	private javax.swing.JMenuItem menuEditBot = new javax.swing.JMenuItem();
	private javax.swing.JMenuItem menuEditServer = new javax.swing.JMenuItem();
	private javax.swing.JMenuItem menuEmailSettings = new javax.swing.JMenuItem();
	private javax.swing.JMenuItem menuRemoveBot = new javax.swing.JMenuItem();
	private javax.swing.JMenuItem menuAbout = new javax.swing.JMenuItem();
	private javax.swing.JMenuItem menuManual = new javax.swing.JMenuItem();
	private javax.swing.JMenu menuSearchbot = new javax.swing.JMenu();
	private javax.swing.JMenu menuServer = new javax.swing.JMenu();
	private javax.swing.JMenu menuHelp = new javax.swing.JMenu();
	private javax.swing.JMenuItem menuUserSettings = new javax.swing.JMenuItem();
	private javax.swing.JPanel pMain = new javax.swing.JPanel();
	private javax.swing.JTable searchTable = new javax.swing.JTable();
	private javax.swing.JPanel pStatus = new javax.swing.JPanel();
	private javax.swing.JLabel lbStatus = new javax.swing.JLabel();
	// End of variables declaration
}
