package cc.aliko.basic_todo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "todos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String title;

    private boolean completed = false;

    private LocalDateTime createdAt = LocalDateTime.now();
    
    public Todo(String title, boolean completed) {
        this.title = title;
        this.completed = completed;
        this.createdAt = LocalDateTime.now();
    }
}
