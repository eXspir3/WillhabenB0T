/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.util.ArrayList;

import javax.swing.JOptionPane;

/**
 * Dialog for adding a searchbot
 * 
 * @author Frank Weber
 */
public class AddSearchbotDialog extends javax.swing.JDialog {

	private String botname, link;
	private final ArrayList<String> emails = new ArrayList();
	private int interval;
	private boolean pressedOK = false;

	/**
	 * Creates new form AddSearchbotDialog
	 */
	public AddSearchbotDialog(java.awt.Frame parent, boolean modal) {
		super(parent, modal);
		initComponents();
		setLocationRelativeTo(null);
	}

	public boolean pressedOK() {
		return pressedOK;
	}

	public String getBotname() {
		return botname;
	}

	public String getLink() {
		return link;
	}

	public ArrayList<String> getEmails() {
		return emails;
	}

	public int getInterval() {
		return interval;
	}

	private void initComponents() {
		java.awt.GridBagConstraints gridBagConstraints;

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("Add Searchbot");

		pMain.setLayout(new java.awt.GridBagLayout());

		tfLink.setColumns(20);
		tfLink.setRows(5);
		jScrollPane1.setViewportView(tfLink);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.insets = new java.awt.Insets(9, 9, 9, 9);
		pMain.add(jScrollPane1, gridBagConstraints);

		tfBotname.setColumns(20);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.insets = new java.awt.Insets(9, 9, 9, 9);
		pMain.add(tfBotname, gridBagConstraints);

		lbBotname.setText("Botname:");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.insets = new java.awt.Insets(9, 9, 9, 9);
		pMain.add(lbBotname, gridBagConstraints);

		lbLink.setText("Searchinterval:");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.insets = new java.awt.Insets(9, 9, 9, 9);
		pMain.add(lbLink, gridBagConstraints);

		lbInterval.setText("Willhaben-Link:");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.insets = new java.awt.Insets(9, 9, 9, 9);
		pMain.add(lbInterval, gridBagConstraints);

		tfEmails.setColumns(20);
		tfEmails.setRows(5);
		jScrollPane2.setViewportView(tfEmails);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.insets = new java.awt.Insets(9, 9, 9, 9);
		pMain.add(jScrollPane2, gridBagConstraints);

		pEmailText.setLayout(new java.awt.GridLayout(2, 0));

		lbEmails.setText("Emails to send Notification:");
		pEmailText.add(lbEmails);

		jLabel1.setText("(separate with new line)");
		pEmailText.add(jLabel1);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.insets = new java.awt.Insets(9, 9, 9, 9);
		pMain.add(pEmailText, gridBagConstraints);

		spInterval.setModel(new javax.swing.SpinnerNumberModel(40, 40, 3600, 10));
		pInterval.add(spInterval);

		lbSeconds.setText("seconds");
		pInterval.add(lbSeconds);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.insets = new java.awt.Insets(9, 9, 9, 9);
		pMain.add(pInterval, gridBagConstraints);

		btAddBot.setText("Add Searchbot");
		btAddBot.setFocusPainted(false);
		btAddBot.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				onAddSearchbot(evt);
			}
		});
		pButtons.add(btAddBot);

		btCancel.setText("Cancel");
		btCancel.setFocusPainted(false);
		btCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				onCancel(evt);
			}
		});
		pButtons.add(btCancel);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.gridwidth = 2;
		pMain.add(pButtons, gridBagConstraints);

		getContentPane().add(pMain, java.awt.BorderLayout.CENTER);

		pack();
	}

	private void onCancel(java.awt.event.ActionEvent evt) {
		dispose();
	}

	private void onAddSearchbot(java.awt.event.ActionEvent evt) {
		try {
			if (tfBotname.getText().isEmpty()) {
				throw new Exception("Botname is empty");
			} else {
				botname = tfBotname.getText();
				if (tfLink.getText().isEmpty()) {
					throw new Exception("Link is empty");
				} else {
					link = tfLink.getText();
					if (tfEmails.getText().isEmpty()) {
						throw new Exception("Email is empty");
					} else {
						emails.add(tfEmails.getText());
					}
				}
			}
			interval = (Integer) spInterval.getValue();
			pressedOK = true;
			dispose();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	// GUI Variables declaration
	private javax.swing.JButton btAddBot = new javax.swing.JButton();
	private javax.swing.JButton btCancel = new javax.swing.JButton();
	private javax.swing.JLabel jLabel1 = new javax.swing.JLabel();
	private javax.swing.JScrollPane jScrollPane1 = new javax.swing.JScrollPane();
	private javax.swing.JScrollPane jScrollPane2 = new javax.swing.JScrollPane();
	private javax.swing.JLabel lbBotname = new javax.swing.JLabel();
	private javax.swing.JLabel lbEmails = new javax.swing.JLabel();
	private javax.swing.JLabel lbInterval = new javax.swing.JLabel();
	private javax.swing.JLabel lbLink = new javax.swing.JLabel();
	private javax.swing.JLabel lbSeconds = new javax.swing.JLabel();
	private javax.swing.JPanel pButtons = new javax.swing.JPanel();
	private javax.swing.JPanel pEmailText = new javax.swing.JPanel();
	private javax.swing.JPanel pInterval = new javax.swing.JPanel();
	private javax.swing.JPanel pMain = new javax.swing.JPanel();
	private javax.swing.JSpinner spInterval = new javax.swing.JSpinner();
	private javax.swing.JTextField tfBotname = new javax.swing.JTextField();
	private javax.swing.JTextArea tfEmails = new javax.swing.JTextArea();
	private javax.swing.JTextArea tfLink = new javax.swing.JTextArea();
	// End of variables declaration
}
