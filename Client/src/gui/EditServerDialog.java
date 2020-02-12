/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import javax.swing.JOptionPane;

/**
 * Dialog for editing the server configuration
 * 
 * @author Frank Weber
 */
public class EditServerDialog extends javax.swing.JDialog {

	private JIpTextField ipInputField;
	private boolean pressedOK = false;
	private String serverIpAddress;

	/**
	 * Creates new form editServerConfig
	 */
	public EditServerDialog(java.awt.Frame parent, boolean modal) {
		super(parent, modal);
		initComponents();
		setLocationRelativeTo(null);
		tfPort.setValue(new Integer(50519));
	}

	public boolean pressedOK() {
		return pressedOK;
	}

	public String getIpAddress() {
		return tfIP.getText();
	}

	public int getPort() {
		return ((Number) tfPort.getValue()).intValue();
	}

	public void setIP(String ip) {
		tfIP.setText(ip);
	}

	public void setPort(int port) {
		tfPort.setValue(port);
	}

	private void initComponents() {

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("Server Configuration");

		pMain.setLayout(new java.awt.GridLayout(2, 0));

		lbIpAddress.setText("Server IP Address:");
		pIPAddress.add(lbIpAddress);

		tfIP = new JIpTextField();
		pIPAddress.add(tfIP);

		pMain.add(pIPAddress);

		lbPort.setText("Server Port:");
		pPort.add(lbPort);

		tfPort.setColumns(5);
		tfPort.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(
				new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("####0"))));
		pPort.add(tfPort);

		pMain.add(pPort);

		getContentPane().add(pMain, java.awt.BorderLayout.CENTER);

		btOK.setText("OK");
		btOK.setFocusPainted(false);
		btOK.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				onBT(evt);
			}
		});
		pButton.add(btOK);

		btCancel.setText("Cancel");
		btCancel.setFocusPainted(false);
		btCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				onCancel(evt);
			}
		});
		pButton.add(btCancel);

		getContentPane().add(pButton, java.awt.BorderLayout.SOUTH);

		pack();
	}

	private void onBT(java.awt.event.ActionEvent evt) {
		if (((Number) tfPort.getValue()).intValue() < 65535) {
			pressedOK = true;
			dispose();
		} else {
			JOptionPane.showMessageDialog(this, "Invalid Port!", "Error", JOptionPane.ERROR_MESSAGE);
			tfPort.setValue(50519);
		}
	}

	private void onCancel(java.awt.event.ActionEvent evt) {
		dispose();
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
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(EditServerDialog.class.getName()).log(java.util.logging.Level.SEVERE,
					null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(EditServerDialog.class.getName()).log(java.util.logging.Level.SEVERE,
					null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(EditServerDialog.class.getName()).log(java.util.logging.Level.SEVERE,
					null, ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(EditServerDialog.class.getName()).log(java.util.logging.Level.SEVERE,
					null, ex);
		}
		// </editor-fold>
		// </editor-fold>
		// </editor-fold>
		// </editor-fold>

		/* Create and display the dialog */
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				EditServerDialog dialog = new EditServerDialog(new javax.swing.JFrame(), true);
				dialog.addWindowListener(new java.awt.event.WindowAdapter() {
					@Override
					public void windowClosing(java.awt.event.WindowEvent e) {
						System.exit(0);
					}
				});
				dialog.setVisible(true);
			}
		});
	}

	// GUI Variables declaration
	private javax.swing.JButton btCancel = new javax.swing.JButton();
	private javax.swing.JButton btOK = new javax.swing.JButton();
	private javax.swing.JLabel lbIpAddress = new javax.swing.JLabel();
	private javax.swing.JLabel lbPort = new javax.swing.JLabel();
	private javax.swing.JPanel pButton = new javax.swing.JPanel();
	private javax.swing.JPanel pIPAddress = new javax.swing.JPanel();
	private javax.swing.JPanel pMain = new javax.swing.JPanel();
	private javax.swing.JPanel pPort = new javax.swing.JPanel();
	private javax.swing.JTextField tfIP = new javax.swing.JTextField();
	private javax.swing.JFormattedTextField tfPort = new javax.swing.JFormattedTextField();
	// End of variables declaration
}
