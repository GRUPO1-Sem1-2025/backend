package com.example.Login.dto;

public class DtoCalificacionUsuario {

		private Integer calificacion;
		private String comentario;
		
		public DtoCalificacionUsuario() {
			
		}
		public DtoCalificacionUsuario(Integer calificacion, String comentario) {
			super();
			this.calificacion = calificacion;
			this.comentario = comentario;
		}
		public Integer getCalificacion() {
			return calificacion;
		}
		public void setCalificacion(Integer calificacion) {
			this.calificacion = calificacion;
		}
		public String getComentario() {
			return comentario;
		}
		public void setComentario(String comentario) {
			this.comentario = comentario;
		}
		

	


}
