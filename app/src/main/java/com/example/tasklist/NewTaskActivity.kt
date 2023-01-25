package com.example.tasklist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tasklist.Constants.EXTRA_NEW_TASK
import com.example.tasklist.databinding.ActivityMainBinding
import com.example.tasklist.databinding.ActivityNewTaskBinding

class NewTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewTaskBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnSubmit.setOnClickListener {

            onSubmit()

        }

    }

    private fun onSubmit() {

        if (binding.etTitle.text.isEmpty()) {
            binding.etTitle.error = "Por favor, preencha o título da tarefa"
            binding.etTitle.requestFocus()
            return
        }

        if (binding.etDescription.text.isEmpty()) {
            binding.etDescription.error = "Por favor, preencha o descrição da tarefa"
            binding.etDescription.requestFocus()
            return
        }

        val newTask = Task(
            binding.etTitle.text.toString(),
            binding.etDescription.text.toString()
        )

        val intentResult = Intent()
        intentResult.putExtra(EXTRA_NEW_TASK, newTask)
        setResult(RESULT_OK, intentResult)
        finish()

    }



}