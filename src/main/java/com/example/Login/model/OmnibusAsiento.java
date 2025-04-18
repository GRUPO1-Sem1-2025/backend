package com.example.Login.model;

import jakarta.persistence.*;

@Entity
@Table(name = "bus_asiento")
public class OmnibusAsiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "bus_id")
    private Omnibus omnibus;

    @ManyToOne
    @JoinColumn(name = "asiento_id")
    private Asiento asiento;

//    @Column(name = "estado")
//    private boolean estado;

    // Getters y setters
    public Long getId() {
        return id;
    }

    public Omnibus getOmnibus() {
        return omnibus;
    }

    public void setOmnibus(Omnibus omnibus) {
        this.omnibus = omnibus;
    }

    public Asiento getAsiento() {
        return asiento;
    }

    public void setAsiento(Asiento asiento) {
        this.asiento = asiento;
    }

//    public boolean isEstado() {
//        return estado;
//    }
//
//    public void setEstado(boolean estado) {
//        this.estado = estado;
//    }
}
