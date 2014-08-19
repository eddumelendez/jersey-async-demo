package net.eddumelendez.jersey;

import java.util.Collection;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.server.ManagedAsync;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

@Path("/books")
public class BookResource {
	
	@Context
	BookDao dao;
	//BookDao dao = new BookDao();
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ManagedAsync
	public void getBooks(@Suspended final AsyncResponse response) {
		//return dao.getBooks();
		ListenableFuture<Collection<Book>> bookFuture = dao.getBooksAsync();
		Futures.addCallback(bookFuture, new FutureCallback<Collection<Book>>() {
			@Override
			public void onSuccess(Collection<Book> books) {
				response.resume(books);
			}
			@Override
			public void onFailure(Throwable thrown) {
				response.resume(thrown);
			}
		});
	}
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@ManagedAsync
	public void getBook(@PathParam("id") String id, @Suspended final AsyncResponse response) {
		//return dao.getBook(id);
		ListenableFuture<Book> bookFuture = dao.getBookAsync(id);
		Futures.addCallback(bookFuture, new FutureCallback<Book>() {
			@Override
			public void onSuccess(Book book) {
				response.resume(book);
			}
			@Override
			public void onFailure(Throwable thrown) {
				response.resume(thrown);
			}
		});
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ManagedAsync
	public void addBook(Book book, @Suspended final AsyncResponse response) {
		//response.resume(dao.addBook(book));
		ListenableFuture<Book> bookFuture = dao.addBookAsync(book);
		Futures.addCallback(bookFuture, new FutureCallback<Book>() {
			@Override
			public void onSuccess(Book addedBook) {
				response.resume(addedBook);
			}
			@Override
			public void onFailure(Throwable thrown) {
				response.resume(thrown);
			}
		});
	}
	
}
