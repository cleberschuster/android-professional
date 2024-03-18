package br.com.androidprofessional.presentation

/**
 * Este projeto tem por objetivo servir como base de estudos para quem está aprendendo
 * sobre MVVM e Clean Architecture.
 *
 * Toda a arquitetura aqui apresentada visa seguir os padrões de SOLID e código limpo.
 *
 * Reservo-me no direito de corrigir pequenos ajustes que se façam necessários.
 *
 * @author Cleber
 * Criado em Março de 2024
 *
 */

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import br.com.androidprofessional.data.api.Status
import br.com.androidprofessional.databinding.ActivityMainBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class YourActivity : AppCompatActivity() {

    private val viewModel: YourViewModel by viewModel() // Injetando a viewModel

    //create a view binding variable
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //instantiate view binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Listen for the button click event to search
        binding.button.setOnClickListener {

            //check to prevent api call with no parameters
            if (binding.searchEditText.text.isNullOrEmpty()) {
                Toast.makeText(this, "Query Can't be empty", Toast.LENGTH_SHORT).show()
            } else {

                //if Query isn't empty, make the api call
                viewModel.getNewComment(binding.searchEditText.text.toString().toInt())
            }
        }

        fetchData()
    }

    private fun fetchData() {
        //Since flow run asynchronously, start listening on background thread
        lifecycleScope.launch {

            /**
             * Collect the data emitted in CommentApiState helper class,
             * It may contain error,success or loading state.
             * Update the UI according to the State received
             */
            viewModel.commentState.collect {

                //When state to check the state of received data
                when (it.status) {

                    //If its loading state then show the progress bar
                    Status.LOADING -> {
                        binding.progressBar.isVisible = true
                    }
                    //If api call was a success , Update the Ui with data and make progress bar invisible
                    Status.SUCCESS -> {
                        binding.progressBar.isVisible = false
                        it.data?.let { comment ->

                            binding.commentIdTextview.text = comment.id.toString()
                            binding.nameTextview.text = comment.name
                            binding.emailTextview.text = comment.email
                            binding.commentTextview.text = comment.comment
                        }
                    }
                    //In case of error, show some data to user
                    else -> {
                        binding.progressBar.isVisible = false
                        Toast.makeText(this@YourActivity, "${it.message}", Toast.LENGTH_SHORT)
                            .show()
                    }
                }


            }
        }
    }
}