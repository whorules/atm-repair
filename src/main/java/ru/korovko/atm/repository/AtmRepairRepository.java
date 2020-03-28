package ru.korovko.atm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.korovko.atm.entity.AtmRepairEntity;

@Repository
public interface AtmRepairRepository extends JpaRepository<AtmRepairEntity, Long> {
}
