package com.example.reactor_bug;

import java.util.stream.IntStream;

import java.io.InputStream;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
* Controller
*/
@Controller
public class ExampleController {

	@PostMapping(value = "/post", produces = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<MultiValueMap<String, InputStreamResource>> getMultipart(@RequestBody final String body) {
		final MultiValueMap<String, InputStreamResource> result = new LinkedMultiValueMap<>(10);
		IntStream.range(0, 10).forEach(i -> result.add("file" + i, new InputStreamResource(getClass().getClassLoader().getResourceAsStream("lorem_ipsum.txt"))));
		return ResponseEntity.ok(result);
	}

	
}
