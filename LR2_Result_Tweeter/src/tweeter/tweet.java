package tweeter;

import twitter4j.*;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class tweet {
	Twitter twitter;
	AccessToken accessToken = null;
	RequestToken requestToken;

	public tweet() {
		//String consumerKey = "RcXkwUIEHoO7wQot0Tvo7deRq";
		//String consumerSecret = "mC0muB8bBHdXdZyrpdxAMm851esHc5bSgNq1zVgV0rEf4Gj5Kw";

		twitter = TwitterFactory.getSingleton();
		//twitter.setOAuthConsumer(consumerKey, consumerSecret);
		twitter.setOAuthAccessToken(accessToken);
	}

	public void TwitterAuth() {
		try {

			Desktop desktop = Desktop.getDesktop();
			requestToken = twitter.getOAuthRequestToken();
			URI uri = new URI(requestToken.getAuthorizationURL());
			desktop.browse(uri);

		} catch (TwitterException e) {
			System.out.println("Error");
		} catch (URISyntaxException e) {
			System.out.println("Error");
		} catch (IOException e) {
			System.out.println("Error");
		}
	}

	public void authorization(String pin) {
		try {
			accessToken = twitter.getOAuthAccessToken(requestToken, pin);
			twitter.setOAuthAccessToken(accessToken);
		} catch (TwitterException e) {
			System.out.println("Authorization Error!");
		}
	}

	public void authorization(AccessToken token) {
		twitter.setOAuthAccessToken(token);
	}

	public void tweetImage(String h, String f, File u) {
		try {
			twitter.updateStatus(new StatusUpdate("" + h + "\n" + f + "").media(u));
		} catch (TwitterException e) {
			e.printStackTrace();
			if (e.getStatusCode() == 403) {
				System.out.println("140文字を超えています。");
			} else if (e.getStatusCode() == 400) {
				System.out.println("タグ形式が不正です。");
			}
		}
	}
}
