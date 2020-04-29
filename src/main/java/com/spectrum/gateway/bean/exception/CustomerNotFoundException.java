package com.spectrum.gateway.bean.exception;

public class CustomerNotFoundException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 5359138371277738047L;

	public CustomerNotFoundException(String customerID) {
        super("Customer ID not found : " + customerID);
    }

}