package br.com.edsdf.proxy_gibboost;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProxyGibboostApplication {

	/**
curl -d "filtro={\"basic_data\":\"basic_data\", \"q\":\"cpf%7B070.680.938-68%7D\"}" -X POST http://localhost:8090/consulta	 
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(ProxyGibboostApplication.class, args);
	}

}

