package com.projet2.api.Entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "administrateur", schema = "forumalternance", catalog = "")
public class AdministrateurEntity {
    private Integer idAdministrateur;
    private String nom;
    private String prenom;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_administrateur")
    public Integer getIdAdministrateur() {
        return idAdministrateur;
    }

    public void setIdAdministrateur(Integer idAdministrateur) {
        this.idAdministrateur = idAdministrateur;
    }

    @Basic
    @Column(name = "nom")
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    @Basic
    @Column(name = "prenom")
    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AdministrateurEntity that = (AdministrateurEntity) o;
        return Objects.equals(idAdministrateur, that.idAdministrateur) &&
                Objects.equals(nom, that.nom) &&
                Objects.equals(prenom, that.prenom);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idAdministrateur, nom, prenom);
    }
}
