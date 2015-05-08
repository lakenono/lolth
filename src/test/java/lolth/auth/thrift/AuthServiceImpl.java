package lolth.auth.thrift;

import java.sql.SQLException;
import java.util.Map;

import lakenono.auth.AuthService;
import lolth.auth.spi.AuthManager;
import lolth.auth.spi.StandardAuthManager;
import lombok.extern.slf4j.Slf4j;

import org.apache.thrift.TException;
@Slf4j
public class AuthServiceImpl implements AuthService.Iface{
	
	private AuthManager authManager ;
	
	public AuthServiceImpl() throws SQLException{
		authManager = new StandardAuthManager();
	}

	@Override
	public Map<String, String> getAuthData(String domain, String clientIp) throws TException {
		log.info("{} {} ",clientIp,domain);
		return authManager.getAuthData(domain, clientIp);
	}

}
