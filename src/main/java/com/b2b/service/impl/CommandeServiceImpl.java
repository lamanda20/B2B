package com.b2b.service.impl;

import com.b2b.model.Commande;
import com.b2b.model.LigneCommande;
import com.b2b.model.StatutCommande;
import com.b2b.repository.CommandeRepository;
import com.b2b.service.CommandeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CommandeServiceImpl implements CommandeService {

    private static final String COMMANDE_NOT_FOUND = "Commande non trouvée";
    private final CommandeRepository commandeRepository;

    @Autowired
    public CommandeServiceImpl(CommandeRepository commandeRepository) {
        this.commandeRepository = commandeRepository;
    }

    @Override
    public List<Commande> findAll() {
        return commandeRepository.findAll();
    }

    @Override
    public Optional<Commande> findById(Long id) {
        return commandeRepository.findById(id);
    }

    @Override
    public Optional<Commande> findByRefCommande(String refCommande) {
        return commandeRepository.findByRefCommande(refCommande);
    }

    @Override
    public List<Commande> findByCompany(Long companyId) {
        return commandeRepository.findByCompanyId(companyId);
    }

    @Override
    public Commande create(Commande commande) {
        return commandeRepository.save(commande);
    }

    @Override
    public Commande ajouterLigneCommande(Long commandeId, LigneCommande ligne) {
        Commande commande = commandeRepository.findById(commandeId)
                .orElseThrow(() -> new RuntimeException(COMMANDE_NOT_FOUND));
        commande.ajouterLigne(ligne);
        return commandeRepository.save(commande);
    }

    @Override
    public double calculerTotal(Long commandeId) {
        Commande commande = commandeRepository.findById(commandeId)
                .orElseThrow(() -> new RuntimeException(COMMANDE_NOT_FOUND));
        return commande.calculerTotal();
    }

    @Override
    public StatutCommande validerCommande(Long commandeId) {
        Commande commande = commandeRepository.findById(commandeId)
                .orElseThrow(() -> new RuntimeException(COMMANDE_NOT_FOUND));
        StatutCommande statut = commande.validerCommande();
        commandeRepository.save(commande);
        return statut;
    }

    @Override
    public void afficherCommande(Long commandeId) {
        Commande commande = commandeRepository.findById(commandeId)
                .orElseThrow(() -> new RuntimeException(COMMANDE_NOT_FOUND));
        commande.afficherCommande();
    }

    @Override
    public Commande updateStatut(Long commandeId, StatutCommande statut) {
        Commande commande = commandeRepository.findById(commandeId)
                .orElseThrow(() -> new RuntimeException(COMMANDE_NOT_FOUND));
        commande.setStatut(statut);
        return commandeRepository.save(commande);
    }

    @Override
    public void delete(Long id) {
        commandeRepository.deleteById(id);
    }
}
