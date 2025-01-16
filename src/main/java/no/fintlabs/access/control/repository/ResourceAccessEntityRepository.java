package no.fintlabs.access.control.repository;

import jakarta.validation.constraints.NotNull;
import no.fintlabs.access.control.model.entity.ClientEntity;
import no.fintlabs.access.control.model.entity.PackageAccessEntity;
import no.fintlabs.access.control.model.entity.ResourceAccessEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface ResourceAccessEntityRepository extends JpaRepository<ResourceAccessEntity, Integer> {
    Collection<ResourceAccessEntity> findByPackageId(Integer id);
}
