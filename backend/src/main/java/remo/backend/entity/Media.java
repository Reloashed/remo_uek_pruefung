package remo.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Table(name = "media")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Media {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String media;
    @Enumerated(EnumType.STRING)
    private MediaStatus mediaStatus;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account owner;

    @ManyToMany
    @JoinTable(name = "media_likes",
            joinColumns = @JoinColumn(name = "media_id"),
            inverseJoinColumns = @JoinColumn(name = "account_id")
    )
    private List<Account> likedBy;
}
