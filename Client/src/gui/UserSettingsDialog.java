/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import javax.swing.JOptionPane;

/**
 * Dialog for editing the server email settings
 * 
 * @author Frank Weber
 */
public class UserSettingsDialog extends javax.swing.JDialog {

	private boolean pressedOK = false;
	private final String username;
	private String password;

	/**
	 * Creates new form EmailSettingsDialog
	 */
	public UserSettingsDialog(java.awt.Frame parent, boolean modal, String username, String password) {
		super(parent, modal);
		initComponents();
		setLocationRelativeTo(null);
		this.username = username;
		this.password = password;
		lbUsername.setText(username);
	}

	public boolean pressedOK() {
		return pressedOK;
	}

	public String getNewPassword() {
		return password;
	}

	private void initComponents() {
		java.awt.GridBagConstraints gridBagConstraints;

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("User settings");

		pMain.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 8, 8, 8));
		pMain.setLayout(new java.awt.GridBagLayout());

		lbChangeUsername.setText("Change password for user:");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
		gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
		pMain.add(lbChangeUsername, gridBagConstraints);

		lbOldPassword.setText("Old password:");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
		gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
		pMain.add(lbOldPassword, gridBagConstraints);

		tfOldPassword.setColumns(15);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
		pMain.add(tfOldPassword, gridBagConstraints);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
		pMain.add(lbUsername, gridBagConstraints);

		lbNewPassword.setText("New password:");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
		gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
		pMain.add(lbNewPassword, gridBagConstraints);

		tfNewPassword.setColumns(15);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
		pMain.add(tfNewPassword, gridBagConstraints);

		tfRetypePassword.setColumns(15);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
		pMain.add(tfRetypePassword, gridBagConstraints);

		lbRetypePassword.setText("Retype password:");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
		gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
		pMain.add(lbRetypePassword, gridBagConstraints);

		btOkay.setText("Change Password");
		btOkay.setFocusPainted(false);
		btOkay.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				onOkay(evt);
			}
		});
		pButtons.add(btOkay);

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
		gridBagConstraints.gridy = 6;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.insets = new java.awt.Insets(4, 4, 4, 4);
		pMain.add(pButtons, gridBagConstraints);

		getContentPane().add(pMain, java.awt.BorderLayout.CENTER);

		pack();
	}

	private void onOkay(java.awt.event.ActionEvent evt) {
		try {
			System.out.println("Old: " + password);
			if (tfOldPassword.getPassword().equals(password)) {
				// TODO Password regex validation
				if (tfNewPassword.getPassword().equals(tfRetypePassword.getPassword())) {
					password = tfNewPassword.getPassword().toString();
				}
			}
			pressedOK = true;
			dispose();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void onCancel(java.awt.event.ActionEvent evt) {
		dispose();
	}

	// GUI Variables declaration
	private javax.swing.JButton btCancel = new javax.swing.JButton();
	private javax.swing.JButton btOkay = new javax.swing.JButton();
	private javax.swing.JLabel lbChangeUsername = new javax.swing.JLabel();
	private javax.swing.JLabel lbNewPassword = new javax.swing.JLabel();
	private javax.swing.JLabel lbOldPassword = new javax.swing.JLabel();
	private javax.swing.JLabel lbRetypePassword = new javax.swing.JLabel();
	private javax.swing.JLabel lbUsername = new javax.swing.JLabel();
	private javax.swing.JPanel pButtons = new javax.swing.JPanel();
	private javax.swing.JPanel pMain = new javax.swing.JPanel();
	private javax.swing.JPasswordField tfNewPassword = new javax.swing.JPasswordField();
	private javax.swing.JPasswordField tfOldPassword = new javax.swing.JPasswordField();
	private javax.swing.JPasswordField tfRetypePassword = new javax.swing.JPasswordField();
	// End of variables declaration
}
