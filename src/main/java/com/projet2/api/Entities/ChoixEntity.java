package com.projet2.api.Entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "choix", schema = "forumalternance", catalog = "")
public class ChoixEntity {
    private Integer idChoix;
    private Integer idEntreprise;
    private Integer idEtudiant;
    private Integer choixEtudiant;
    private Integer choixEntreprise;
    private Integer superMatchEtudiant;
    private Integer superMatchEntreprise;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_choix")
    public Integer getIdChoix() {
        return idChoix;
    }

    public void setIdChoix(Integer idChoix) {
        this.idChoix = idChoix;
    }

    @Basic
    @Column(name = "id_entreprise")
    public Integer getIdEntreprise() {
        return idEntreprise;
    }

    public void setIdEntreprise(Integer idEntreprise) {
        this.idEntreprise = idEntreprise;
    }

    @Basic
    @Column(name = "id_etudiant")
    public Integer getIdEtudiant() {
        return idEtudiant;
    }

    public void setIdEtudiant(Integer idEtudiant) {
        this.idEtudiant = idEtudiant;
    }

    @Basic
    @Column(name = "choix_etudiant")
    public Integer getChoixEtudiant() {
        return choixEtudiant;
    }

    public void setChoixEtudiant(Integer choixEtudiant) {
        this.choixEtudiant = choixEtudiant;
    }

    @Basic
    @Column(name = "choix_entreprise")
    public Integer getChoixEntreprise() {
        return choixEntreprise;
    }

    public void setChoixEntreprise(Integer choixEntreprise) {
        this.choixEntreprise = choixEntreprise;
    }

    @Basic
    @Column(name = "super_match_etudiant")
    public Integer getSuperMatchEtudiant() {
        return superMatchEtudiant;
    }

    public void setSuperMatchEtudiant(Integer superMatchEtudiant) {
        this.superMatchEtudiant = superMatchEtudiant;
    }

    @Basic
    @Column(name = "super_match_entreprise")
    public Integer getSuperMatchEntreprise() {
        return superMatchEntreprise;
    }

    public void setSuperMatchEntreprise(Integer superMatchEntreprise) {
        this.superMatchEntreprise = superMatchEntreprise;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChoixEntity that = (ChoixEntity) o;
        return Objects.equals(idChoix, that.idChoix) &&
                Objects.equals(idEntreprise, that.idEntreprise) &&
                Objects.equals(idEtudiant, that.idEtudiant) &&
                Objects.equals(choixEtudiant, that.choixEtudiant) &&
                Objects.equals(choixEntreprise, that.choixEntreprise) &&
                Objects.equals(superMatchEtudiant, that.superMatchEtudiant) &&
                Objects.equals(superMatchEntreprise, that.superMatchEntreprise);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idChoix, idEntreprise, idEtudiant, choixEtudiant, choixEntreprise, superMatchEtudiant, superMatchEntreprise);
    }
}
