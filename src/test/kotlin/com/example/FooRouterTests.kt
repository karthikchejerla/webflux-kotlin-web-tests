package com.example

import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.returnResult

@RunWith(SpringRunner::class)
@WebFluxTest
@Import(FooRouter::class) // See https://github.com/spring-projects/spring-boot/issues/10683
class FooRouterTests {

	@Autowired
	private lateinit var client: WebTestClient

	@MockBean
	private lateinit var repository: FooRepository

	@Test
	fun fooTest() {
		given(this.repository.foo()).willReturn("foo")
		client
				.get()
				.uri("/router/foo")
				.exchange()
				.expectStatus().isOk
				// TODO expectBody + KT-5464 workaround, see https://jira.spring.io/browse/SPR-15692?focusedCommentId=158700#comment-158700
				.returnResult<String>().responseBody.apply { assertEquals("foo", this) }
	}
}