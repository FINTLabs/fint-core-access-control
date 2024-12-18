package no.fintlabs.access.control.repository;

import no.fintlabs.access.control.model.entity.ResourceAccessEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResourceAccessEntityRepository extends JpaRepository<ResourceAccessEntity, Integer> {
}
