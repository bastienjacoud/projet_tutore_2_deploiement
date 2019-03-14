package com.projet2.api.Entities;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Objects;

@Entity
@Table(name = "entreprise", schema = "forumalternance", catalog = "")
public class EntrepriseEntity {
    private Integer idEntreprise;
    private String nom;
    private String presentation;
    private byte[] avatar;
    private Integer nbEntreprise;

    @Id
    @Column(name = "id_entreprise")
    public Integer getIdEntreprise() {
        return idEntreprise;
    }

    public void setIdEntreprise(Integer idEntreprise) {
        this.idEntreprise = idEntreprise;
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
    @Column(name = "presentation")
    public String getPresentation() {
        return presentation;
    }

    public void setPresentation(String presentation) {
        this.presentation = presentation;
    }

    @Basic
    @Column(name = "avatar")
    public byte[] getAvatar() {
        return avatar;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }

    @Basic
    @Column(name = "nb_entreprise")
    public Integer getNbEntreprise() {
        return nbEntreprise;
    }

    public void setNbEntreprise(Integer nbEntreprise) {
        this.nbEntreprise = nbEntreprise;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EntrepriseEntity that = (EntrepriseEntity) o;
        return Objects.equals(idEntreprise, that.idEntreprise) &&
                Objects.equals(nom, that.nom) &&
                Objects.equals(presentation, that.presentation) &&
                Arrays.equals(avatar, that.avatar) &&
                Objects.equals(nbEntreprise, that.nbEntreprise);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(idEntreprise, nom, presentation, nbEntreprise);
        result = 31 * result + Arrays.hashCode(avatar);
        return result;
    }
}
