package kz.kazgeowarning.newservice.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "news")
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 2000)
    private String title;

    @Column(length = 2000)
    private String subtitle;

    @Column(length = 20000)
    private String text;

    @Column(nullable = false)
    private Date publicationDate;

    @Column(nullable = false)
    private String category;
}
