package br.com.androidprofessional.data.mapper

import br.com.androidprofessional.data.model.ExampleResponse
import br.com.androidprofessional.domain.model.ObjectDomain
import br.com.androidprofessional.utils.Mapper

/*
* Esta classe transforma um objeto da camada de DATA para um objeto da camada de DOMAIN.
* Lembre-se: Quanto mais isoladas suas camadas forem, maior sua
* flexibilidade para realizar mudanças sem gerar grandes impactos.
*/

class ObjectToDomainMapper: Mapper<ExampleResponse, ObjectDomain> {

    override fun map(source: ExampleResponse): ObjectDomain {
        return ObjectDomain(
            postId = source.postId,
            id = source.id,
            email = source.email,
            name = source.name,
            comment = source.comment
        )
    }
}