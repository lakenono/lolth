package lolth.auth.spi.login.select;

import lolth.auth.spi.login.client.LoginClient;

public interface LoginClientSelector {

	public LoginClient select(String domain);

}
