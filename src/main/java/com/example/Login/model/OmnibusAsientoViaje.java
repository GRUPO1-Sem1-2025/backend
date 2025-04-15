package com.example.Login.model;

import com.example.Login.dto.EstadoViaje;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "bus_asiento_viaje")
public class OmnibusAsientoViaje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "omnibus_asiento_id")
    private OmnibusAsiento omnibusAsiento;

    @ManyToOne
    @JoinColumn(name = "viaje_id")
    private Viaje viaje;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_viaje")
    private EstadoViaje estadoViaje;
}