package com.b2b.service.impl;

import com.b2b.dto.CompanyDto;
import com.b2b.model.Commande;
import com.b2b.model.Company;
import com.b2b.model.Produit;
import com.b2b.repository.CommandeRepository;
import com.b2b.repository.CompanyRepository;
import com.b2b.repository.ProduitRepository;
import com.b2b.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private final CompanyRepository companyRepository;

    @Autowired
    private  ProduitRepository produitRepository;

    @Autowired
    private  CommandeRepository commandeRepository;

    @Autowired
    private  ProduitServiceImpl produitService;

    @Autowired
    public CompanyServiceImpl(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    private CompanyDto toDto(Company company) {
        CompanyDto dto = new CompanyDto();
        dto.setId(company.getId());
        dto.setName(company.getName());
        dto.setAddress(company.getAddress());
        dto.setCity(company.getCity());
        dto.setPhone(company.getPhone());
        dto.setEnabled(company.isEnabled());
        dto.setEmail(company.getEmail());
        dto.setCreatedAt(company.getCreatedAt().toString());
        return dto;
    }

    private Company toEntity(CompanyDto dto) {
        Company company = new Company();
        company.setId(dto.getId());
        company.setName(dto.getName());
        company.setAddress(dto.getAddress());
        company.setCity(dto.getCity());
        company.setPhone(dto.getPhone());
        company.setEmail(dto.getEmail());
        return company;
    }

    @Override
    public List<CompanyDto> getAllCompanies() {
        return companyRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public CompanyDto getCompanyById(Long id) {
        return companyRepository.findById(id).map(this::toDto).orElse(null);
    }

    @Override
    public CompanyDto createCompany(CompanyDto companyDto) {
        Company company = toEntity(companyDto);
        return toDto(companyRepository.save(company));
    }

    @Override
    public CompanyDto updateCompany(Long id, CompanyDto companyDto) {
        return companyRepository.findById(id).map(existing -> {
            existing.setName(companyDto.getName());
            existing.setAddress(companyDto.getAddress());
            existing.setCity(companyDto.getCity());
            existing.setPhone(companyDto.getPhone());
            existing.setEmail(companyDto.getEmail());
            return toDto(companyRepository.save(existing));
        }).orElse(null);
    }

    @Override
    public CompanyDto enableCompany(Long id){
        return companyRepository.findById(id).map(existing -> {
            existing.setEnabled(true);
            return toDto(companyRepository.save(existing));
        }).orElse(null);
    }
    @Override
    public CompanyDto disableCompany(Long id){
        return companyRepository.findById(id).map(existing -> {
            existing.setEnabled(false);
            return toDto(companyRepository.save(existing));
        }).orElse(null);
    }


    @Override
    public void deleteCompany(Long id) {
        List<Produit> produits1 = produitRepository.findAll();
        if(produits1.isEmpty()){
            System.out.println("wa khawi");
            return;
        }
        List<Produit> produits = produitRepository.findByCompanyId(id);
        for(Produit prod : produits){
            produitService.delete(prod.getId());
        }
        List<Commande> commandes  = commandeRepository.findByCompanyId(id);
        for(Commande cmd : commandes){
            cmd.setCompany(null);
        }
        companyRepository.deleteById(id);
    }

    @Override
    public CompanyDto getCompanyByEmail(String email) {
        return companyRepository.findByEmailIgnoreCase(email).map(this::toDto).orElse(null);
    }
}