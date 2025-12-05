package com.b2b.dto;

import java.util.List;

public class CommandeDto {

    private Long id;
    private String refCommande;
    private String dateCommande;
    private String statut;

    private CompanyDto company; // buyer
    private List<LigneCommandeDto> lignes;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getRefCommande() { return refCommande; }
    public void setRefCommande(String refCommande) { this.refCommande = refCommande; }

    public String getDateCommande() { return dateCommande; }
    public void setDateCommande(String dateCommande) { this.dateCommande = dateCommande; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    public CompanyDto getCompany() { return company; }
    public void setCompany(CompanyDto company) { this.company = company; }

    public List<LigneCommandeDto> getLignes() { return lignes; }
    public void setLignes(List<LigneCommandeDto> lignes) { this.lignes = lignes; }
}
