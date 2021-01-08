package ru.korovko.atm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.korovko.atm.entity.AtmRepairEntity;

public interface AtmRepairRepository extends JpaRepository<AtmRepairEntity, Long> {
}
