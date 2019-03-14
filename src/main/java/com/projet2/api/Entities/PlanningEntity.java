package com.projet2.api.Entities;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "planning", schema = "forumalternance", catalog = "")
public class PlanningEntity {
    private Integer idPlanning;
    private Integer idChoix;
    private Integer ordre;
    private Integer retard;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_planning")
    public Integer getIdPlanning() {
        return idPlanning;
    }

    public void setIdPlanning(Integer idPlanning) {
        this.idPlanning = idPlanning;
    }

    @Basic
    @Column(name = "id_choix")
    public Integer getIdChoix() {
        return idChoix;
    }

    public void setIdChoix(Integer idChoix) {
        this.idChoix = idChoix;
    }

    @Basic
    @Column(name = "ordre")
    public Integer getOrdre() {
        return ordre;
    }

    public void setOrdre(Integer ordre) {
        this.ordre = ordre;
    }

    @Basic
    @Column(name = "retard")
    public Integer getRetard() {
        return retard;
    }

    public void setRetard(Integer retard) {
        this.retard = retard;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlanningEntity that = (PlanningEntity) o;
        return Objects.equals(idPlanning, that.idPlanning) &&
                Objects.equals(idChoix, that.idChoix) &&
                Objects.equals(ordre, that.ordre) &&
                Objects.equals(retard, that.retard);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPlanning, idChoix, ordre, retard);
    }
}
