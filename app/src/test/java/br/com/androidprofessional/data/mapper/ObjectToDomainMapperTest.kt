package br.com.androidprofessional.data.mapper

import br.com.androidprofessional.data.model.ExampleResponse
import br.com.androidprofessional.domain.model.ObjectDomain
import org.junit.Test
import kotlin.test.assertEquals

class ObjectToDomainMapperTest {

    private val mapper: ObjectToDomainMapper = ObjectToDomainMapper()

    @Test
    fun `when mapper should map to presentation`() {
        // When
        val result = mapper.map(ExampleResponse(id = 123))

        // Then
        assertEquals(result, ObjectDomain(id = 123))
    }
}