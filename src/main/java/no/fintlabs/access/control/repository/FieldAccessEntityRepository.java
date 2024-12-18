package no.fintlabs.access.control.repository;

import no.fintlabs.access.control.model.entity.FieldAccessEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FieldAccessEntityRepository extends JpaRepository<FieldAccessEntity, Integer> {
}
