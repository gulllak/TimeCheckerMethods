package ru.evgenii.timecheckermethods.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.evgenii.timecheckermethods.model.ClassEntity;

import java.util.List;
import java.util.Optional;

public interface ClassEntityRepository extends JpaRepository<ClassEntity, Long> {

    Optional<ClassEntity> findByClassNameAndPackageName(String className, String packageName);

    Optional<ClassEntity> findByClassName(String className);

    List<ClassEntity> findByPackageNameLikeIgnoreCase(String packageName);
}
