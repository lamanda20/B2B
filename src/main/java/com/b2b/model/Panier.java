package com.b2b.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
@EqualsAndHashCode
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Panier {
    private int id ;
    private Client client;
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
        System.out.println("Votre panier : ");
        for (LignePanier lp : lignes){
            System.out.println(" Produit : "+ lp.getProduit() + " quantité :" + lp.getQuantite() +" sous-total: "+ lp.getSousTotal());
        }
        System.out.println("total du panier : "+total +"DH");
    }

    public Commande validerCommande(){

        // verifier si le panier est vide
        if (lignes.isEmpty()){
            System.out.println("Le panier est vide, impossible de valider la commande. ");
            return null;
        }
        List<LigneCommande> lc = new ArrayList<>();
        Commande commande = new Commande();
        commande.setLignes(lc);
        commande.setDateCommande(LocalDate.now());
        commande.setStatut(StatutCommande.EN_COURS);
        commande.setClient(client);

        // geneeration de reference lisible
        String ref= "CMD-"+LocalDate.now().getYear()+"-"+(int)(Math.random()* 10000);
        for (LignePanier lp : lignes){
            LigneCommande lcmd = new LigneCommande();
            lcmd.setCommande(commande);
            lcmd.setProduit(lp.getProduit());
            lcmd.setQuantite(lp.getQuantite());
            lcmd.setPrixUnitaire(lp.getProduit().getPrix());
            lc.add(lcmd);

        }
        viderPanier();
        System.out.println("commande validé" +commande.getRefCommande()+" Date "+commande.getDateCommande());
        System.out.println("Total :"+commande.calculerTotal()+"DH");
        return commande ;
    }

}
