package ru.tbcarus.jrfinal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.tbcarus.jrfinal.model.Ticker;

import java.util.Optional;

@Repository
public interface TickerRepository extends JpaRepository<Ticker, Integer> {

    Optional<Ticker> findByName(String name);

}
