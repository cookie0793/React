package org.zerock.carddatebase.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.carddatebase.domain.Owner;

public interface OwnerRepository extends JpaRepository<Owner, Long> {
}
