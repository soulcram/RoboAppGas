package br.com.m3tech.AppGas.dto;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pedido {
	
	private String id;
	private Merchant merchant;
	private List<Item>items;
	private Total total;
	private Payments payments;
	private Customer customer;
	private Delivery delivery;
	
	
	public String getImpressao() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(merchant.getName())
		.append("\r\n")
		.append("\r\n")
		.append("Cliente: " + customer.getName())
		.append("\r\n")
		.append("Telefone: " + customer.getPhone().getNumber())
		.append("\r\n")
		.append("Endereço")
		.append("\r\n")
		.append("Rua: " + delivery.getDeliveryAddress().getStreet())
		.append("\r\n")
		.append("Compl: " + delivery.getDeliveryAddress().getComplement())
		.append("\r\n")
		.append("Bairro: " + delivery.getDeliveryAddress().getDistrict())
		.append("\r\n")
		.append(delivery.getDeliveryAddress().getReference())
		.append("\r\n")
		.append("\r\n")
		.append("Itens")
		.append("\r\n")
		.append("Qtde  | Produto  | SubTotal ")
		.append("\r\n");
		
		for(Item item : items) {
			sb.append(item.getQuantity() + "  " + item.getName() + "  " + item.getTotalPrice().getValue());
		}
		
		sb.append("\r\n")
		.append("Total: " + payments.getPending())
		.append("\r\n");
		
		for(Method method : payments.getMethods()) {
			sb.append("Forma de Pagamento: " + method.getMethodInfo())
			.append("\r\n");
			
			if(!StringUtils.isAllBlank(method.getChangeFor())) {
				sb.append("Troco para: " + method.getChangeFor());
			}
		}
		
		sb.append("\r\n")
		.append("\r\n")
		.append("\r\n")
		.append("\r\n")
		.append("\r\n")
		.append("\r\n");
		
		return sb.toString();
	}

}
