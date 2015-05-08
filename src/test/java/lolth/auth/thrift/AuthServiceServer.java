package lolth.auth.thrift;

import lakenono.auth.AuthService;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.apache.thrift.TProcessor;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;

@Slf4j
public class AuthServiceServer {

	public static void main(String[] args) {
		if (args.length == 0) {
			System.out.println("Usage : AuthServiceServer [port]");
			return;
		}

		String port = args[0];
		if (!StringUtils.isNumeric(port)) {
			System.out.println("Commond error : AuthServiceServer [port], port must be number !");
			return;
		}

		try {
			TProcessor processor = new AuthService.Processor<AuthService.Iface>(new AuthServiceImpl());

			TServerTransport serverTransport = new TServerSocket(Integer.parseInt(port));

			TServer server = new TThreadPoolServer(new TThreadPoolServer.Args(serverTransport).processor(processor));

			log.info("Starting the auth service server...");

			server.serve();
		} catch (Exception e) {
			log.error("Starting the auth service server error : ", e);
		}

	}
}
