package kr.or.dgit.erp.main;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import kr.or.dgit.erp.setting.InitSettingService;

import java.awt.GridLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class SettingUI extends JFrame implements ActionListener {
	private JButton btnInit;
	private JButton btnBackup;
	private JButton btnRestore;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SettingUI settingUI = new SettingUI();
					settingUI.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public SettingUI() {
		setTitle("DB관리메뉴");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 535, 135);
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(1, 0, 20, 0));
		
		btnInit = new JButton("초기화");
		btnInit.addActionListener(this);
		contentPane.add(btnInit);
		
		btnBackup = new JButton("백업");
		btnBackup.addActionListener(this);
		contentPane.add(btnBackup);
		
		btnRestore = new JButton("복원");
		btnRestore.addActionListener(this);
		contentPane.add(btnRestore);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnRestore) {
			actionPerformedBtnRestore(e);
		}
		if (e.getSource() == btnBackup) {
			actionPerformedBtnBackup(e);
		}
		if (e.getSource() == btnInit) {
			actionPerformedBtnInit(e);
		}
	}
	protected void actionPerformedBtnInit(ActionEvent e) {		
		InitSettingService init = new InitSettingService();
		init.initSetting();	//초기화
	}
	protected void actionPerformedBtnBackup(ActionEvent e) {
		InitSettingService init = new InitSettingService();
		init.backUp();		//백업
	}
	protected void actionPerformedBtnRestore(ActionEvent e) {
		InitSettingService init = new InitSettingService();
		init.restore();		//복원
	}
}
