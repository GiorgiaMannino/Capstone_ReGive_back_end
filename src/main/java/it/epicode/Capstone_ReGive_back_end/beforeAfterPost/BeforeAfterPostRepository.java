package it.epicode.Capstone_ReGive_back_end.beforeAfterPost;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BeforeAfterPostRepository extends JpaRepository<BeforeAfterPost, Long> {
}
