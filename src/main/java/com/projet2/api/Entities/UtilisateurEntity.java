package com.projet2.api.Entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "utilisateur", schema = "forumalternance", catalog = "")
public class UtilisateurEntity {
    private Integer idUtilisateur;
    private String login;
    private String motPasse;
    private Integer etudiantIdEtudiant;
    private Integer entrepriseIdEntreprise;
    private Integer adminitrateurIdAdministrateur;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_utilisateur")
    public Integer getIdUtilisateur() {
        return idUtilisateur;
    }

    public void setIdUtilisateur(Integer idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }

    @Basic
    @Column(name = "login")
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Basic
    @Column(name = "mot_passe")
    public String getMotPasse() {
        return motPasse;
    }

    public void setMotPasse(String motPasse) {
        this.motPasse = motPasse;
    }

    @Basic
    @Column(name = "Etudiant_id_etudiant")
    public Integer getEtudiantIdEtudiant() {
        return etudiantIdEtudiant;
    }

    public void setEtudiantIdEtudiant(Integer etudiantIdEtudiant) {
        this.etudiantIdEtudiant = etudiantIdEtudiant;
    }

    @Basic
    @Column(name = "Entreprise_id_entreprise")
    public Integer getEntrepriseIdEntreprise() {
        return entrepriseIdEntreprise;
    }

    public void setEntrepriseIdEntreprise(Integer entrepriseIdEntreprise) {
        this.entrepriseIdEntreprise = entrepriseIdEntreprise;
    }

    @Basic
    @Column(name = "Adminitrateur_id_administrateur")
    public Integer getAdminitrateurIdAdministrateur() {
        return adminitrateurIdAdministrateur;
    }

    public void setAdminitrateurIdAdministrateur(Integer adminitrateurIdAdministrateur) {
        this.adminitrateurIdAdministrateur = adminitrateurIdAdministrateur;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UtilisateurEntity that = (UtilisateurEntity) o;
        return Objects.equals(idUtilisateur, that.idUtilisateur) &&
                Objects.equals(login, that.login) &&
                Objects.equals(motPasse, that.motPasse) &&
                Objects.equals(etudiantIdEtudiant, that.etudiantIdEtudiant) &&
                Objects.equals(entrepriseIdEntreprise, that.entrepriseIdEntreprise) &&
                Objects.equals(adminitrateurIdAdministrateur, that.adminitrateurIdAdministrateur);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idUtilisateur, login, motPasse, etudiantIdEtudiant, entrepriseIdEntreprise, adminitrateurIdAdministrateur);
    }
}
