package no.fintlabs.access.control.repository;

import jakarta.validation.constraints.NotNull;
import no.fintlabs.access.control.model.entity.ClientEntity;
import no.fintlabs.access.control.model.entity.PackageAccessEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface PackageAccessEntityRepository extends JpaRepository<PackageAccessEntity, Integer> {

    Collection<PackageAccessEntity> findByClient(@NotNull ClientEntity client);
}
