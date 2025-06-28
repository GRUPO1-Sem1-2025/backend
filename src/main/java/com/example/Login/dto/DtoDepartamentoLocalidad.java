package com.example.Login.dto;

//public class DtoDepartamentoLocalidad {
//    private String nombreDepartamento;
//    private int cantidad;
//
//    public DtoDepartamentoLocalidad(String nombreDepartamento, int cantidad) {
//        this.nombreDepartamento = nombreDepartamento;
//        this.cantidad = cantidad;
//    }
//
//	public String getNombreDepartamento() {
//		return nombreDepartamento;
//	}
//
//	public void setNombreDepartamento(String nombreDepartamento) {
//		this.nombreDepartamento = nombreDepartamento;
//	}
//
//	public int getCantidad() {
//		return cantidad;
//	}
//
//	public void setCantidad(int cantidad) {
//		this.cantidad = cantidad;
//	}
//    
//    
//
//}
public interface DtoDepartamentoLocalidad {
    String getNombreDepartamento();
    int getCantidadLocalidades();
}
