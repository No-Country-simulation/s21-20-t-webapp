package com.inventario.demo.entities.transaction.service;

import com.inventario.demo.config.PaginatedResponse;
import com.inventario.demo.config.exceptions.ResourceNotFoundException;
import com.inventario.demo.config.exceptions.TenantNotFoundException;
import com.inventario.demo.config.exceptions.UserNotFoundException;
import com.inventario.demo.entities.tenant.dtoResponse.TenantResponseDto;
import com.inventario.demo.entities.tenant.model.TenantModel;
import com.inventario.demo.entities.tenant.repository.TenantRepository;
import com.inventario.demo.entities.transaction.dtoRequest.TransactionRequestDto;
import com.inventario.demo.entities.transaction.dtoRequest.TransactionUpdateRequestDto;
import com.inventario.demo.entities.transaction.dtoResponse.TransactionResponseDto;
import com.inventario.demo.entities.transaction.mapper.TransactionMapper;
import com.inventario.demo.entities.transaction.model.TransactionModel;
import com.inventario.demo.entities.transaction.respository.TransactionRepository;
import com.inventario.demo.entities.user.model.UserModel;
import com.inventario.demo.entities.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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

    public PaginatedResponse<TransactionResponseDto> getAllTransactions(int page, int size) {
        Page<TransactionModel> transactionPage = transactionRepository.findAll(PageRequest.of(page, size));
        List<TransactionResponseDto> transactionDtos = transactionPage.getContent().stream()
                .map(transactionMapper::toDto)
                .collect(Collectors.toList());
        return new PaginatedResponse<>(transactionDtos, transactionPage.getTotalPages(), transactionPage.getTotalElements());
    }

    public TransactionResponseDto getTransactionById(Long id) {
        TransactionModel transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with id: " + id));
        return transactionMapper.toDto(transaction);
    }

    public TransactionResponseDto createTransaction(TransactionRequestDto dto) {
        TenantModel tenant = tenantRepository.findById(dto.getTenantId())
                .orElseThrow(() -> new TenantNotFoundException("Tenant no encontrado"));

        UserModel user = userRepository.findById(dto.getCreatedById())
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));

        // Se mapean los campos simples del DTO a la entidad
        TransactionModel transaction = transactionMapper.toEntity(dto);

        // Se asignan las asociaciones con las entidades gestionadas
        transaction.setTenant(tenant);
        transaction.setCreatedBy(user);

        TransactionModel savedTransaction = transactionRepository.save(transaction);
        return transactionMapper.toDto(savedTransaction);
    }

    public TransactionResponseDto updateTransaction(Long id, TransactionUpdateRequestDto transactionRequestDto) {
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

    public PaginatedResponse<TransactionResponseDto> searchTransactions(
            String type,
            String reference,
            LocalDate startDate,
            LocalDate endDate,
            Long tenantId,
            Long createdById,
            Long productId,
            int page,
            int size) {

        Pageable pageable = PageRequest.of(page, size);
        Specification<TransactionModel> spec = Specification.where(null);


        // Filtro por tipo de transacción
        if (type != null && !type.trim().isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("type"), type));
        }

        // Filtro por referencia
        if (reference != null && !reference.trim().isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("reference"), "%" + reference + "%"));
        }

        // Filtro por rango de fechas de creación
        if (startDate != null && endDate != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.between(root.get("createdAt"), startDate, endDate));
        }

        // Filtro por tenant
        if (tenantId != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("tenant").get("id"), tenantId));
        }

        // Filtro por creador
        if (createdById != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("createdBy").get("id"), createdById));
        }

        // Filtro por producto
        if (productId != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("product").get("id"), productId));
        }

        Page<TransactionModel> transactionPage = transactionRepository.findAll(spec, pageable);
        List<TransactionResponseDto> transactionDtos = transactionPage.getContent().stream()
                .map(transactionMapper::toDto)
                .collect(Collectors.toList());
        return new PaginatedResponse<>(transactionDtos, transactionPage.getTotalPages(), transactionPage.getTotalElements());
    }

    public PaginatedResponse<TransactionResponseDto> getAllTransactionsByCreatedAtRange(LocalDate startDate, LocalDate endDate, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<TransactionModel> transactionsPage = transactionRepository.findAllByCreatedAtBetween(startDate, endDate, pageable);

        List<TransactionResponseDto> transactionsDtos = transactionsPage.getContent().stream()
                .map(transactionMapper::toDto)
                .collect(Collectors.toList());

        return new PaginatedResponse<>(transactionsDtos, transactionsPage.getTotalPages(), transactionsPage.getTotalElements());
    }
}

