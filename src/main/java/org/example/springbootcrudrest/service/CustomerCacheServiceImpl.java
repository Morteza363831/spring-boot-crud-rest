package org.example.springbootcrudrest.service;

import lombok.RequiredArgsConstructor;
import org.example.springbootcrudrest.repository.inMem.InMemoryCustomerRepositoryImpl;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerCacheServiceImpl implements CustomerCacheService {

    private final InMemoryCustomerRepositoryImpl customerRepository;

    @Override
    public void refreshCache() {
        customerRepository.refresh();
    }
}
