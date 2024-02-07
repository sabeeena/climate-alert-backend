package kz.kazgeowarning.authgateway.repository;

import kz.kazgeowarning.authgateway.dto.ClientDetailDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ClientDetailDTORepository extends JpaRepository<ClientDetailDTO, Long> {

    @Query("SELECT c FROM ClientDetailDTO c WHERE c.orgId = :orgId and (c.clientName like %:search% or c.clientSurname like %:search%)")
    Page<ClientDetailDTO> findAllByOrgIdOrByClientName(Long orgId, String search, Pageable pageable);
}
