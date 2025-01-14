package no.fintlabs.access.control.service;

import no.fintlabs.access.control.model.dto.FieldAccess;
import no.fintlabs.access.control.model.entity.FieldAccessEntity;
import no.fintlabs.access.control.model.entity.ResourceAccessEntity;
import no.fintlabs.access.control.model.metamodel.Metamodel;
import no.fintlabs.access.control.repository.FieldAccessEntityRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class FieldAccessService {

    private final FieldAccessEntityRepository fieldAccessEntityRepository;

    public FieldAccessService(FieldAccessEntityRepository fieldAccessEntityRepository) {
        this.fieldAccessEntityRepository = fieldAccessEntityRepository;
    }

    public List<FieldAccess> getFieldAccess(Metamodel metamodel, Optional<ResourceAccessEntity> resourceAccessEntity) {

        List<FieldAccess> result = new ArrayList<>(metamodel.fields().size());
        Collection<FieldAccessEntity> fieldAccessEntities = getFieldAccessEntities(resourceAccessEntity);

        for (String field : metamodel.fields()) {
            Optional<FieldAccessEntity> fieldAccessEntity = getFieldAccessEntity(field, fieldAccessEntities);

            result.add(
                    new FieldAccess(
                            field,
                            shouldContainConverter(fieldAccessEntity),
                            getIsHidden(fieldAccessEntity)
                    )
            );
        }

        return result;
    }

    private Collection<FieldAccessEntity> getFieldAccessEntities(Optional<ResourceAccessEntity> resourceAccessEntity) {
        if (resourceAccessEntity.isEmpty()) return Collections.emptyList();
        return fieldAccessEntityRepository.findByResourceId(resourceAccessEntity.get().getId());
    }

    private boolean getIsHidden(Optional<FieldAccessEntity> fieldAccessEntity) {
        if (fieldAccessEntity.isEmpty()) return false;
        return !fieldAccessEntity.get().getHasAccess();
    }

    private List<String> shouldContainConverter(Optional<FieldAccessEntity> fieldAccessEntity) {
        String input = fieldAccessEntity.isEmpty() ? "" : fieldAccessEntity.get().getMustContain();

        if (!StringUtils.hasText(input)) return Collections.emptyList();
        return Arrays.asList(input.split(","));
    }

    private Optional<FieldAccessEntity> getFieldAccessEntity(String field, Collection<FieldAccessEntity> fieldAccessEntities) {
        return fieldAccessEntities.stream()
                .filter(f -> f.getFieldName().equalsIgnoreCase(field))
                .findFirst();
    }
}
