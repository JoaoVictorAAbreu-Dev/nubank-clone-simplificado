package com.taskflowdev.nubankclone.statement;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface StatementRepository extends JpaRepository<StatementEntry, UUID> {
    @EntityGraph(attributePaths = {"account", "account.owner"})
    @Override
    List<StatementEntry> findAll();
}
