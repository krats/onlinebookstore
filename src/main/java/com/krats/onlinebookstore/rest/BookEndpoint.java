package com.krats.onlinebookstore.rest;

import com.krats.onlinebookstore.model.Book;
import com.krats.onlinebookstore.repository.BookRepository;

import javax.inject.Inject;
import javax.validation.constraints.Min;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;

@Path("/books")
public class BookEndpoint {

    @Inject
    private BookRepository bookRepository;

    @GET
    @Produces(APPLICATION_JSON)
    public Response getBooks() {
        List<Book> books = bookRepository.findAll();

        if(books.size() == 0) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }

        return Response.ok(books).build();
    }

    @GET
    @Produces(APPLICATION_JSON)
    @Path("/{id : \\d+}")
    public Response getBook(@PathParam("id") Long id) {
        Book book = bookRepository.find(id);

        if(book == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(book).build();
    }

    @DELETE
    @Path("/{id : \\d+}")
    public Response deleteBook(@PathParam("id") @Min(1) Long id) {
        bookRepository.delete(id);
        return Response.noContent().build();
    }

    @POST
    @Consumes(APPLICATION_JSON)
    public Response createBook(Book book, @Context UriInfo uriInfo) {
        book = bookRepository.create(book);
        URI createdUri = uriInfo.getBaseUriBuilder().path(book.getId().toString()).build();
        return Response.created(createdUri).build();
    }

    @GET
    @Path("/count")
    @Produces(TEXT_PLAIN)
    public Response countBooks() {
        Long nbOfBooks = bookRepository.countAll();

        if (nbOfBooks == 0)
            return Response.status(Response.Status.NO_CONTENT).build();

        return Response.ok(nbOfBooks).build();
    }
}
