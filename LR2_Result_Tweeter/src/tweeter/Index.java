package tweeter;

import java.awt.BorderLayout;
import java.awt.event.*;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.FlowLayout;

import javax.swing.JLabel;

import java.awt.GridLayout;
import java.awt.Desktop;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JTextField;
import javax.swing.JButton;

import twitter4j.*;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;

import java.awt.Font;

public class Index extends JFrame implements ActionListener {

	JPanel contentPane;
	JTextField Folder;
	JTextField Header;
	JTextField Footer;
	File folderName;
	File token = new File("token");
	File config = new File("config.cfg");
	JButton choose = new JButton("選択");
	JButton auth;
	JButton activate = new JButton("実行");

	String[] combobox = { "RED BELT(1P SQUARE)", "RED BELT(2P SQUARE)",
			"RED BELT(1P WIDE)", "RED BELT(2P WIDE)" };
	JComboBox skin = new JComboBox(combobox);

	JLabel account = new JLabel("認証されていません");
	tweet tw = new tweet();

	boolean run = false;
	boolean loading = false;
	boolean authentication = false;
	boolean choosefolder = false;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		Index frame = new Index();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	/**
	 * Create the frame.
	 */
	public Index() {
		setTitle("LR2 Result Tweeter");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 320, 256);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		ImageIcon icon = new ImageIcon("icon.png");
		setIconImage(icon.getImage());

		JLabel label = new JLabel("アカウント名:");
		label.setFont(new Font("ＭＳ Ｐゴシック", Font.PLAIN, 12));
		label.setBounds(12, 10, 74, 13);
		contentPane.add(label);

		account.setFont(new Font("ＭＳ Ｐゴシック", Font.PLAIN, 12));
		account.setBounds(98, 10, 123, 13);
		contentPane.add(account);

		JLabel lblNewLabel_1 = new JLabel("LR2フォルダ:");
		lblNewLabel_1.setFont(new Font("ＭＳ Ｐゴシック", Font.PLAIN, 12));
		lblNewLabel_1.setBounds(12, 33, 74, 13);
		contentPane.add(lblNewLabel_1);

		Folder = new JTextField();
		Folder.setEditable(false);
		Folder.setFont(new Font("ＭＳ Ｐゴシック", Font.PLAIN, 12));
		Folder.setBounds(98, 30, 123, 19);
		contentPane.add(Folder);
		Folder.setColumns(10);

		choose.setFont(new Font("ＭＳ Ｐゴシック", Font.PLAIN, 12));
		choose.setBounds(233, 29, 59, 21);
		contentPane.add(choose);
		choose.addActionListener(this);
		choose.setActionCommand("choose");

		auth = new JButton("認証");
		auth.setFont(new Font("ＭＳ Ｐゴシック", Font.PLAIN, 12));
		auth.setBounds(233, 6, 59, 21);
		contentPane.add(auth);
		auth.addActionListener(this);
		auth.setActionCommand("auth");

		JLabel label_1 = new JLabel("ヘッダー:");
		label_1.setFont(new Font("ＭＳ Ｐゴシック", Font.PLAIN, 12));
		label_1.setBounds(12, 56, 50, 13);
		contentPane.add(label_1);

		Header = new JTextField();
		Header.setFont(new Font("ＭＳ Ｐゴシック", Font.PLAIN, 12));
		Header.setBounds(98, 53, 123, 19);
		contentPane.add(Header);
		Header.setColumns(10);

		JLabel label_2 = new JLabel("フッター:");
		label_2.setFont(new Font("ＭＳ Ｐゴシック", Font.PLAIN, 12));
		label_2.setBounds(12, 79, 50, 13);
		contentPane.add(label_2);

		Footer = new JTextField();
		Footer.setFont(new Font("ＭＳ Ｐゴシック", Font.PLAIN, 12));
		Footer.setText("#LR2result");
		Footer.setBounds(98, 76, 123, 19);
		contentPane.add(Footer);
		Footer.setColumns(10);

