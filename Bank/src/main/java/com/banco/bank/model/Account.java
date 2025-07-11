package com.banco.bank.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Builder;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Table(name = "account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_product")
    private Long id;

    @Column(name = "account_number", unique = true)
    private String accountNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusAccount status;

    @Column(name = "balance")
    private double balance = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "Exempt_gmf")
    private ExemptGMF exemptGMF;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountType accountType;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

}

