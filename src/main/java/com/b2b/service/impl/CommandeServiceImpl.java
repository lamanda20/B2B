package com.b2b.service.impl;

import com.b2b.model.Commande;
import com.b2b.repository.CommandeRepository;
import com.b2b.service.CommandeService;
import org.springframework.stereotype.Service;

@Service
public class CommandeServiceImpl implements CommandeService {

    private final CommandeRepository commandeRepository;

    public CommandeServiceImpl(CommandeRepository commandeRepository) {
        this.commandeRepository = commandeRepository;
    }

    @Override
    public Commande getCommandeById(Long id) {
        return commandeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commande introuvable"));
    }
}
