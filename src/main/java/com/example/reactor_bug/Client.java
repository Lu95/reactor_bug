package com.example.reactor_bug;

import java.util.List;
import java.util.Map;

import java.nio.ByteBuffer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.ResolvableType;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ReactiveHttpInputMessage;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
* Client
*/
public class Client {

	private static final Logger log = LogManager.getLogger(Client.class);

	private final WebClient webClient = WebClient.builder()
		.baseUrl("http://localhost:9999")
		.codecs(codecs -> codecs.registerDefaults(false))
		.codecs(codecs -> codecs.customCodecs().register(new MultipartDecoder()))
		.build();

	private static final class MultipartDecoder implements HttpMessageReader<Map<String, String>> {

		@Override
		public List<MediaType> getReadableMediaTypes() {
			return List.of(MediaType.MULTIPART_FORM_DATA);
		}

		@Override
		public boolean canRead(final ResolvableType elementType, final MediaType mediaType) {
			return elementType.getRawClass().equals(Map.class) && mediaType != null && MediaType.MULTIPART_FORM_DATA.includes(mediaType);
		}

		@Override
		public Flux<Map<String, String>> read(ResolvableType elementType, ReactiveHttpInputMessage message,
				Map<String, Object> hints) {
			return Flux.firstWithSignal(message.getBody()).map(dataBuffer -> {
				final ByteBuffer buffer = dataBuffer.asByteBuffer();
				buffer.rewind();
				if (buffer.remaining() > 2048) return Map.of();
				throw new RuntimeException("Expected more than 2048 bytes");
			});
		}

		@Override
		public Mono<Map<String, String>> readMono(ResolvableType elementType, ReactiveHttpInputMessage message,
				Map<String, Object> hints) {
			return Mono.fromDirect(message.getBody()).map(dataBuffer -> {
				final ByteBuffer buffer = dataBuffer.asByteBuffer();
				buffer.rewind();
				if (buffer.remaining() > 2048) return Map.of();
				throw new RuntimeException("Expected more than 2048 bytes");
			});
		}

	}

	public void testPost() {
		log.info("Sending test post request");
		webClient.post()
			.uri("/post")
			.contentType(MediaType.APPLICATION_FORM_URLENCODED)
			.body((msg, ctx) -> msg.writeWith(Mono.just(DefaultDataBufferFactory.sharedInstance.wrap("test".getBytes()))))
			.exchangeToMono(resp -> resp.toEntity(Map.class)).doOnSuccess(entity -> {
				log.info("Received full response");
			}).block();
	}
	
}
