package br.com.androidprofessional.domain.mapper

import br.com.androidprofessional.domain.model.ObjectDomain
import br.com.androidprofessional.presentation.model.ObjectPresentation
import br.com.androidprofessional.utils.Mapper

class ObjectToPresentationMapper: Mapper<ObjectDomain, ObjectPresentation> {

    override fun map(source: ObjectDomain): ObjectPresentation {
        return ObjectPresentation(
            postId = source.postId,
            id = source.id,
            email = source.email,
            name = source.name,
            comment = source.comment
        )
    }
}