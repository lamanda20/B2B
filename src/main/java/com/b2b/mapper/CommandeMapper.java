package com.b2b.mapper;

import com.b2b.dto.CommandeDto;
import com.b2b.dto.CompanyDto;
import com.b2b.dto.LigneCommandeDto;
import com.b2b.model.Commande;
import com.b2b.model.LigneCommande;

import java.util.stream.Collectors;

public class CommandeMapper {

    public static CommandeDto toDto(Commande c) {
        CommandeDto dto = new CommandeDto();

        dto.setId(c.getId());
        dto.setRefCommande(c.getRefCommande());
        dto.setDateCommande(
                c.getDateCommande() != null ? c.getDateCommande().toString() : null
        );
        dto.setStatut(c.getStatut().name());

        // Convert company → CompanyDto
        if (c.getCompany() != null) {
            CompanyDto comp = new CompanyDto();
            comp.setId(c.getCompany().getId());
            comp.setName(c.getCompany().getName());
            comp.setCity(c.getCompany().getCity());
            comp.setPhone(c.getCompany().getPhone());
            comp.setEmail(c.getCompany().getEmail());
            dto.setCompany(comp);
        }

        // Convert lignes to DTOs
        if (c.getLignes() != null) {
            dto.setLignes(
                    c.getLignes()
                            .stream()
                            .map(LigneCommandeDto::fromEntity)
                            .collect(Collectors.toList())
            );
        }

        return dto;
    }
}
