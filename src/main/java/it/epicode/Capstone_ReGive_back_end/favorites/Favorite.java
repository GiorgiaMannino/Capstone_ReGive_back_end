package it.epicode.Capstone_ReGive_back_end.favorites;

import it.epicode.Capstone_ReGive_back_end.articles.Article;
import it.epicode.Capstone_ReGive_back_end.auth.AppUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Entity
@Table(name = "favorites", uniqueConstraints = { @UniqueConstraint(columnNames = {"user_id", "article_id"})
})
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private AppUser user;

    @ManyToOne(optional = false)
    private Article article;
}