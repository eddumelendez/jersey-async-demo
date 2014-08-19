package net.eddumelendez.jersey;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

public class BookApplication extends ResourceConfig {

	public BookApplication(final BookDao dao) {
		JacksonJsonProvider json = new JacksonJsonProvider().configure(
				SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
				.configure(SerializationFeature.INDENT_OUTPUT, true);
		packages("net.eddumelendez.jersey").register(new AbstractBinder() {

			@Override
			protected void configure() {
				bind(dao).to(BookDao.class);
			}
		}).register(json).property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, false);
	}

}
