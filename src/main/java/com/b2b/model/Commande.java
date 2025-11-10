package com.b2b.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Commande {
    private int id ;
    private String refCommande;
    private Client client ;
    private List<LigneCommande> lignes = new ArrayList<>();
    private LocalDate dateCommande ;
    private StatutCommande statut = StatutCommande.EN_COURS;


    public void ajouterligne(LigneCommande lignecmd){
        lignecmd.setCommande(this);
        lignes.add(lignecmd);
    }
    public double calculerTotal(){
        double total= 0.0;
        for ( LigneCommande lcm : lignes){
            total += lcm.getSousTotal();
        }
        return total;
    }

    public StatutCommande suivreCommande(){
        return this.getStatut();
    }
    public void afficherCommande(){
        System.out.println("Commande numéro :"+id+" effectué le :" + dateCommande);
        if ( client != null){
            System.out.println("Client :" +  statut );
        }
        System.out.println("Statut :" +  statut );
        System.out.println("-----------------------------------------------");
        for (LigneCommande lc : lignes){
            System.out.println("Produit :" + lc.getProduit().getNom()
            + "quantité: " +lc.getQuantite()+ " , prix unitaire : " + lc.getPrixUnitaire() + ", sous-total " + lc.getSousTotal());

        }
        System.out.println("total " + calculerTotal() + " DH" );
    }

}
