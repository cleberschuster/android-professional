package br.com.androidprofessional

import br.com.androidprofessional.utils.BaseRobot

/*
* Esta é a classe responsável por realizar os seus Assertions, ou seja,
* validar se a sua view está mostrando o cenário esperado.
* Caso você precise fazer um click na tela, por exemplo, o ideal é criar uma classe
* separada, chamada `YourActivityActionRobot`, por exemplo.
*
* Dessa forma, as responsabilidades ficarão bem separadas.
*/
fun check(func: YourActivityAssertionRobot.() -> Unit) =
    YourActivityAssertionRobot().apply { func() }

open class YourActivityAssertionRobot : BaseRobot() {

    fun titleIsVisible() {
        checkViewHasText(R.id.comment_textview, "Comment Text")
    }
}


