package com.example.tasklist

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.tasklist.databinding.ResTaskItemBinding

    // aqui dizemos que nossa classe é do tipo adapter
    // ela recebe um view holder - que é a classe TaskViewHolder que criamos
class TaskAdapter(
        private val onDeleteClick: (Task) -> Unit,
        private val onClick: (Task) -> Unit
    ) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    private val tasks = mutableListOf<Task>()

    // fazendo o view holder
    inner class TaskViewHolder(
        // ele recebe um item view do tipo res_task_item - que é o que a gente criou
        itemView: ResTaskItemBinding

        // essa classe vai ser do tipo view holder
        // <- itemView.root significa que estamos pegando o
        // item raiz que é o ResTaskItemBinding
    ): RecyclerView.ViewHolder(itemView.root) {

        // representação dos elementos do nosso layout aqui no adapter

        private val tvTaskTitle : TextView
        private val imgBtnDelete : ImageButton
        private val clTask: ConstraintLayout

        // ativando esses elementos
        init {
            tvTaskTitle = itemView.tvTaskTitle
            imgBtnDelete = itemView.imgBtnDelete
            clTask = itemView.clTask

            // agora os elementos do layout estão conectados com suas representações aqui
        }

        // essa função conecta o texto do tvTaskTitle com o parâmetro title da nossa data class Task

        fun bind(task: Task,
            onDeleteClick: (Task) -> Unit,
            onClick: (Task) -> Unit
        ){
            tvTaskTitle.text = task.title
            imgBtnDelete.setOnClickListener {
                onDeleteClick(task)
            }
            clTask.setOnClickListener {
                onClick(task)
            }


            if(task.completed){
                tvTaskTitle.setTextColor(
                    ContextCompat.getColor(itemView.context, R.color.white)
                )
                imgBtnDelete.setImageResource(R.drawable.deletewhite)
                clTask.setBackgroundColor(
                    ContextCompat.getColor(
                        itemView.context, R.color.complete_task
                    )
                )
            }
            else{
                tvTaskTitle.setTextColor(
                    ContextCompat.getColor(itemView.context, R.color.black)
                )
                imgBtnDelete.setImageResource(R.drawable.deleteblack)
                clTask.setBackgroundColor(
                    ContextCompat.getColor(
                        itemView.context, R.color.chinese_white
                    )
                )
            }
        }

    }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder =
             TaskViewHolder(
                ResTaskItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )


        override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
            holder.bind(tasks[position],
                onDeleteClick,
                onClick
            )
        }

        // colocar o tamanho da nossa lista
        override fun getItemCount(): Int = tasks.size

        fun addTask(task: Task){

            tasks.add(task)
            notifyItemInserted(tasks.size )
        }

        fun deleteTask(task: Task){

            // pegar a posição da task que a gente quer deletar
            val deletedposition = tasks.indexOf(task)
            tasks.remove(task)
            notifyItemRemoved(deletedposition)
        }

        fun updateTask(task: Task){
            val updatedposition = tasks.indexOf(task)
            tasks[updatedposition]= task
            notifyItemChanged(updatedposition)
        }

        fun isEmpty() = tasks.isEmpty()
    }