package com.rest.api.payload;

import java.util.List;

import lombok.Data;

@Data
public class UserDetailsRequest
{
	private String email;
    private int mobNum;
    private String address;
	private String state;
	private String country;	
	
	private List<AddressDto> addressDto ;
	
}
