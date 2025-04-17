package com.example.Login.dto;

public class DtoCambiarContrasenia {
	private String email;
	private String old_pass;
	private String new_pass;
	private String new_pass1;
	
	public DtoCambiarContrasenia() {
		
	}
	
	public DtoCambiarContrasenia(String email, String old_pass, String new_pass, String new_pass1) {
		super();
		this.email = email;
		this.old_pass = old_pass;
		this.new_pass = new_pass;
		this.new_pass1 = new_pass1;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getOld_pass() {
		return old_pass;
	}

	public void setOld_pass(String old_pass) {
		this.old_pass = old_pass;
	}

	public String getNew_pass() {
		return new_pass;
	}

	public void setNew_pass(String new_pass) {
		this.new_pass = new_pass;
	}

	public String getNew_pass1() {
		return new_pass1;
	}

	public void setNew_pass1(String new_pass1) {
		this.new_pass1 = new_pass1;
	}
	
	
}
