package scitech.newsservice.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jsoup.nodes.Element;

import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import java.time.LocalDate;
import java.util.Arrays;
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
    private int likes;
    private int shows;
    //теги по принадлежности статьи
//    @OneToMany
//    private List<NewsTag> tags;
    @Column(length = 1000)
    private String tags; // "технологии,наука,политика"

    private LocalDate dateOfCreation;

    public List<String> getTagsList() {
        return Arrays.asList(this.tags.split(","));
    }

    public void setTagsList(List<String> tags) {
        this.tags = String.join(",", tags);
    }

    //newsObject.setContent(jsoupElement.outerHtml());

    //Element element = Jsoup.parse(newsObject.getContent()).body();


}
