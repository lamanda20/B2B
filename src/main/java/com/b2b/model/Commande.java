package com.b2b.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "commandes")
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Slf4j
public class Commande {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String refCommande;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    @OneToMany(mappedBy = "commande", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LigneCommande> lignes = new ArrayList<>();

    private LocalDate dateCommande;

    @Enumerated(EnumType.STRING)
    private StatutCommande statut = StatutCommande.EN_COURS;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "livraison_id")
    private Livraison livraison;

    public void ajouterligne(LigneCommande lignecmd){
        if (lignecmd == null) return;
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
        log.info("Commande numéro :{} effectué le :{}", id, dateCommande);
        if ( company != null){
            log.info("Company : {}", company.getName());
        }
        log.info("Statut : {}", statut);
        log.info("-----------------------------------------------");
        for (LigneCommande lc : lignes){
            log.info("Produit :{} quantité: {} , prix unitaire : {} , sous-total {}",
                    lc.getProduit() != null ? lc.getProduit().getName() : "N/A",
                    lc.getQuantite(), lc.getPrixUnitaire(), lc.getSousTotal());

        }
        log.info("total {} DH", calculerTotal());
    }

}
