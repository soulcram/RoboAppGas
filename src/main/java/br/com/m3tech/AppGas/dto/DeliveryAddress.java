package br.com.m3tech.AppGas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryAddress {
	private String country;
	private String state;
	private String city;
	private String district;
	private String street;
	private String number;
	private String complement;
	private String reference;
	private String postalCode;
}
