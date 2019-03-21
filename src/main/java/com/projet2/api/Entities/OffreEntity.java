package com.projet2.api.Entities;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Objects;

@Entity
@Table(name = "offre", schema = "forumalternance", catalog = "")
public class OffreEntity {
    private Integer idOffre;
    private Integer idEntreprise;
    private byte[] offre;
    private String titre;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_offre")
    public Integer getIdOffre() {
        return idOffre;
    }

    public void setIdOffre(Integer idOffre) {
        this.idOffre = idOffre;
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
    @Column(name = "offre")
    public byte[] getOffre() {
        return offre;
    }

    public void setOffre(byte[] offre) {
        this.offre = offre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OffreEntity that = (OffreEntity) o;
        return Objects.equals(idOffre, that.idOffre) &&
                Objects.equals(idEntreprise, that.idEntreprise) &&
                Arrays.equals(offre, that.offre);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(idOffre, idEntreprise);
        result = 31 * result + Arrays.hashCode(offre);
        return result;
    }

    @Basic
    @Column(name = "titre")
    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }
}
