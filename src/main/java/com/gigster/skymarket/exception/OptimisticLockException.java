package com.gigster.skymarket.exception;

import lombok.Getter;

@Getter
public class OptimisticLockException extends RuntimeException {
    private final String entityName;
    private final Object entityId;
    private final int expectedVersion;
    private final int actualVersion;

    public OptimisticLockException(String entityName, Object entityId, int expectedVersion, int actualVersion) {
        super(String.format("Optimistic locking failure for entity %s with ID %s. Expected version: %d, Actual version: %d.",
                entityName, entityId, expectedVersion, actualVersion));
        this.entityName = entityName;
        this.entityId = entityId;
        this.expectedVersion = expectedVersion;
        this.actualVersion = actualVersion;
    }

}
