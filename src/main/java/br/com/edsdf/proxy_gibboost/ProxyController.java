package br.com.edsdf.proxy_gibboost;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.edsdf.proxy_gibboost.service.BigBoostServer;

@RestController
public class ProxyController {

		
	@Autowired
	BigBoostServer service;

	
	@GetMapping("/greeting")
	public String greeting() {
		return "greeting";
	}

	@PostMapping("/consulta")
	public String consulta(@RequestParam("filtro") String filtro) {

		return service.buscaServico(filtro);

	}

}
