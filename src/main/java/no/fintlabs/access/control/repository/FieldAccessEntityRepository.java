package no.fintlabs.access.control.repository;

import no.fintlabs.access.control.model.entity.FieldAccessEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface FieldAccessEntityRepository extends JpaRepository<FieldAccessEntity, Integer> {
    Collection<FieldAccessEntity> findByResourceId(Integer id);
}
