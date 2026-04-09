package com.java_api.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "url")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Url {
    @Id
    @SequenceGenerator(
            name = "url_gen",
            sequenceName = "url_seq",
            initialValue = 1,
            allocationSize = 1
    )
    @EqualsAndHashCode.Include
    private Long id;

    @Column(columnDefinition = "TEXT", unique = true)
    private String url;

    @Column(name = "short_url", unique = true)
    private String shortUrl;
}
