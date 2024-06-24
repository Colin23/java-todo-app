package com.colinmoerbe.javatodoapp.todo;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * This class represents a To-Do item with all its details. These are:
 * <ol>
 *     <li>id</li>
 *     <li>title</li>
 *     <li>description</li>
 *     <li>createdAt</li>
 *     <li>dueAt</li>
 *     <li>completed</li>
 * </ol>
 * This entity is used to store To-Do items in the database.
 * The {@link Entity} annotation marks this class as a JPA entity.
 * The {@link Table} annotation specifies the table in the database to which this entity maps.
 * The {@link Getter} and {@link Setter} annotations from Lombok automatically generate getter and setter methods for all fields.
 * The {@link AllArgsConstructor} and {@link NoArgsConstructor} annotations generate constructors with and without parameters respectively.
 * The {@link Builder} annotation creates builder APIs for this class.
*/
@Entity
@Table(name = "todo")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor()
@Builder
public class Todo {

    /**
     * The ID of the To-Do item.
     * <p></p>
     * {@link Id} specifies that this field is mapped to the primary key of the table.
     * {@link GeneratedValue} marks the value of this field to be generated automatically with the {@code IDENTITY} strategy.
     * {@link Column} maps this field to the column {@code id} in the database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    /**
     * The title of the To-Do item.
     * <p></p>
     * {@link NotBlank} makes sure the field is not null and contains at least one character.
     * {@link Column} maps this field to the column {@code todo_title} in the database.
     * {@link JsonProperty} sets an alias of the field for JSON properties to {@code todo_title}.
     */
    @NotBlank
    @Column(name = "todo_title", nullable = false)
    @JsonProperty("todo_title")
    private String title;

    /**
     * The description of the To-Do item.
     * <p></p>
     * {@link Column} maps this field to the column {@code todo_description} in the database.
     * {@link JsonProperty} sets an alias of the field for JSON properties to {@code todo_description}.
     */
    @Column(name = "todo_description")
    @JsonProperty("todo_description")
    private String description;

    /**
     * The date the To-Do item was created.
     * <p></p>
     * {@link Column} maps this field to the column {@code todo_created_at} in the database.
     * {@link JsonProperty} Sets an alias of the field for JSON properties to {@code todo_created_at}.
     */
    @Column(name = "todo_created_at", updatable = false, nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @JsonProperty("todo_created_at")
    private LocalDateTime createdAt;

    /**
     * The date by which the To-Do item should be completed.
     * <p></p>
     * {@link Column} maps this field to the column {@code todo_due_at} in the database.
     * {@link JsonProperty} Sets an alias of the field for JSON properties to {@code todo_due_at}.
     */
    @Column(name = "todo_due_at")
    @JsonProperty("todo_due_at")
    private LocalDateTime dueAt;

    /**
     * A boolean to represent if a To-Do is completed.
     * <p></p>
     * {@link Column} maps this field to the column {@code todo_completed} in the database.
     * {@link JsonProperty} Sets an alias of the field for JSON properties to {@code todo_completed}.
     */
    @Column(name = "todo_completed", nullable = false)
    @JsonProperty("todo_completed")
    private boolean completed;

    /**
     * Sets the {@code createdAt} field to the current timestamp before the entity is persisted.
     * This method ensures that the {@code createdAt} field is not null, aligning with the database schema's constraints.
     * The method is automatically invoked by JPA due to the {@link PrePersist} annotation.
     */
    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }
}