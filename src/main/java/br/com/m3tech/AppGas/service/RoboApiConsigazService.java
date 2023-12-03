package br.com.m3tech.AppGas.service;

import java.text.Normalizer;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;

import br.com.m3tech.AppGas.dto.Acknowledgment;
import br.com.m3tech.AppGas.dto.NewOrders;
import br.com.m3tech.AppGas.dto.Pedido;
import br.com.m3tech.AppGas.dto.Token;
import br.com.m3tech.AppGas.model.Task;
import br.com.m3tech.AppGas.util.ImprimirUtils;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RoboApiConsigazService {
	
	@Autowired
	private TaskService taskService;
	
	private Token token;
	
	private String urlBase;
	private RestTemplate restTemplate = new RestTemplate();
	
	@Scheduled(cron = "*/31 * * * * *") // Executa a cada 31 seg
    public void minhaTarefa() {
        // Lógica da tarefa que será executada a cada minuto
        System.out.println(LocalTime.now() + " Tarefa agendada executada!");
        
        Task task = taskService.buscarPrimeiro();
        
        this.urlBase = task.getUrl();
        
		if(token == null) {
			
			MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
			body.add("grant_type", "client_credentials");
			body.add("client_id", task.getClientId());
			body.add("client_secret", task.getClientSecret());
	        
			ResponseEntity<String> response = new RequestApiClient(this.restTemplate, HttpMethod.POST, urlBase)
                    .addContentTypeUrlEncoded()
                    .pathValue("authentication")
                    .pathValue("v1")
                    .pathValue("oauth")
                    .pathValue("token")
                    .bodyFromMultiValueMap(body)
                    .build()
                    .enviar();
			
			token = new Gson().fromJson(response.getBody(), Token.class);
			log.info(token.toString());
			
		}
		
		ResponseEntity<String> responseNewOrders = new RequestApiClient(this.restTemplate, HttpMethod.GET, urlBase)
                .addContentTypeJson()
                .pathValue("merchant")
                .pathValue("v3")
                .pathValue("events:polling")
                .addHeader("Authorization", "Bearer " + token.getAccessToken())
                .requestParam("eventType[]", "CREATED")
                .build()
                .enviar();
		
		NewOrders[] novosPedidos = new Gson().fromJson(responseNewOrders.getBody(), NewOrders[].class);
		
		if(novosPedidos == null) {
			return;
		}
		
		for(NewOrders order : novosPedidos) {
			log.info(order.toString());
			
			if("CREATED".equalsIgnoreCase(order.getEventType()) ) {
				ResponseEntity<String> responseConfirm = new RequestApiClient(this.restTemplate, HttpMethod.POST, urlBase)
		                .addContentTypeJson()
		                .pathValue("merchant")
		                .pathValue("v3")
		                .pathValue("orders")
		                .pathValue(order.getOrderId())
		                .pathValue("confirm")
		                .addHeader("Authorization", "Bearer " + token.getAccessToken())
		                .build()
		                .enviar();
				
//				log.info(responseConfirm.getBody());
				
				List<Acknowledgment> eventos = new ArrayList<>();
				
				eventos.add(new Acknowledgment(order.getEventId(),order.getOrderId(),order.getEventType()));
				
				ResponseEntity<String> responseConhecido = new RequestApiClient(this.restTemplate, HttpMethod.POST, urlBase)
		                .addContentTypeJson()
		                .pathValue("merchant")
		                .pathValue("v3")
		                .pathValue("events")
		                .pathValue("acknowledgment")
		                .addHeader("Authorization", "Bearer " + token.getAccessToken())
		                .bodyFromObject(eventos)
		                .build()
		                .enviar();
				
//				log.info(responseConhecido.getBody());
				
				
				ResponseEntity<String> responsePedido = new RequestApiClient(this.restTemplate, HttpMethod.GET, urlBase)
		                .addContentTypeJson()
		                .pathValue("merchant")
		                .pathValue("v3")
		                .pathValue("orders")
		                .pathValue(order.getOrderId())
		                .addHeader("Authorization", "Bearer " + token.getAccessToken())
		                .build()
		                .enviar();
				
				Pedido detalhePedido = new Gson().fromJson(responsePedido.getBody(), Pedido.class);
				
				log.info(detalhePedido.getImpressao());
				
				ImprimirUtils.imprimir(semAcento(detalhePedido.getImpressao()), task.getImpressora());
			}
		}
		
        
    }
	
	private String semAcento(String text) {
		String nfdNormalizedString = Normalizer.normalize(text, Normalizer.Form.NFD);
		Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
		return pattern.matcher(nfdNormalizedString).replaceAll("");
	}

}
