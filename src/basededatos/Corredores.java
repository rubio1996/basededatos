/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basededatos;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author 1daw
 */
@Entity
@Table(name = "CORREDORES")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Corredores.findAll", query = "SELECT c FROM Corredores c")
    , @NamedQuery(name = "Corredores.findById", query = "SELECT c FROM Corredores c WHERE c.id = :id")
    , @NamedQuery(name = "Corredores.findByNombre", query = "SELECT c FROM Corredores c WHERE c.nombre = :nombre")
    , @NamedQuery(name = "Corredores.findByApellidos", query = "SELECT c FROM Corredores c WHERE c.apellidos = :apellidos")
    , @NamedQuery(name = "Corredores.findByTelefono", query = "SELECT c FROM Corredores c WHERE c.telefono = :telefono")
    , @NamedQuery(name = "Corredores.findByEmail", query = "SELECT c FROM Corredores c WHERE c.email = :email")
    , @NamedQuery(name = "Corredores.findByFechaNacimiento", query = "SELECT c FROM Corredores c WHERE c.fechaNacimiento = :fechaNacimiento")
    , @NamedQuery(name = "Corredores.findByNumHijos", query = "SELECT c FROM Corredores c WHERE c.numHijos = :numHijos")
    , @NamedQuery(name = "Corredores.findByEstadoCivil", query = "SELECT c FROM Corredores c WHERE c.estadoCivil = :estadoCivil")
    , @NamedQuery(name = "Corredores.findByJubilado", query = "SELECT c FROM Corredores c WHERE c.jubilado = :jubilado")
    , @NamedQuery(name = "Corredores.findByFoto", query = "SELECT c FROM Corredores c WHERE c.foto = :foto")})
public class Corredores implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "NOMBRE")
    private String nombre;
    @Basic(optional = false)
    @Column(name = "APELLIDOS")
    private String apellidos;
    @Column(name = "TELEFONO")
    private String telefono;
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "FECHA_NACIMIENTO")
    @Temporal(TemporalType.DATE)
    private Date fechaNacimiento;
    @Column(name = "NUM_HIJOS")
    private Short numHijos;
    @Column(name = "ESTADO_CIVIL")
    private Character estadoCivil;
    @Column(name = "JUBILADO")
    private Boolean jubilado;
    @Column(name = "FOTO")
    private String foto;
    @JoinColumn(name = "CARRERA", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Carrera carrera;

    public Corredores() {
    }

    public Corredores(Integer id) {
        this.id = id;
    }

    public Corredores(Integer id, String nombre, String apellidos) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Short getNumHijos() {
        return numHijos;
    }

    public void setNumHijos(Short numHijos) {
        this.numHijos = numHijos;
    }

    public Character getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(Character estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public Boolean getJubilado() {
        return jubilado;
    }

    public void setJubilado(Boolean jubilado) {
        this.jubilado = jubilado;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public Carrera getCarrera() {
        return carrera;
    }

    public void setCarrera(Carrera carrera) {
        this.carrera = carrera;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Corredores)) {
            return false;
        }
        Corredores other = (Corredores) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "basededatos.Corredores[ id=" + id + " ]";
    }
    
}
