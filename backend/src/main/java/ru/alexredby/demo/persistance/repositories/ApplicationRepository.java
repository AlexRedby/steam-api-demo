package ru.alexredby.demo.persistance.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.alexredby.demo.persistance.models.Application;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Integer> {
}
