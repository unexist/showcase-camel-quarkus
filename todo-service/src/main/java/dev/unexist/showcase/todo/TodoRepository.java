/**
 * @package Showcase-Hadoop-Camel-Quarkus
 *
 * @file
 * @copyright 2022 Christoph Kappel <christoph@unexist.dev>
 * @version $Id$
 *
 * This program can be distributed under the terms of the Apache License v2.0.
 * See the file LICENSE for details.
 **/

package dev.unexist.showcase.todo;

import io.quarkus.runtime.annotations.RegisterForReflection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RegisterForReflection
public class TodoRepository {
    private final Logger LOGGER = LoggerFactory.getLogger(TodoRoute.class);

    private final List<Todo> list;

    /**
     * Constructor
     **/

    TodoRepository() {
        this.list = new ArrayList<>();
    }

    /**
     * Add {@link Todo} entry to list
     *
     * @param todo
     *         {@link Todo} entry to add
     *
     * @return Either {@code true} on success; otherwise {@code false}
     **/

    public boolean add(final Todo todo) {
        todo.setId(this.list.size() + 1);

        return this.list.add(todo);
    }

    /**
     * Update {@link Todo} with given id
     *
     * @param todo
     *         A {@link Todo} to update
     *
     * @return Either {@code true} on success; otherwise {@code false}
     **/

    public boolean update(final Todo todo) {
        boolean ret = false;

        try {
            this.list.set(todo.getId(), todo);

            ret = true;
        } catch (IndexOutOfBoundsException e) {
            LOGGER.warn("update: id={} not found", todo.getId());
        }

        return ret;
    }

    /**
     * Delete {@link Todo} with given id
     *
     * @param id
     *         Id to delete
     *
     * @return Either {@code true} on success; otherwise {@code false}
     **/

    public boolean deleteById(int id) {
        boolean ret = false;

        try {
            this.list.remove(id);

            ret = true;
        } catch (IndexOutOfBoundsException e) {
            LOGGER.warn("deleteById: id={} not found", id);
        }

        return ret;
    }

    /**
     * Get all {@link Todo} entries
     *
     * @return List of all stored {@link Todo}
     **/

    public List<Todo> getAll() {
        return Collections.unmodifiableList(this.list);
    }

    /**
     * Find {@link Todo} by given id
     *
     * @param id
     *         Id to find
     *
     * @return A {@link Optional} with the result of the lookup
     **/

    public Optional<Todo> findById(int id) {
        return this.list.stream()
                .filter(t -> t.getId() == id)
                .findFirst();
    }
}