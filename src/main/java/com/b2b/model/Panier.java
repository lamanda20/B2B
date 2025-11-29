package com.b2b.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
@EqualsAndHashCode
@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class Panier {
    private int id ;
    private Company company;
    private List<LignePanier> lignes = new ArrayList<>() ;
    private LocalDate dateCreation = LocalDate.now() ;
    private double total;

    public void ajouterProduit(Produit p , int quantite){
        boolean produitTrouve = false ;
        for (LignePanier lp : lignes){
            if (lp.getProduit().equals(p)) {
                lp.setQuantite(lp.getQuantite() + quantite);
                produitTrouve = true;
                break;
            }

        }
        if (!produitTrouve){
            lignes.add(new LignePanier(lignes.size()+1,quantite,this,p));
        }
        total=calculerTotal();
    }
    public void supprimerProduit(Produit p){
        for (int i=0;i <lignes.size();i++){
            LignePanier lp =lignes.get(i);
            if(lp.getProduit().equals(p)){
                int qt=lp.getQuantite();
                if (qt > 1){
                    lp.setQuantite(lp.getQuantite()-1);
                }else {
                    lignes.remove(i);
                }
                total=calculerTotal();
                break;
            }
        }
    }
    public void viderPanier(){
        lignes.clear();
        total= 0.0 ;
    }
    public double calculerTotal(){
        total = 0.0 ;
        for(LignePanier lp : lignes){
            total += lp.getSousTotal();
        }
        return total ;
    }
    public void afficherContenu(){
        log.info("Votre panier : ");
        for (LignePanier lp : lignes){
            log.info(" Produit : {} quantité : {} sous-total: {}", lp.getProduit(), lp.getQuantite(), lp.getSousTotal());
        }
        log.info("total du panier : {} DH", total);
    }

    public Commande validerCommande(){

        // verifier si le panier est vide
        if (lignes.isEmpty()){
            log.warn("Le panier est vide, impossible de valider la commande.");
            return null;
        }
        List<LigneCommande> lc = new ArrayList<>();
        Commande commande = new Commande();
        commande.setLignes(lc);
        commande.setDateCommande(LocalDate.now());
        commande.setStatut(StatutCommande.EN_COURS);

        // generation de reference lisible
        int rand = ThreadLocalRandom.current().nextInt(0, 10000);
        String ref= String.format("CMD-%d-%04d", LocalDate.now().getYear(), rand);
        commande.setRefCommande(ref);

        // associer la company si présente
        commande.setCompany(company);

        for (LignePanier lp : lignes){
            LigneCommande lcmd = new LigneCommande();
            lcmd.setCommande(commande);
            lcmd.setProduit(lp.getProduit());
            lcmd.setQuantite(lp.getQuantite());
            // Produit.price est un BigDecimal -> convertir en double
            if (lp.getProduit() != null && lp.getProduit().getPrice() != null) {
                lcmd.setPrixUnitaire(lp.getProduit().getPrice().doubleValue());
            } else {
                lcmd.setPrixUnitaire(0.0);
            }
            lc.add(lcmd);

        }
        viderPanier();
        log.info("commande validée {} Date {}", commande.getRefCommande(), commande.getDateCommande());
        log.info("Total : {} DH", commande.calculerTotal());
        return commande ;
    }

}
