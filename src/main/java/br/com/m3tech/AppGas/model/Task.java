package br.com.m3tech.AppGas.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Task {
	

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String url;
	private String clientId;
	private String clientSecret;
	private String impressora;
	
	
	public Task(String url, String clientId, String clientSecret, String impressora) {
		super();
		this.url = url;
		this.clientId = clientId;
		this.clientSecret = clientSecret;
		this.impressora = impressora;
	}

}
