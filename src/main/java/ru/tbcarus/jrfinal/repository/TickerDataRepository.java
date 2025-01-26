package ru.tbcarus.jrfinal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.tbcarus.jrfinal.model.Ticker;
import ru.tbcarus.jrfinal.model.TickerData;
import ru.tbcarus.jrfinal.model.User;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TickerDataRepository extends JpaRepository<TickerData, Integer> {

    List<TickerData> findByUserAndTickerAndDateBetween(User user, Ticker ticker, LocalDate from, LocalDate to);
    List<TickerData> findAllByUserAndTicker(User user, Ticker ticker);

}
