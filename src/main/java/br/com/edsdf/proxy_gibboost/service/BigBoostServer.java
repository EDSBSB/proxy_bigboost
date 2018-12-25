package br.com.edsdf.proxy_gibboost.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;
import org.postgresql.util.PGobject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class BigBoostServer {
	@Autowired
	JdbcTemplate jdbcTemplate;

	@Value("${AccessToken}")
	private String accessToken;

	@Value("${bigBoostUrl}")
	private String bigBoostUrl;

	public String buscaServico(String filtro) {
		String retorno = buscaFiltro( filtro );
		
		if( retorno != null && !retorno.isEmpty()) {
			return retorno;
		}

		try {
			JSONObject jFiltro = new JSONObject(filtro);
			
			String q = jFiltro.getString("q");
//			q = q.replaceAll("\\.","").replaceAll("-", "");
			
			CloseableHttpClient client = HttpClients.createDefault();
			String url = bigBoostUrl+"?Datasets="
					+jFiltro.getString("Datasets")+"&q="
					+q
					+"&AccessToken="+accessToken;
			
			url = url.replaceAll("\\{", "%7B").replaceAll("\\}", "%7D");
			
			System.out.println(url);
			HttpGet http = new HttpGet(url);

			CloseableHttpResponse response = client.execute(http);

			if (response.getStatusLine().getStatusCode() == 200) {
				retorno = resultBody(response);
				System.out.println(retorno);
				salvaResponsta(filtro, retorno);
			}else {
				JSONObject j = new JSONObject();
				j.put("sucess", false);
				j.put("errorcode", response.getStatusLine().getStatusCode());
				j.put("msg", resultBody(response) );
				return j.toString();
			}
		} catch (Exception e) {
			System.out.println( e.getMessage() );
			e.printStackTrace();
		}

		return retorno;
	}

	private void salvaResponsta(String filtro, String retorno) {
		JSONObject j = new JSONObject();
		j.put("request", filtro);
		j.put("response", retorno);
		
		String sql = "insert into conteiner (tipo, dado) values (?, ?::json)";
		
		jdbcTemplate.update(sql, "bigBoost", j.toString() );
		
	}

	private String buscaFiltro(String filtro) {
		String arr[] = filtro.split("q\":");
		if( arr.length > 1) {
			filtro = arr[1];
		}
		String sql = "select dado from conteiner where tipo = 'bigBoost' and dado ->> 'request' like  ?";
		List<?>lst = jdbcTemplate.queryForList(sql, new Object[] {"%"+filtro+"%"}, String.class);
		System.out.println( lst );
		if( lst != null && lst.size() > 0 ) {
			String s = lst.get(0).toString();
			JSONObject j = new JSONObject(s);
			return j.get("response").toString();
		}
		
		return null;
	}

	private String resultBody(CloseableHttpResponse response) throws UnsupportedOperationException, IOException {
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));// ,
																											// "ISO-8859-1"));

		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}

		return result.toString();
	}

}
