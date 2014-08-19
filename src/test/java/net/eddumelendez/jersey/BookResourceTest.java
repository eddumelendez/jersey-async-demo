package net.eddumelendez.jersey;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import junit.framework.Assert;

import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.Before;
import org.junit.Test;

public class BookResourceTest extends JerseyTest {
	
	String bookId1;
	String bookId2;

	@Before
	public void setup(){
		bookId1 = addBook("title1", "author1", new Date(), "1234").readEntity(Book.class).getId();
		bookId2 = addBook("title2", "author2", new Date(), "1234").readEntity(Book.class).getId();
	}
	
	@Override
	protected Application configure() {
		enable(TestProperties.LOG_TRAFFIC);
		enable(TestProperties.DUMP_ENTITY);
		final BookDao dao = new BookDao();
		return new BookApplication(dao);
	}
	
	protected Response addBook(String title, String author, Date date, String isbn, String... extras) {
//		Book book = new Book();
//		book.setTitle(title);
//		book.setAuthor(author);
//		book.setPublished(date);
//		book.setIsbn(isbn);
		Map<String, Object> book = new HashMap<String, Object>();
		book.put("title", title);
		book.put("author", author);
		book.put("published", date);
		book.put("isbn", isbn);
		if (extras != null) {
			int count = 1;
			for (String s : extras) {
				book.put("extra" + count++, s);
			}
		}
			
		//Entity<Book> bookEntity = Entity.entity(book, MediaType.APPLICATION_JSON_TYPE);
		Entity<Map<String, Object>> bookEntity = Entity.entity(book, MediaType.APPLICATION_JSON_TYPE);
		return target("books").request().post(bookEntity);
	}
	
	@Test
	public void testAddBook() {
		Response response = addBook("title", "author", new Date(), "1234");
		Assert.assertEquals(200, response.getStatus());
		Map<String, Object> responseBook = toHashMap(response);
		Assert.assertNotNull(responseBook.get("id"));
		Assert.assertEquals("title", responseBook.get("title"));
		Assert.assertEquals("author", responseBook.get("author"));
	}

	@Test
	public void testGetBook() {
		Map<String, Object> response = toHashMap(target("books").path(bookId1).request().get());
		Assert.assertNotNull(response);
	}

	@Test
	public void testGetBooks() {
		Collection<Book> response = target("books").request().get(
				new GenericType<Collection<Book>>() {
				});
		Assert.assertEquals(2, response.size());
	}

	protected HashMap<String, Object> toHashMap(Response response) {
		return response.readEntity(new GenericType<HashMap<String, Object>>(){});
	}
	
	@Test
	public void testAddExtraField() {
		Response response = addBook("title", "author", new Date(), "1234", "Hello word");
		Assert.assertEquals(200, response.getStatus());
	}
	
}