		JCheckBox scanning = new JCheckBox("数値読み込み（開発中）");
		scanning.setFont(new Font("ＭＳ Ｐゴシック", Font.PLAIN, 12));
		scanning.setEnabled(false);
		scanning.setBounds(8, 98, 103, 21);
		contentPane.add(scanning);

		JLabel label_3 = new JLabel("スキン:");
		label_3.setFont(new Font("ＭＳ Ｐゴシック", Font.PLAIN, 12));
		label_3.setEnabled(false);
		label_3.setBounds(12, 125, 50, 13);
		contentPane.add(label_3);

		skin.setEnabled(false);
		skin.setFont(new Font("ＭＳ Ｐゴシック", Font.PLAIN, 12));
		skin.setBounds(98, 125, 123, 19);
		contentPane.add(skin);

		activate.setFont(new Font("ＭＳ Ｐゴシック", Font.PLAIN, 12));
		activate.setEnabled(false);
		activate.setBounds(12, 148, 280, 60);
		contentPane.add(activate);
		activate.addActionListener(this);
		activate.setActionCommand("activate");

		if (loadAccessToken() != null) {
			tw.authorization(loadAccessToken());
			loading = true;
			try {
				account.setText(tw.twitter.getScreenName());
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (TwitterException e) {
				e.printStackTrace();
			}
			authentication = true;
			choosefolder = true;
			activate.setEnabled(true);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("auth")) {
			System.out.println("push auth key.");
			tw.TwitterAuth();
			String value = JOptionPane.showInputDialog(this, "PIN番号を入力して下さい");
			if (value == null) {
			} else {
				tw.authorization(value);
				try {
					account.setText(tw.twitter.getScreenName());
					authentication = true;
					if (choosefolder)
						activate.setEnabled(true);
				} catch (TwitterException e1) {
					e1.printStackTrace();
				}
			}
		} else if (e.getActionCommand().equals("choose")) {
			System.out.println("push choose key.");
			JFileChooser filechooser = new JFileChooser("C:\\");
			filechooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int selected = filechooser.showSaveDialog(this);
			if (selected == JFileChooser.APPROVE_OPTION) {
				folderName = filechooser.getSelectedFile();
				Folder.setText(folderName.getAbsolutePath());
				choosefolder = true;
				if (authentication)
					activate.setEnabled(true);
			}
			if (!new File(Folder.getText() + "\\LR2body.exe").exists()) {
				JOptionPane.showMessageDialog(this,
						"LR2body.exeが見つかりません。\n選択しなおして下さい。", "Warn",
						JOptionPane.WARNING_MESSAGE);
				Folder.setText("");
			}
		} else if (e.getActionCommand().equals("activate")) {
			System.out.println("push activate key.");
			if (!run) {
				storeAccessToken(tw.accessToken);
				activate();
			} else {
				terminate();
			}
		}

	}

	public void activate() {
		Folder.setEnabled(false);
		auth.setEnabled(false);
		choose.setEnabled(false);
		activate.setText("終了");
		run = true;
		Crawler c = new Crawler(Header.getText(), Footer.getText(), folderName,
				tw);
		c.start();
	}

	public void terminate() {
		System.exit(0);
	}

	private AccessToken loadAccessToken() {
		ObjectInputStream is = null;
		try {
			is = new ObjectInputStream(new FileInputStream(token));
			AccessToken accessToken = (AccessToken) is.readObject();
			return accessToken;
		} catch (IOException e) {
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			try {
				BufferedReader br = new BufferedReader(new FileReader(config));
				Folder.setText(br.readLine());
				br.close();
			} catch (FileNotFoundException e) {
				System.out.println("初回の起動です。");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void storeAccessToken(AccessToken accessToken) {
		ObjectOutputStream os = null;
		try {
			if (!loading) {
				os = new ObjectOutputStream(new FileOutputStream(token));
				os.writeObject(accessToken);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			try {
				BufferedWriter bw = new BufferedWriter(new FileWriter(config));
				bw.write(Folder.getText());
				bw.newLine();
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
