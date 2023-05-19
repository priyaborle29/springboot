package com.rest.api.payload;

import lombok.Data;

@Data
public class AddressDto {

	private String district;
	private String state;
	private String country;
	private long pinCode;
	
}
