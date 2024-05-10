package com.akatsuki.pioms.franchise.service;

import com.akatsuki.pioms.franchise.aggregate.Franchise;
import com.akatsuki.pioms.franchise.repository.FranchiseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class FranchiseServiceImpl implements FranchiseService{

    private final FranchiseRepository franchiseRepository;

    @Autowired
    public FranchiseServiceImpl(FranchiseRepository franchiseRepository) {
        this.franchiseRepository = franchiseRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Franchise> findFranchiseList() {
        return franchiseRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Franchise> findFranchiseById(int franchiseCode) {
        return franchiseRepository.findById(franchiseCode);
    }



}
