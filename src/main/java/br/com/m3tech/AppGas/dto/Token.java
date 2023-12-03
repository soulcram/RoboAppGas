package br.com.m3tech.AppGas.dto;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Token {
	
	@SerializedName(value="access_token")
	private String accessToken;
	@SerializedName(value="token_type")
	private String tokenType;
	@SerializedName(value="expires_in")
	private String expiresIn;

}
