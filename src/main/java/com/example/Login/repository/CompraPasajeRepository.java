package com.example.Login.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.Login.dto.EstadoCompra;
import com.example.Login.dto.DtoComprasUsuarios;
import com.example.Login.dto.DtoNewUsuariosPorMes;
import com.example.Login.dto.DtoTipoDeCompra;
import com.example.Login.dto.DtoTotalPorMes;
import com.example.Login.model.Asiento;
import com.example.Login.model.AsientoPorViaje;
import com.example.Login.model.CompraPasaje;
import com.example.Login.model.Viaje;

@Repository
public interface CompraPasajeRepository extends JpaRepository<CompraPasaje, Integer> {
	@Query("SELECT apv " +
		       "FROM AsientoPorViaje apv " +
		       "JOIN apv.omnibusAsiento oa " +
		       "JOIN oa.asiento a " +
		       "WHERE apv.viaje.id = :viajeId AND a.nro = :nroAsiento")
		Optional<AsientoPorViaje> findByViajeIdAndNroAsiento(@Param("viajeId") Long viajeId,
		                                                     @Param("nroAsiento") Integer nroAsiento);
	
	List<CompraPasaje> findByEstadoCompraAndFechaHoraCompraBefore(EstadoCompra estadoCompra, LocalDateTime fechaLimite);

	Optional<CompraPasaje> findById(Long compraId);
	
	List<CompraPasaje> findByViajeId(Long idViaje);
	
	@Query("""
		    SELECT new com.example.Login.dto.DtoTotalPorMes(
		        FUNCTION('TO_CHAR', c.fechaHoraCompra, 'MM'),
		        FUNCTION('TO_CHAR', c.fechaHoraCompra, 'YYYY'),
		        SUM(c.total)
		    )
		    FROM CompraPasaje c
		    GROUP BY FUNCTION('TO_CHAR', c.fechaHoraCompra, 'MM'), FUNCTION('TO_CHAR', c.fechaHoraCompra, 'YYYY')
		    ORDER BY FUNCTION('TO_CHAR', c.fechaHoraCompra, 'YYYY'), FUNCTION('TO_CHAR', c.fechaHoraCompra, 'MM')
		""")
	List<DtoTotalPorMes> findTotalPorMes();
	
//	@Query("SELECT c.tipo_venta, COUNT(c) FROM CompraPasaje c GROUP BY c.tipo_venta")
//	List<DtoTipoDeCompra> contarPorTipoVenta();
	
	@Query("SELECT new com.example.Login.dto.DtoTipoDeCompra(c.tipo_venta, COUNT(c)) FROM CompraPasaje c GROUP BY c.tipo_venta")
	List<DtoTipoDeCompra> contarPorTipoVenta();
	
	Optional<CompraPasaje> findTopByOrderByIdDesc();
	
	@Query(value = """
		    SELECT u.nombre AS nombre, 
		           u.apellido AS apellido, 
		           COUNT(c.cliente_id) AS cantidadCompras
		    FROM compras c
		    JOIN usuarios u ON u.id = c.cliente_id
		    GROUP BY c.cliente_id, u.nombre, u.apellido
		    ORDER BY cantidadCompras DESC
		    LIMIT 5
		    """, nativeQuery = true)
		List<DtoComprasUsuarios> obtenerComprasPorUsuario();
}
