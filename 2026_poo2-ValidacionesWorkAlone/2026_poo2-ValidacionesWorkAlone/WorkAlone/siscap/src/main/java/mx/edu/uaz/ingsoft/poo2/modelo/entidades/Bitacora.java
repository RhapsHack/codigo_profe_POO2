package mx.edu.uaz.ingsoft.poo2.modelo.entidades;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
public class Bitacora implements IEntidad{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private String nombreTabla;
    @Column
    private String nombreAccion;
    @Column
    private LocalDate fecha;
    @Column
    private LocalTime hora;
    @Column
    private boolean activo;
    
    public Bitacora(){
        this.fecha = LocalDate.now();
        this.hora = LocalTime.now();
    }
    
    public Bitacora(String nombreTabla,String nombreAccion, LocalDate fecha, LocalTime hora){
        this.nombreAccion = nombreAccion;
        this.nombreTabla = nombreTabla;
        this.fecha = fecha;
        this.hora = hora;
    }
    
     public Bitacora(String nombreTabla,String nombreAccion){
        this.nombreAccion = nombreAccion;
        this.nombreTabla = nombreTabla;
        this.fecha = LocalDate.now();
        this.hora = LocalTime.now();
    }
    
    

    /**
     * @return the nombreTabla
     */
    public String getNombreTabla() {
        return nombreTabla;
    }

    /**
     * @param nombreTabla the nombreTabla to set
     */
    public void setNombreTabla(String nombreTabla) {
        this.nombreTabla = nombreTabla;
    }

    /**
     * @return the nombreAccion
     */
    public String getNombreAccion() {
        return nombreAccion;
    }

    /**
     * @param nombreAccion the nombreAccion to set
     */
    public void setNombreAccion(String nombreAccion) {
        this.nombreAccion = nombreAccion;
    }

    /**
     * @return the fecha
     */
    public LocalDate getFecha() {
        return fecha;
    }

    /**
     * @param fecha the fecha to set
     */
    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    /**
     * @return the hora
     */
    public LocalTime getHora() {
        return hora;
    }

    /**
     * @param hora the hora to set
     */
    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }
    @Override
    public boolean isActivo() {
        return activo;
    }

    @Override
    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
