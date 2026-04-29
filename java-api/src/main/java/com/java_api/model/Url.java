package com.java_api.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "urls")
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

    @Column(columnDefinition = "TEXT")
    private String url;

    @Column(name = "custom_slug", unique = true)
    private String customSlug;

    @Column(name = "click_count")
    private long clickCount;

    @Column(name = "max_click_count")
    private Long maxClickCount;

    @Column(columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean active;

    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "TIMESTAMPTZ")
    private OffsetDateTime createdAt;

    @Column(name = "expires_at", columnDefinition = "TIMESTAMPTZ")
    private OffsetDateTime expiresAt;

    @Column(name = "last_clicked_at", columnDefinition = "TIMESTAMPTZ")
    private OffsetDateTime lastClickedAt;

    @OneToMany(mappedBy = "url", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Click> clicks;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user;

    @PrePersist
    protected void onCreate() {
        if(this.createdAt == null) {
            this.createdAt = OffsetDateTime.now(ZoneOffset.UTC);
        }

        this.active = true;
    }
}
