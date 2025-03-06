package com.almonium.card.core.repository;

import com.almonium.card.core.model.entity.Example;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExampleRepository extends JpaRepository<Example, UUID> {}
