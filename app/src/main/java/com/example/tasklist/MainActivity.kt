package com.example.tasklist

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.tasklist.Constants.EXTRA_NEW_TASK
import com.example.tasklist.databinding.ActivityMainBinding
import com.example.tasklist.databinding.ResTaskItemBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: TaskAdapter
    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->

        // ele vai executar isso quando voltarmos da new task activity
        // verifica se o result code é ok
        if (result.resultCode != RESULT_OK)
            return@registerForActivityResult

        // se for ok, ele pega a task que veio como dado extra na intent e adiciona no adapter
        val task = result.data?.extras?.getSerializable(EXTRA_NEW_TASK) as Task
        adapter.addTask(task)
        onDataUpdate()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAdapter()
        setupLayout()

    }

    private fun onDataUpdate() = if (adapter.isEmpty()) {
        binding.rvTasks.visibility = View.GONE
        binding.tvNoData.visibility = View.VISIBLE
    } else {
        binding.rvTasks.visibility = View.VISIBLE
        binding.tvNoData.visibility = View.GONE
    }

    private fun setupLayout() {
        binding.fabNewTask.setOnClickListener {
            resultLauncher.launch(Intent(this, NewTaskActivity::class.java))

        }
    }
    private fun setupAdapter() {
        adapter = TaskAdapter(
            onDeleteClick = { taskToConfirmDelection ->
                showDeleteConfirmation(taskToConfirmDelection){ task ->
                    adapter.deleteTask(task)
                }


            },
            onClick = { taskToBeShowed ->

                showTaskDetails(taskToBeShowed){ taskToBeUpdated ->
                    adapter.updateTask(taskToBeUpdated)                }

            }
        )

        binding.rvTasks.adapter = adapter
    }

    private fun showDeleteConfirmation(task: Task, onConfirm: (Task) -> Unit){
        val builder = AlertDialog.Builder(this)
        builder.apply {
            setTitle("Confirmação")
            setMessage("Deseja deletar a tarefa \"${task.title}\"?")
            setPositiveButton("Sim"){_,_ ->
                onConfirm(task)
            }
            setNegativeButton("Não"){dialog, _ ->
                dialog.dismiss()

            }
        }
        builder.show()

    }


    private fun showTaskDetails(task: Task, onTaskUpdated: (Task) -> Unit){
        val builder = AlertDialog.Builder(this)
        builder.apply {
            setTitle("Detalhes da tarefa")
            setMessage(
                """
                    Título: ${task.title}
                    Descrição: ${task.description}
                    Concluída: ${
                        if(task.completed){
                            "Sim"
                        }
                        else{
                            "Não"
                        }
                    }
                """.trimIndent()
            )
            setPositiveButton(
                if(task.completed)
                    "Não concluída"
                else
                    "Concluída"
            ){_,_ ->
                task.completed = !task.completed
                onTaskUpdated(task)
            }
            setNegativeButton("Fechar   "){dialog, _ ->
                dialog.dismiss()

            }
        }
        builder.show()
    }
}