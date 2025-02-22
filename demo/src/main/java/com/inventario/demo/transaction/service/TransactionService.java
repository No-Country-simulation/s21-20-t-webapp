package com.inventario.demo.transaction.service;

import com.inventario.demo.config.exceptions.ResourceNotFoundException;
import com.inventario.demo.tenant.model.TenantModel;
import com.inventario.demo.tenant.repository.TenantRepository;
import com.inventario.demo.tenant.service.TenantService;
import com.inventario.demo.transaction.dtoRequest.TransactionRequestDto;
import com.inventario.demo.transaction.dtoResponse.TransactionPageResponse;
import com.inventario.demo.transaction.dtoResponse.TransactionResponseDto;
import com.inventario.demo.transaction.mapper.TransactionMapper;
import com.inventario.demo.transaction.model.TransactionModel;
import com.inventario.demo.transaction.respository.TransactionRepository;
import com.inventario.demo.user.model.UserModel;
import com.inventario.demo.user.repository.UserRepository;
import com.inventario.demo.user.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final TenantRepository tenantRepository;
    private final UserRepository userRepository;

    public TransactionPageResponse getAllTransactions(int page, int size) {
        Page<TransactionModel> transactionPage = transactionRepository.findAll(PageRequest.of(page, size));
        List<TransactionResponseDto> transactionDtos = transactionPage.getContent().stream()
                .map(transactionMapper::toDto)
                .collect(Collectors.toList());
        return new TransactionPageResponse(transactionDtos, transactionPage.getTotalPages(), transactionPage.getTotalElements());
    }

    public TransactionResponseDto getTransactionById(Long id) {
        TransactionModel transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with id: " + id));
        return transactionMapper.toDto(transaction);
    }

    public TransactionResponseDto createTransaction(TransactionRequestDto dto) {
        TenantModel tenant = tenantRepository.findById(dto.getTenantId())
                .orElseThrow(() -> new EntityNotFoundException("Tenant no encontrado"));

        UserModel user = userRepository.findById(dto.getCreatedById())
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        // Se mapean los campos simples del DTO a la entidad
        TransactionModel transaction = transactionMapper.toEntity(dto);

        // Se asignan las asociaciones con las entidades gestionadas
        transaction.setTenant(tenant);
        transaction.setCreatedBy(user);

        TransactionModel savedTransaction = transactionRepository.save(transaction);
        return transactionMapper.toDto(savedTransaction);
    }

    public TransactionResponseDto updateTransaction(Long id, TransactionRequestDto transactionRequestDto) {
        TransactionModel existingTransaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with id: " + id));
        transactionMapper.updateEntityFromDto(transactionRequestDto, existingTransaction);
        TransactionModel updatedTransaction = transactionRepository.save(existingTransaction);
        return transactionMapper.toDto(updatedTransaction);
    }

    public void deleteTransaction(Long id) {
        TransactionModel transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with id: " + id));
        transactionRepository.delete(transaction);
    }
}
