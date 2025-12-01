package com.banking.entity;

/**
 * JobStatus Enum - Defines the status of statement generation jobs
 *
 * PENDING: Job created but not yet started
 * PROCESSING: Job is currently being processed
 * COMPLETED: Job finished successfully
 * FAILED: Job encountered an error
 */
public enum JobStatus {
    PENDING,
    PROCESSING,
    COMPLETED,
    FAILED
}
