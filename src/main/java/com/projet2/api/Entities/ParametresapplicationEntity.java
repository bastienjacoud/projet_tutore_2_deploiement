package com.projet2.api.Entities;

import javax.persistence.*;
import java.sql.Time;
import java.util.Objects;

@Entity
@Table(name = "parametresapplication", schema = "forumalternance", catalog = "")
public class ParametresapplicationEntity {
    private Integer idParametres;
    private Integer choixFerme;
    private Integer planningFerme;
    private Time debutEntretiens;
    private Integer dureeEntretiens;
    private Time finEntretiens;

    @Id
    @Column(name = "id_parametres")
    public Integer getIdParametres() {
        return idParametres;
    }

    public void setIdParametres(Integer idParametres) {
        this.idParametres = idParametres;
    }

    @Basic
    @Column(name = "choix_ferme")
    public Integer getChoixFerme() {
        return choixFerme;
    }

    public void setChoixFerme(Integer choixFerme) {
        this.choixFerme = choixFerme;
    }

    @Basic
    @Column(name = "planning_ferme")
    public Integer getPlanningFerme() {
        return planningFerme;
    }

    public void setPlanningFerme(Integer planningFerme) {
        this.planningFerme = planningFerme;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParametresapplicationEntity that = (ParametresapplicationEntity) o;
        return idParametres == that.idParametres &&
                choixFerme == that.choixFerme &&
                planningFerme == that.planningFerme;
    }

    @Override
    public int hashCode() {

        return Objects.hash(idParametres, choixFerme, planningFerme);
    }

    @Basic
    @Column(name = "debut_entretiens")
    public Time getDebutEntretiens() {
        return debutEntretiens;
    }

    public void setDebutEntretiens(Time debutEntretiens) {
        this.debutEntretiens = debutEntretiens;
    }

    @Basic
    @Column(name = "duree_entretiens")
    public Integer getDureeEntretiens() {
        return dureeEntretiens;
    }

    public void setDureeEntretiens(Integer dureeEntretiens) {
        this.dureeEntretiens = dureeEntretiens;
    }

    @Basic
    @Column(name = "fin_entretiens")
    public Time getFinEntretiens() {
        return finEntretiens;
    }

    public void setFinEntretiens(Time finEntretiens) {
        this.finEntretiens = finEntretiens;
    }
}
