package br.com.m3tech.AppGas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewOrders {
	
	private String eventId;
	private String createdAt;
	private String eventType;
	private String orderId;
	private String orderUrl;
	private String orderType;

}
