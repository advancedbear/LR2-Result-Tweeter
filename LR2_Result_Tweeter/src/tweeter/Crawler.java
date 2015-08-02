package tweeter;

import java.io.File;

public class Crawler extends Thread {
	
	File file;
	File[] files;
	File uploadingfile;
	String Header;
	String Footer;
	tweet tw;

	public Crawler(String h, String f, File u, tweet t) {
		Header = h;
		Footer = f;
		file = u;
		files = u.listFiles();
		tw = t;
		uploadingfile = files[0];
	}

	public void run() {
		while (true) {
			try {
				File[] files2 = file.listFiles();
				if (files.length != files2.length) {
					for (int n = 1; n < files2.length; n++) {
						if (uploadingfile.lastModified() < files2[n].lastModified()) {
							uploadingfile = files2[n];
						}
					}
					tw.tweetImage(Header, Footer, uploadingfile);
					files = files2;
					uploadingfile = files[0];
				}
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
