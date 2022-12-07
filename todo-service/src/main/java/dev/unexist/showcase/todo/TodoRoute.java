/**
 * @package Showcase-Hadoop-Camel-Quarkus
 *
 * @file Todo route
 * @copyright 2022 Christoph Kappel <christoph@unexist.dev>
 * @version $Id$
 *
 * This program can be distributed under the terms of the Apache License v2.0.
 * See the file LICENSE for details.
 **/
 
package dev.unexist.showcase.todo;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

// camel-k: dependency=mvn:org.apache.camel.quarkus:camel-quarkus-jackson

@ApplicationScoped
public class TodoRoute extends RouteBuilder {
    @Inject
    private TodoService todoService;

    @Override
    public void configure() throws Exception {
        restConfiguration().bindingMode(RestBindingMode.json);

        /* Create REST endpoint */
        rest("todo")
            .post("/")
                .consumes("application/json").type(Todo.class)
                .to("direct:createTodo")

            .get("/{id}")
                .to("direct:findById")

            .put("/{id}")
                .consumes("application/json").type(Todo.class)
                .to("direct:update")

            .delete("/{id}")
                .to("direct:delete")

            .get()
                .to("direct:getAll");

        /* From mapping */
        from("direct:createTodo")
            .bean(todoService, "create(${body})")
            .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(201));

        from("direct:update")
            .bean(todoService, "update(${header.id},${body})")
            .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(204));

        from("direct:delete")
            .bean(todoService, "delete(${header.id})")
            .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(204));


        from("direct:getAll")
            .bean(todoService, "getAll()");
    }
}
