package kz.kazgeowarning.authgateway.repository;


import kz.kazgeowarning.authgateway.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
    Session findFirstByTokenOrderByTokenCreateDateDesc(String token);
}
