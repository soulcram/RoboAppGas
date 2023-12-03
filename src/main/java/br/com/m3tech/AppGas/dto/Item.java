package br.com.m3tech.AppGas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item {
	private String id;
	private String name;
	private Integer quantity;
	private Price unitPrice;
	private Price totalPrice;
}
