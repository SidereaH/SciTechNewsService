package scitech.newsservice.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jsoup.nodes.Element;

import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class NewsObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //в микро юзероу
    private Long ownerId;
    //делаем по б
    private String theme;
    @ManyToOne
    private Status status;
    private String title;
    private String description;
    @Lob
    private String content;
    private String url;
    //теги по принадлежности статьи
    @OneToMany
    private List<NewsTag> tags;


}
