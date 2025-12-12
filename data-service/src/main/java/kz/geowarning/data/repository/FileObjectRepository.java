package kz.geowarning.data.repository;

import kz.geowarning.data.entity.FileObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface FileObjectRepository extends JpaRepository<FileObject, Long> {}