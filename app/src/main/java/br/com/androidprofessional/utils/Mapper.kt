package br.com.androidprofessional.utils

interface Mapper<S, T> {
    fun map(source: S): T
}