package pl.uj.repositories;

import org.springframework.data.repository.CrudRepository;
import pl.uj.model.Score;

public interface ScoreRepository extends CrudRepository<Score, Long> {
}
