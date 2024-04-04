package kz.geowarning.report.reportsevice.repository;

import kz.geowarning.report.reportsevice.entity.Editor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EditorRepository extends JpaRepository<Editor, Long> {
}
