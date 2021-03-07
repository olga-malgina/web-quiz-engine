package engine;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizCompletionRepository extends PagingAndSortingRepository<QuizCompletion, Long> {

    @Query(value = "SELECT q FROM QuizCompletion q WHERE q.user = :user")
    Page<QuizCompletion> getQuizCompletionByUser(@Param("user") Users user, Pageable pageable);
}
