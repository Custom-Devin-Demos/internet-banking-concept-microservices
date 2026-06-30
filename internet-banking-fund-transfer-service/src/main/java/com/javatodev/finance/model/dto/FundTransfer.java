package com.javatodev.finance.model.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Persisted fund transfer record")
public class FundTransfer extends AuditAware {

    @Schema(description = "Unique identifier of the fund transfer", example = "1")
    private Long id;

    @Schema(description = "Reference of the underlying core-banking transaction", example = "TXN-000123")
    private String transactionReference;

    @Schema(description = "Current status of the transfer", example = "SUCCESS")
    private String status;

    @Schema(description = "Account number money was debited from", example = "1000000001")
    private String fromAccount;

    @Schema(description = "Account number money was credited to", example = "1000000002")
    private String toAccount;

    @Schema(description = "Transferred amount", example = "150.00")
    private BigDecimal amount;
}
