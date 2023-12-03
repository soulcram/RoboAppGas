package br.com.m3tech.AppGas.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.m3tech.AppGas.dao.TaskRepository;
import br.com.m3tech.AppGas.model.Task;

@Service
public class TaskService {
	
	private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public void salvarTask(Task task) {
        taskRepository.save(task);
    }

    public Task buscarTaskPorId(Long id) {
        return taskRepository.findById(id).orElse(null);
    }
    
    public Task buscarPrimeiro() {
    	
    	List<Task> tasks = taskRepository.findAll();
    	
    	if(tasks != null && !tasks.isEmpty()) {
    		return tasks.get(0);
    	}
        return new Task("https://api.deliveryvip.com.br","Rz_Sv-odNiICnXfemBduJhKGNp0JEdmr_YXMt8Kzfzg","7BsZzckJ6N7C6KXF3OHx-kMgWa5oJZI4URu-fpOCYSU","Teste");
    }

}
