package com.taskflowdev.nubankclone.statement;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StatementRepository extends JpaRepository<StatementEntry, UUID> {
}
